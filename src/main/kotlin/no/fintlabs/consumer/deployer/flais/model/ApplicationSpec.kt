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

}

