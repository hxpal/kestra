package io.kestra.core.models.dashboards.filters;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@NoArgsConstructor
public class NotEqualTo extends AbstractFilter {
    @NotNull
    @JsonInclude
    @Builder.Default
    protected String type = "NOT_EQUAL_TO";

    @NotNull
    private String value;
}