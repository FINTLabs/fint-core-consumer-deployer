package no.fintlabs

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FintCoreConsumerDeployerApplication

fun main(args: Array<String>) {
	runApplication<FintCoreConsumerDeployerApplication>(*args)
}
