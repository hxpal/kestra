package io.kestra.webserver.controllers.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.kestra.core.exceptions.IllegalVariableEvaluationException;
import io.kestra.core.models.dashboards.Dashboard;
import io.kestra.core.models.dashboards.DashboardWithSource;
import io.kestra.core.models.flows.Flow;
import io.kestra.core.models.flows.FlowWithSource;
import io.kestra.core.repositories.DashboardRepositoryInterface;
import io.kestra.core.serializers.JacksonMapper;
import io.kestra.core.tenant.TenantService;
import io.kestra.webserver.responses.PagedResults;
import io.kestra.webserver.utils.PageableUtils;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.validation.Validated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Validated
@Controller("/api/v1/dashboards")
@Slf4j
public class DashboardController {
    @Inject
    private DashboardRepositoryInterface dashboardRepository;

    @Inject
    private TenantService tenantService;

    @ExecuteOn(TaskExecutors.IO)
    @Get
    @Operation(tags = {"Dashboards"}, summary = "List all dashboards")
    public PagedResults<Dashboard> list(
        @Parameter(description = "The current page") @QueryValue(defaultValue = "1") @Min(1) int page,
        @Parameter(description = "The current page size") @QueryValue(defaultValue = "10") @Min(1) int size,
        @Parameter(description = "The sort of current page") @Nullable @QueryValue List<String> sort
    ) throws ConstraintViolationException, IllegalVariableEvaluationException {
        return PagedResults.of(dashboardRepository.list(PageableUtils.from(page, size, sort), tenantService.resolveTenant()));
    }

    @ExecuteOn(TaskExecutors.IO)
    @Get(uri = "{id}")
    @Operation(tags = {"Dashboards"}, summary = "Retrieve a dashboard")
    public DashboardWithSource index(
        @Parameter(description = "The dashboard id") @PathVariable String id
    ) throws ConstraintViolationException, IllegalVariableEvaluationException {
        return dashboardRepository.get(tenantService.resolveTenant(), id).orElse(null);
    }

    @ExecuteOn(TaskExecutors.IO)
    @Post(consumes = MediaType.APPLICATION_YAML)
    @Operation(tags = {"Dashboards"}, summary = "Create a dashboard from yaml source")
    public HttpResponse<DashboardWithSource> create(
        @Parameter(description = "The dashboard") @Body String dashboard
    ) throws ConstraintViolationException, JsonProcessingException {
        Dashboard dashboardParsed = JacksonMapper.ofYaml().readValue(dashboard, Dashboard.class).toBuilder().deleted(false).build();

        if (dashboardParsed.getId() != null) {
            throw new IllegalArgumentException("Dashboard id is not editable");
        }

        return HttpResponse.ok(dashboardRepository.save(dashboardParsed, dashboard));
    }

    @Put(uri = "{id}", consumes = MediaType.APPLICATION_YAML)
    @ExecuteOn(TaskExecutors.IO)
    @Operation(tags = {"Dashboards"}, summary = "Update a dashboard")
    public HttpResponse<DashboardWithSource> update(
        @Parameter(description = "The dashboard id") @PathVariable String id,
        @Parameter(description = "The dashboard") @Body String dashboard
    ) throws ConstraintViolationException, JsonProcessingException {
        Optional<DashboardWithSource> existingFlow = dashboardRepository.get(tenantService.resolveTenant(), id);
        if (existingFlow.isEmpty()) {
            return HttpResponse.status(HttpStatus.NOT_FOUND);
        }
        Dashboard dashboardToSave = JacksonMapper.ofYaml().readValue(dashboard, Dashboard.class).toBuilder().deleted(false).build();

        return HttpResponse.ok(dashboardRepository.save(existingFlow.get(), dashboardToSave, dashboard));
    }

    @ExecuteOn(TaskExecutors.IO)
    @Get(uri = "{id}/graph/{graphId}")
    @Operation(tags = {"Dashboards"}, summary = "Generate a dashboard graph")
    public Object dashboardGraph(
        @Parameter(description = "The dashboard id") @PathVariable String id,
        @Parameter(description = "The graph id") @PathVariable String graphId,
        @Parameter(description = "The filters to apply") @Body Map<String, Object> filters
    ) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
