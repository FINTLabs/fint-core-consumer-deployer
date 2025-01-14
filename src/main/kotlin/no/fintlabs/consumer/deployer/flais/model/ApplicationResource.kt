package no.fintlabs.consumer.deployer.flais.model

data class ApplicationResource(
    val apiVersion: String = "fintlabs.no/v1alpha1",
    val kind: String = "Application",
    val metadata: Metadata,
    val spec: Spec
)

data class Metadata(
    val name: String,
    val namespace: String,
    val labels: Map<String, String> = emptyMap()
)

data class Spec(
    val port: Int = 8080,
    val orgId: String,
    val image: String,
    val env: List<EnvVar> = emptyList(),
    val kafka: Kafka,
    val ingress: Ingress,
    val resources: Resources,
)

data class EnvVar(val name: String, val value: String)
data class Acl(val topic: String, val permission: String = "admin")

data class Kafka(
    val enabled: Boolean,
    val acls: List<Acl>
)

data class Ingress(
    val routes: List<Route>,
)

data class Route(
    val host: String,
    val path: String?,
    val headers: Map<String, String>?,
)

data class Resources(
    val limits: Limits,
    val requests: Requests
)
data class Limits(val memory: String, val cpu: String)
data class Requests(val memory: String, val cpu: String)
