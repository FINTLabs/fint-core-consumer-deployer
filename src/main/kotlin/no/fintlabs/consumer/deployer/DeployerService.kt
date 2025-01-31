package no.fintlabs.consumer.deployer

import no.fintlabs.consumer.state.interfaces.Consumer
import no.fintlabs.consumer.state.model.ConsumerResponse
import no.fintlabs.consumer.state.model.Operation
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class DeployerService(private val kubectl: KubectlService) {

    private val log: Logger = LoggerFactory.getLogger(DeployerService::class.java)

    fun handleEvent(consumerResponse: ConsumerResponse) {
        when (consumerResponse.operation) {
            Operation.CREATE -> handleCreate(consumerResponse.consumer)
            Operation.UPDATE -> handleUpdate(consumerResponse.consumer)
            Operation.DELETE -> handleDelete(consumerResponse.consumer)
        }
    }

    private fun handleCreate(consumer: Consumer) {
        kubectl.takeIf { it.deploymentDoesntExist(consumer) }
            ?.create(consumer)
            ?: log.error("Skipping creation... Deployment already exists: {}", createConsumerId(consumer))
    }

    private fun handleUpdate(consumer: Consumer) {
        kubectl.takeIf { it.deploymentExists(consumer) }
            ?.update(consumer)
            ?: log.error("Skipping update... Deployment doesn't exist: {}", createConsumerId(consumer))
    }

    private fun handleDelete(consumer: Consumer) {
        kubectl.takeIf { it.deploymentExists(consumer) }
            ?.delete(consumer)
            ?: log.error("Skipping deletion... Deployment doesn't exist: {} ", createConsumerId(consumer))
    }

    private fun createConsumerId(consumer: Consumer) = "${consumer.domain}-${consumer.`package`}-${consumer.org}"

}
