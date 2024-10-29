package io.kestra.jdbc.repository;

import io.kestra.core.events.CrudEvent;
import io.kestra.core.events.CrudEventType;
import io.kestra.core.models.dashboards.Dashboard;
import io.kestra.core.models.dashboards.DashboardWithSource;
import io.kestra.core.repositories.ArrayListTotal;
import io.kestra.core.repositories.DashboardRepositoryInterface;
import io.kestra.core.utils.IdUtils;
import io.micronaut.context.event.ApplicationEventPublisher;
import io.micronaut.data.model.Pageable;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.time.Instant;
import java.util.*;

@Slf4j
@AllArgsConstructor
public abstract class AbstractJdbcDashboardRepository extends AbstractJdbcRepository implements DashboardRepositoryInterface {
    protected io.kestra.jdbc.AbstractJdbcRepository<Dashboard> jdbcRepository;
    private final ApplicationEventPublisher<CrudEvent<Dashboard>> eventPublisher;

    @Override
    public Optional<DashboardWithSource> get(String tenantId, String id) {
        return jdbcRepository
            .getDslContextWrapper()
            .transactionResult(configuration -> {
                DSLContext context = DSL.using(configuration);
                Select<Record2<String, String>> from;

                from = context
                        .select(
                            field("source_code", String.class),
                            field("value", String.class)
                        )
                        .from(jdbcRepository.getTable())
                        .where(this.defaultFilter(tenantId))
                        .and(field("id", String.class).eq(id));
                Record2<String, String> fetched = from.fetchAny();

                if (fetched == null) {
                    return Optional.empty();
                }

                Dashboard dashboard = jdbcRepository.map(fetched);
                String source = fetched.get("source_code", String.class);
                return Optional.of(DashboardWithSource.of(dashboard, source));
            });
    }

    @Override
    public ArrayListTotal<Dashboard> list(Pageable pageable, String tenantId) {
        return this.jdbcRepository
            .getDslContextWrapper()
            .transactionResult(configuration -> {
                DSLContext context = DSL.using(configuration);

                SelectConditionStep<Record1<Object>> select = context
                    .select(
                        field("value")
                    )
                    .hint(context.configuration().dialect().supports(SQLDialect.MYSQL) ? "SQL_CALC_FOUND_ROWS" : null)
                    .from(jdbcRepository.getTable())
                    .where(this.defaultFilter(tenantId));

                return this.jdbcRepository.fetchPage(context, select, pageable);
            });
    }

    @Override
    public DashboardWithSource save(Dashboard previousDashboard, Dashboard dashboard, String source) throws ConstraintViolationException {
        Map<Field<Object>, Object> fields = this.jdbcRepository.persistFields(dashboard);
        fields.put(field("source_code"), source);

        if (previousDashboard == null) {
            dashboard = dashboard.toBuilder().id(IdUtils.create()).build();
        } else {
            dashboard = dashboard.toBuilder().id(previousDashboard.getId()).created(previousDashboard.getCreated()).updated(Instant.now()).build();
        }

        this.jdbcRepository.persist(dashboard, fields);

        if (previousDashboard == null) {
            eventPublisher.publishEvent(new CrudEvent<>(dashboard, CrudEventType.CREATE));
        } else {
            eventPublisher.publishEvent(new CrudEvent<>(dashboard, previousDashboard, CrudEventType.UPDATE));
        }

        return DashboardWithSource.of(dashboard, source);
    }
}
