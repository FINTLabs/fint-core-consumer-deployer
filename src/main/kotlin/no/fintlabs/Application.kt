package no.fintlabs

import no.fintlabs.webhook.client.annotation.WebhookClient
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@WebhookClient
@EnableScheduling
@ConfigurationPropertiesScan
@SpringBootApplication
class FintCoreConsumerDeployerApplication

fun main(args: Array<String>) {
    runApplication<FintCoreConsumerDeployerApplication>(*args)
}
