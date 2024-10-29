package io.kestra.core.models.dashboards.filters;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
@NoArgsConstructor
public class Or extends AbstractFilter {
    @NotNull
    @JsonInclude
    @Builder.Default
    protected String type = "OR";

    @NotNull
    private List<AbstractFilter> value;
}