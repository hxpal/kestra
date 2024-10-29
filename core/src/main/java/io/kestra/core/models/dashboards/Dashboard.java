package io.kestra.core.models.dashboards;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.kestra.core.models.DeletedInterface;
import io.kestra.core.models.HasUID;
import io.kestra.core.utils.IdUtils;
import io.micronaut.core.annotation.Introspected;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.List;

@SuperBuilder(toBuilder = true)
@Getter
@NoArgsConstructor
@Introspected
@ToString
@EqualsAndHashCode
public class Dashboard implements HasUID, DeletedInterface {
    @Hidden
    @Pattern(regexp = "^[a-z0-9][a-z0-9_-]*")
    protected String tenantId;

    protected String id;

    @NotNull
    @NotBlank
    protected String title;

    protected String description;

    protected TimeWindow timeWindow;

    protected List<Chart> charts;

    @NotNull
    @Builder.Default
    protected boolean deleted = false;

    @Setter(AccessLevel.NONE)
    protected Instant created;

    @Setter(AccessLevel.NONE)
    protected Instant updated;

    @Override
    @JsonIgnore
    public String uid() {
        return IdUtils.fromParts(
            tenantId,
            id
        );
    }
}
