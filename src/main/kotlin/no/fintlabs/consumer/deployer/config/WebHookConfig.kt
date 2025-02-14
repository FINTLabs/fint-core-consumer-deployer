package no.fintlabs.consumer.deployer.config

import no.fintlabs.consumer.deployer.DeployerService
import no.fintlabs.consumer.state.model.ConsumerResponse
import no.fintlabs.webhook.client.config.WebhookClientProperties
import no.fintlabs.webhook.client.handler.WebhookEventHandler
import no.fintlabs.webhook.client.handler.createWebhookEventHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class WebHookConfig(
    private val webhookClientProperties: WebhookClientProperties,
    private val deployerService: DeployerService
) {

    @Bean
    fun consumerEventHandler(): WebhookEventHandler<ConsumerResponse> =
        createWebhookEventHandler<ConsumerResponse>(webhookClientProperties, "consumer")
        { event -> deployerService.handleConsumer(event) }

}