package io.kestra.core.models.dashboards;

import io.kestra.core.models.annotations.Plugin;
import io.kestra.core.models.dashboards.filters.AbstractFilter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@SuperBuilder(toBuilder = true)
@Getter
@NoArgsConstructor
@Plugin
public class DataFilter {
    @NotNull
    @NotBlank
    @Pattern(regexp="\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*(\\.\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*)*")
    private String type;

    // TODO ColumnDescriptor should include extra properties depending on the Graph type
    private Map<String, ColumnDescriptor> columns;

    private List<AbstractFilter> where;

    private Map<String, Order> orderBy;
}
