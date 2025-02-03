package no.fintlabs.consumer.deployer.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "fint")
data class FintProperties(
    val env: String
)
