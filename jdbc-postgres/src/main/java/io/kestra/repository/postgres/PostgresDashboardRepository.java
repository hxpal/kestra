package io.kestra.repository.postgres;

import io.kestra.core.events.CrudEvent;
import io.kestra.core.models.dashboards.Dashboard;
import io.kestra.core.models.flows.Flow;
import io.kestra.jdbc.repository.AbstractJdbcDashboardRepository;
import io.kestra.jdbc.repository.AbstractJdbcFlowRepository;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.event.ApplicationEventPublisher;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.jooq.Condition;

import java.util.Map;

@Singleton
@PostgresRepositoryEnabled
public class PostgresDashboardRepository extends AbstractJdbcDashboardRepository {
    @Inject
    public PostgresDashboardRepository(@Named("dashboards") PostgresRepository<Dashboard> repository,
                                       ApplicationEventPublisher<CrudEvent<Dashboard>> eventPublisher) {
        super(repository, eventPublisher);
    }
}
