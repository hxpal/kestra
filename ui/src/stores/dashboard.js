import {apiUrl} from "override/utils/route";

export default {
    namespaced: true,
    actions: {
        list(_, options) {
            const sortString = options.sort ? `?sort=${options.sort}` : ""
            delete options.sort
            return this.$http.get(`${apiUrl(this)}/dashboards/search${sortString}`, {
                params: options
            }).then(response => response.data);
        },
        create(_, source) {
            return this.$http.post(`${apiUrl(this)}/dashboards`, {
                body: source
            });
        }
    }
}
