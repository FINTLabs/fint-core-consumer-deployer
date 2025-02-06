package no.fintlabs.consumer.deployer.flais.model

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import io.fabric8.generator.annotation.Min
import io.fabric8.generator.annotation.Required
import io.fabric8.kubernetes.api.model.IntOrString
import io.fabric8.kubernetes.api.model.KubernetesResource
import no.fintlabs.consumer.state.interfaces.Consumer
import no.fintlabs.consumer.state.model.PodResources
import no.fintlabs.v1alpha1.applicationspec.*
import no.fintlabs.v1alpha1.applicationspec.ingress.Routes
import no.fintlabs.v1alpha1.applicationspec.kafka.Acls
import javax.annotation.processing.Generated

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
    "database",
    "env",
    "envFrom",
    "image",
    "imagePullPolicy",
    "imagePullSecrets",
    "ingress",
    "kafka",
    "observability",
    "onePassword",
    "orgId",
    "port",
    "prometheus",
    "replicas",
    "resources",
    "restartPolicy",
    "strategy",
    "url"
)
@JsonDeserialize(using = JsonDeserializer.None::class)
@Generated("io.fabric8.java.generator.CRGeneratorRunner")
class ApplicationSpec : KubernetesResource {

    @JsonProperty("database")
    @JsonSetter(nulls = Nulls.SKIP)
    var database: Database? = null

    @JsonProperty("env")
    @JsonSetter(nulls = Nulls.SKIP)
    var env: List<Env>? = null

    @JsonProperty("envFrom")
    @JsonSetter(nulls = Nulls.SKIP)
    var envFrom: List<EnvFrom>? = null

    @JsonProperty("image")
    @Required
    @JsonSetter(nulls = Nulls.SKIP)
    var image: String? = null

    @JsonProperty("imagePullPolicy")
    @JsonSetter(nulls = Nulls.SKIP)
    var imagePullPolicy: String? = null

    @JsonProperty("imagePullSecrets")
    @JsonSetter(nulls = Nulls.SKIP)
    var imagePullSecrets: List<String>? = null

    @JsonProperty("ingress")
    @JsonSetter(nulls = Nulls.SKIP)
    var ingress: Ingress? = null

    @JsonProperty("kafka")
    @JsonSetter(nulls = Nulls.SKIP)
    var kafka: Kafka? = null

    @JsonProperty("observability")
    @JsonSetter(nulls = Nulls.SKIP)
    var observability: Observability? = null

    @JsonProperty("onePassword")
    @JsonSetter(nulls = Nulls.SKIP)
    var onePassword: OnePassword? = null

    @JsonProperty("orgId")
    @Required
    @JsonSetter(nulls = Nulls.SKIP)
    var orgId: String? = null

    @JsonProperty("port")
    @Min(1.0)
    @JsonSetter(nulls = Nulls.SKIP)
    var port: Long? = null

    @JsonProperty("prometheus")
    @JsonSetter(nulls = Nulls.SKIP)
    var prometheus: Prometheus? = null

    @JsonProperty("replicas")
    @Min(0.0)
    @JsonSetter(nulls = Nulls.SKIP)
    var replicas: Long? = null

    @JsonProperty("resources")
    @JsonSetter(nulls = Nulls.SKIP)
    var resources: Resources? = null

    @JsonProperty("restartPolicy")
    @JsonSetter(nulls = Nulls.SKIP)
    var restartPolicy: String? = null

    @JsonProperty("strategy")
    @JsonSetter(nulls = Nulls.SKIP)
    var strategy: Strategy? = null

    @JsonProperty("url")
    @JsonSetter(nulls = Nulls.SKIP)
    var url: Url? = null

    companion object {

        fun kafkaFromOrg(org: String) = Kafka().apply {
            enabled = true
            acls = listOf(
                Acls().apply {
                    topic = "${org.replace(".", "-")}.fint-core.*"
                    permission = "admin"
                }
            )
        }

        fun envFromConsumer(consumer: Consumer, env: String): List<Env> =
            listOf(
                Env().apply {
                    name = "JAVA_TOOL_OPTIONS"
                    value = "-XX:+ExitOnOutOfMemoryError -Xmx${calculateXmx(consumer.podResources.limits.memory)}"
                },
                Env().apply {
                    name = "fint.relation.base-url"
                    value = "https://$env.felleskomponent.no"
                },
                Env().apply {
                    name = "fint.consumer.domain"
                    value = consumer.domain
                },
                Env().apply {
                    name = "fint.consumer.package"
                    value = consumer.`package`
                },
                Env().apply {
                    name = "fint.consumer.org-id"
                    value = consumer.org
                },
                Env().apply {
                    name = "fint.consumer.writeable"
                    value = consumer.writeableResources.joinToString(",")
                },
                Env().apply {
                    name = "fint.consumer.cache.disabled"
                    value = consumer.cacheDisabledResources.joinToString(",")
                }
            )

        private fun calculateXmx(memoryLimit: String): String {
            val totalMi = parseMemoryMi(memoryLimit)

            val xmxMi = if (totalMi <= 1536) {
                (totalMi - 300).coerceAtLeast(0)
            } else {
                (totalMi * 0.8).toLong()
            }

            return "-Xmx${xmxMi}M"
        }

        private fun parseMemoryMi(memoryLimit: String): Long {
            return when {
                memoryLimit.endsWith("Mi") -> {
                    memoryLimit.removeSuffix("Mi").toLong()
                }

                memoryLimit.endsWith("Gi") -> {
                    val gigabytes = memoryLimit.removeSuffix("Gi").toLong()
                    gigabytes * 1024
                }

                else -> throw IllegalArgumentException("Unknown memory unit: $memoryLimit")
            }
        }


        fun ingressFromConsumer(consumer: Consumer, env: String): Ingress {
            val ingressPath = if (consumer.resources.isNotEmpty()) {
                val joinedResources = consumer.resources.joinToString("|")
                "/${consumer.domain}/${consumer.`package`}/{resource:(admin|actuator|$joinedResources)}"
            } else {
                "/${consumer.domain}/${consumer.`package`}"
            }

            return Ingress().apply {
                routes = listOf(
                    Routes().apply {
                        host = "$env.felleskomponent.no"
                        path = ingressPath
                        if (consumer.shared.not()) {
                            headers = mapOf("x-org-id" to consumer.org)
                        }
                    }
                )
            }
        }

        fun resourcesFromPodResources(podResources: PodResources) =
            Resources().apply {
                requests = mapOf(
                    "memory" to IntOrString(podResources.requests.memory),
                    "cpu" to IntOrString(podResources.requests.cpu)
                )
                limits = mapOf(
                    "memory" to IntOrString(podResources.limits.memory),
                    "cpu" to IntOrString(podResources.limits.cpu)
                )
            }

        fun prometheusFromComponent(domain: String, pkg: String) =
            Prometheus().apply {
                enabled = true
                path = "/$domain/$pkg/actuator/prometheus"
                port = "8080"
            }

    }

}

