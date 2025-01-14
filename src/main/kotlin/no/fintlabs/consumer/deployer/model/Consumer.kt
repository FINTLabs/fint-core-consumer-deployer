package no.fintlabs.consumer.deployer.model

data class Consumer(
    val id: String,
    val domain: String,
    val `package`: String,
    val org: String,
    val version: String,
    val managed: Boolean,
    val resources: List<String>,
    val writeableResources: List<String>,
    val cacheDisabledResources: List<String>
)
