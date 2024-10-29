CREATE TABLE IF NOT EXISTS dashboards (
                                     "key" VARCHAR(250) NOT NULL PRIMARY KEY,
    "value" TEXT NOT NULL,
    "deleted" BOOL NOT NULL GENERATED ALWAYS AS (JQ_BOOLEAN("value", '.deleted')),
    "tenant_id" VARCHAR(250) NOT NULL GENERATED ALWAYS AS (JQ_STRING("value", '.tenantId')),
    "id" VARCHAR(100) NOT NULL GENERATED ALWAYS AS (JQ_STRING("value", '.id')),
    "title" VARCHAR(250) NOT NULL GENERATED ALWAYS AS (JQ_STRING("value", '.title')),
    "description" TEXT GENERATED ALWAYS AS (JQ_STRING("value", '.description')),
    "fulltext" TEXT NOT NULL GENERATED ALWAYS AS (
        JQ_STRING("value", '.title') || JQ_STRING("value", '.description')
    ),
    "source_code" TEXT NOT NULL,
    "created" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

CREATE INDEX IF NOT EXISTS dashboards_tenant ON dashboards ("deleted", "tenant_id");
CREATE INDEX IF NOT EXISTS dashboards_title ON dashboards ("title");
CREATE INDEX IF NOT EXISTS dashboards_description ON dashboards ("description");
CREATE INDEX IF NOT EXISTS dashboards_created ON dashboards ("created");
CREATE INDEX IF NOT EXISTS dashboards_updated ON dashboards ("updated");