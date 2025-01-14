package no.fintlabs.consumer.deployer.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "fint.consumer.state")
data class WebhookProperties(
    val callback: String,
    val webhookEndpoint: String
)
