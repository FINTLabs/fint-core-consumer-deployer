package no.fintlabs.consumer.deployer.config

import com.fasterxml.jackson.databind.ObjectMapper
import no.fintlabs.consumer.deployer.DeployerService
import no.fintlabs.consumer.state.model.ConsumerResponse
import no.fintlabs.webhook.client.WebhookClientService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class WebHookConfig(private val deployerService: DeployerService) {

    @Bean
    fun webHookClientService(objectMapper: ObjectMapper) =
        WebhookClientService(ConsumerResponse::class.java, deployerService::handleEvent, objectMapper)

}