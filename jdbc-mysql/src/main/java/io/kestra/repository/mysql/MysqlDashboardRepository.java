package io.kestra.repository.mysql;

import io.kestra.core.events.CrudEvent;
import io.kestra.core.models.dashboards.Dashboard;
import io.kestra.jdbc.repository.AbstractJdbcDashboardRepository;
import io.micronaut.context.event.ApplicationEventPublisher;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

@Singleton
@MysqlRepositoryEnabled
public class MysqlDashboardRepository extends AbstractJdbcDashboardRepository {
    @Inject
    public MysqlDashboardRepository(@Named("dashboards") MysqlRepository<Dashboard> repository,
                                    ApplicationEventPublisher<CrudEvent<Dashboard>> eventPublisher) {
        super(repository, eventPublisher);
    }
}
