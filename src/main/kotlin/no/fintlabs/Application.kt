package no.fintlabs

import no.fintlabs.webhook.client.annotation.WebHookClient
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@WebHookClient
@ConfigurationPropertiesScan
@SpringBootApplication
class FintCoreConsumerDeployerApplication

fun main(args: Array<String>) {
	runApplication<FintCoreConsumerDeployerApplication>(*args)
}
