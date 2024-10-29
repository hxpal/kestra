package io.kestra.core.models.dashboards;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
@Getter
@NoArgsConstructor
public abstract class PanelOption {
    @NotNull
    @NotBlank
    private String title;

    private String description;
}
