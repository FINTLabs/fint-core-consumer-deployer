package no.fintlabs.consumer.deployer

import no.fintlabs.consumer.deployer.flais.model.ApplicationSpec
import no.fintlabs.consumer.state.model.ConsumerRequest
import no.fintlabs.consumer.state.model.Limits
import no.fintlabs.consumer.state.model.PodResources
import no.fintlabs.consumer.state.model.Requests
import org.junit.jupiter.api.Assertions.assertEquals
import java.util.UUID
import kotlin.random.Random

class TestTools {
    companion object {

        fun randomConsumerRequest(): ConsumerRequest {
            fun randomString() = UUID.randomUUID().toString()
            fun randomBoolean() = Random.nextBoolean()

            return ConsumerRequest(
                domain = "domain-${randomString()}",
                `package` = "package-${randomString()}",
                version = "version-${randomString()}",
                org = "org-${randomString()}",
                shared = randomBoolean(),
                podResources = PodResources(),
                resources = listOf("res1-${randomString()}", "res2-${randomString()}"),
                writeableResources = listOf("wres1-${randomString()}"),
                cacheDisabledResources = listOf("cdres1-${randomString()}")
            )
        }

        fun verifySpec(expected: ApplicationSpec, actual: ApplicationSpec) {
            assertEquals(expected.image, actual.image, "image mismatch")
            assertEquals(expected.imagePullPolicy, actual.imagePullPolicy, "imagePullPolicy mismatch")
            assertEquals(expected.orgId, actual.orgId, "orgId mismatch")
            assertEquals(expected.port, actual.port, "port mismatch")
            assertEquals(expected.replicas, actual.replicas, "replicas mismatch")
            assertEquals(expected.restartPolicy, actual.restartPolicy, "restartPolicy mismatch")

            assertEquals(expected.imagePullSecrets, actual.imagePullSecrets, "imagePullSecrets mismatch")

            assertEquals(expected.database?.database, actual.database?.database, "database.database mismatch")
            assertEquals(expected.database?.enabled, actual.database?.enabled, "database.enabled mismatch")

            assertEquals(expected.env?.size, actual.env?.size, "env.size mismatch")
            expected.env?.forEachIndexed { i, e ->
                val a = actual.env?.get(i)
                assertEquals(e.name,  a?.name,  "env[$i].name mismatch")
                assertEquals(e.value, a?.value, "env[$i].value mismatch")
            }

            assertEquals(expected.envFrom?.size, actual.envFrom?.size, "envFrom.size mismatch")
            expected.envFrom?.forEachIndexed { i, e ->
                val a = actual.envFrom?.get(i)
                assertEquals(e.prefix,          a?.prefix, "envFrom[$i].prefix mismatch")
            }

            assertEquals(expected.ingress?.enabled,   actual.ingress?.enabled,   "ingress.enabled mismatch")
            assertEquals(expected.ingress?.basePath,  actual.ingress?.basePath,  "ingress.basePath mismatch")
            assertEquals(expected.ingress?.middlewares, actual.ingress?.middlewares, "ingress.middlewares mismatch")
            assertEquals(expected.ingress?.routes?.size, actual.ingress?.routes?.size, "routes.size mismatch")

            expected.ingress?.routes?.forEachIndexed { i, r ->
                val ar = actual.ingress?.routes?.get(i)
                assertEquals(r.host,       ar?.host,       "routes[$i].host mismatch")
                assertEquals(r.path,       ar?.path,       "routes[$i].path mismatch")
                assertEquals(r.headers,    ar?.headers,    "routes[$i].headers mismatch")
                assertEquals(r.queries,    ar?.queries,    "routes[$i].queries mismatch")
                assertEquals(r.middlewares,ar?.middlewares,"routes[$i].middlewares mismatch")
            }

            assertEquals(expected.kafka?.enabled, actual.kafka?.enabled, "kafka.enabled mismatch")

            assertEquals(expected.observability?.logging, actual.observability?.logging, "observability.logging mismatch")
            assertEquals(expected.observability?.metrics, actual.observability?.metrics, "observability.metrics mismatch")

            assertEquals(expected.onePassword?.itemPath, actual.onePassword?.itemPath, "onePassword.itemPath mismatch")

            assertEquals(expected.prometheus?.enabled, actual.prometheus?.enabled, "prometheus.enabled mismatch")
            assertEquals(expected.prometheus?.path,    actual.prometheus?.path,    "prometheus.path mismatch")
            assertEquals(expected.prometheus?.port,    actual.prometheus?.port,    "prometheus.port mismatch")

            assertEquals(expected.resources?.limits,   actual.resources?.limits,   "resources.limits mismatch")
            assertEquals(expected.resources?.requests, actual.resources?.requests, "resources.requests mismatch")

            assertEquals(expected.strategy?.type,           actual.strategy?.type,           "strategy.type mismatch")
            assertEquals(expected.strategy?.rollingUpdate,  actual.strategy?.rollingUpdate,  "strategy.rollingUpdate mismatch")

            assertEquals(expected.url?.basePath, actual.url?.basePath, "url.basePath mismatch")
            assertEquals(expected.url?.hostname, actual.url?.hostname, "url.hostname mismatch")
        }


    }
}
