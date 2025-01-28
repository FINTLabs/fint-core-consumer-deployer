package no.fintlabs.consumer.deployer.flais.model

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import io.fabric8.kubernetes.api.model.KubernetesResource
import javax.annotation.processing.Generated

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("correlationId", "dependentErrors", "observedGeneration", "state")
@JsonDeserialize(using = JsonDeserializer.None::class)
@Generated("io.fabric8.java.generator.CRGeneratorRunner")
class ApplicationStatus : KubernetesResource {
    @JsonProperty("correlationId")
    @JsonSetter(nulls = Nulls.SKIP)
    var correlationId: String? = null

    @JsonProperty("dependentErrors")
    @JsonSetter(nulls = Nulls.SKIP)
    var dependentErrors: Map<String, String>? = null

    @JsonProperty("observedGeneration")
    @JsonSetter(nulls = Nulls.SKIP)
    var observedGeneration: Long? = null

    enum class State(@get:JsonValue var value: String) {
        @JsonProperty("DEPLOYED")
        DEPLOYED("DEPLOYED"),

        @JsonProperty("FAILED")
        FAILED("FAILED"),

        @JsonProperty("PENDING")
        PENDING("PENDING")
    }

    @JsonProperty("state")
    @JsonSetter(nulls = Nulls.SKIP)
    var state: State? = null
}

