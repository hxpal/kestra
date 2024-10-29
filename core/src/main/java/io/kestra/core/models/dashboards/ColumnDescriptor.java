package io.kestra.core.models.dashboards;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
@Getter
@NoArgsConstructor
public class ColumnDescriptor {
    @NotNull
    @NotBlank
    private String field;
    private String displayName;
    private AggregationType agg;
}
