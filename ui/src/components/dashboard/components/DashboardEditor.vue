<template>
    <el-row>
        <el-col>
            <editor
                @save="$emit('save', $event)"
                v-model="source"
                schema-type="dashboard"
                lang="yaml"
                @update:model-value="source = $event"
                @cursor="updatePluginDocumentation"
                :creating="true"
                :read-only="false"
                :navbar="false"
            />
        </el-col>
        <el-col>
            <iframe :src="`http://localhost:5173/dashboards/${YamlUtils.parse(source).id}`" />
        </el-col>
    </el-row>
</template>

<script>
    import RouteContext from "../../../mixins/routeContext";
    import Editor from "../../inputs/Editor.vue";
    import YamlUtils from "../../../utils/yamlUtils.js";

    export default {
        computed: {
            YamlUtils() {
                return YamlUtils
            }
        },
        mixins: [RouteContext],
        emits: ["save"],
        props: {
            initialSource: {
                type: String,
                default: undefined
            }
        },
        components: {
            Editor
        },
        methods: {
            updatePluginDocumentation(event) {
                const taskType = YamlUtils.getTaskType(
                    event.model.getValue(),
                    event.position
                );
                const pluginSingleList = this.$store.getters["plugin/getPluginSingleList"];
                if (taskType && pluginSingleList && pluginSingleList.includes(taskType)) {
                    this.$store.dispatch("plugin/load", {cls: taskType}).then((plugin) => {
                        this.$store.commit("plugin/setEditorPlugin", plugin);
                    });
                } else {
                    this.$store.commit("plugin/setEditorPlugin", undefined);
                }
            }
        },
        data() {
            return {
                source: this.initialSource
            }
        },
        beforeUnmount() {
            this.$store.commit("plugin/setEditorPlugin", undefined);
        }
    };
</script>
