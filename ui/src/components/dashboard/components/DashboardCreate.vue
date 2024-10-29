<template>
    <top-nav-bar :title="routeInfo.title" />
    <section class="full-container">
        <dashboard-editor @save="save" :initial-source="initialSource" />
    </section>
</template>

<script>
    import RouteContext from "../../../mixins/routeContext";
    import TopNavBar from "../../../components/layout/TopNavBar.vue";
    import DashboardEditor from "./DashboardEditor.vue";

    export default {
        mixins: [RouteContext],
        components: {
            DashboardEditor,
            TopNavBar
        },
        data() {
            return {
                initialSource: `id: countries
                    title: Executions per country
                    description: Count executions per country label and execution state
                    timeWindow:
                      default: P30D # P30DT30H
                      max: P365D

                    charts:
                      - id: time_series
                        type: io.kestra.plugin.core.dashboards.TimeSeries
                        timeseries:
                          column: executionDate
                        panelOptions:
                          title: Executions per country over time
                          description: Count executions per country label and execution state # optional icon on hover
                          tooltip: ALL # ALL, NONE, SINGLE
                          legend:
                            enabled: true # later on possible to extend it e.g. position AUTO, LEFT, RIGHT, TOP, BOTTOM
                          # colorScheme: CLASSIC # PURPLE - TBD - we may sync with the Settings color scheme as in the main dashboard
                        data:
                          type: io.kestra.plugin.core.dashboards.data.Executions # also: Logs and Metrics available
                          columns:
                            executionDate:
                              field: execution.startDate
                              displayName: Execution Date
                            country:
                              field: label.country
                              displayName: Country
                              # alternative definition timeseries: true
                            state:
                              field: execution.state
                              displayName: Execution State
                            duration: # right vertical axis
                              field: execution.duration
                              displayName: Execution Duration
                              agg: SUM
                              graphStyle: LINES # LINES, BARS, POINTS
                            total: # left vertical axis
                              field: execution
                              displayName: Total Executions
                              agg: COUNT
                              graphStyle: BARS # LINES, BARS, POINTS
                          where:
                            - field: namespace
                              type: IN
                              value:
                                - aimtec.production
                                - aimtec.partners
                          orderBy:
                            total: DESC
                            duration: ASC

                      - id: markdown_section
                        type: io.kestra.plugin.core.dashboards.charts.Markdown
                        panelOptions:
                          title: Executions per country over time
                          description: Count executions per country label and execution state # optional icon on hover
                        markdownContent: |
                          ## This is a markdown panel

                      - id: executions_per_country
                        type: io.kestra.plugin.core.dashboards.BarChart
                        panelOptions:
                          title: Executions per country
                          description: Count executions per country label and execution state # optional icon on hover
                          tooltip: ALL # ALL, NONE, SINGLE
                          legend:
                            enabled: true # later on possible to extend it e.g. position AUTO, LEFT, RIGHT, TOP, BOTTOM
                          # colorScheme: CLASSIC # PURPLE - TBD - we may sync with the Settings color scheme as in the main dashboard
                        axes:
                          horizontal:
                            - column: namespace
                              limit: 100
                            - column: state
                              limit: 10
                        data:
                          type: io.kestra.plugin.core.dashboards.data.Executions
                          columns:
                            namespace:
                              field: namespace
                              displayName: Namespace
                            state:
                              field: execution.state
                              displayName: Execution State
                            total: # left vertical axis
                              field: execution
                              displayName: Total Executions
                              agg: COUNT
                          orderBy:
                            total: DESC

                      - id: total_executions_per_country
                        type: io.kestra.plugin.core.dashboards.charts.PieChart
                        panelOptions:
                          title: Executions per country
                          description: Count executions per country label and execution state
                          graphStyle: PIE # PIE, DONUT - donutdefault
                          legend:
                            enabled: true # later on possible to extend it e.g. position AUTO, LEFT, RIGHT, TOP, BOTTOM
                          # colorScheme: CLASSIC # PURPLE - TBD - we may sync with the Settings color scheme as in the main dashboard
                        data:
                          type: io.kestra.plugin.core.dashboards.data.Executions
                          columns:
                            country:
                              field: label.country
                              displayName: Country
                            total:
                              field: execution
                              agg: COUNT
                              displayName: Total Executions

                      - id: table
                        type: io.kestra.plugin.core.dashboards.charts.Table
                        panelOptions:
                          title: Executions per country
                          description: Count executions per country label and execution state
                          header:
                            enabled: true # header = column names; in the future can add customization
                          pagination:
                            enabled: true # in the future: possible to add page size
                        data:
                          type: io.kestra.plugin.core.dashboards.data.Executions
                          columns:
                            country:
                              field: label.country
                              displayName: Country
                              columnAlignment: left #  right, center
                            project:
                              field: label.project
                            env:
                              field: label.env
                            state:
                              field: execution.state
                              displayName: Execution State
                            total: # left vertical axis
                              field: execution
                              agg: COUNT # we group by all columns that don't have aggregation
                              displayName: Total Executions
                            duration:
                              field: execution.duration
                              agg: SUM
                          where:
                            - field: namespace
                              type: IN
                              value:
                                - aimtec.production
                                - aimtec.partners
                            - field: duration
                              type: GREATER_THAN_OR_EQUAL_TO
                              value: 30
                            - type: OR
                              value:
                                - field: execution.state
                                  type: EQUAL_TO
                                  value: CREATED
                                - field: label.country
                                  type: EQUAL_TO
                                  value: Italy
                          orderBy:
                            total: DESC
                            duration: ASC
                    # possible WHERE filters are EQUAL_TO, NOT_EQUAL_TO, GREATER_THAN, LESS_THAN, BETWEEN, GREATER_THAN_OR_EQUAL_TO, LESS_THAN_OR_EQUAL_TO, IS_EMPTY, NOT_EMPTY
                    #    layout:
                    #      width: 24 # int nr max 24
                    #      height: 8 # int nr max 24`
            }
        },
        methods: {
            async save(input) {
                const dashboard = await this.$store.dispatch("dashboard/create", input);
                this.$store.dispatch("core/isUnsaved", false);
                this.$router.push({name: "dashboards/update", params: {id: dashboard.id}});
            }
        },
        computed: {
            routeInfo() {
                return {
                    title: this.$t("dashboards")
                };
            }
        }
    };
</script>
