package io.kestra.core.models.dashboards;

import io.kestra.core.models.flows.Flow;
import io.kestra.core.services.FlowService;
import io.micronaut.core.annotation.Introspected;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
@Getter
@NoArgsConstructor
@Introspected
@ToString
public class DashboardWithSource extends Dashboard {
    String source;

    public DashboardWithSource toDeleted() {
        return this.toBuilder()
            .deleted(true)
            .build();
    }

    @SuppressWarnings("deprecation")
    public static DashboardWithSource of(Dashboard dashboard, String source) {
        return DashboardWithSource.builder()
            .tenantId(dashboard.tenantId)
            .id(dashboard.id)
            .title(dashboard.title)
            .description(dashboard.description)
            .deleted(dashboard.deleted)
            .timeWindow(dashboard.timeWindow)
            .charts(dashboard.charts)
            .source(source)
            .created(dashboard.created)
            .updated(dashboard.updated)
            .build();
    }
}
