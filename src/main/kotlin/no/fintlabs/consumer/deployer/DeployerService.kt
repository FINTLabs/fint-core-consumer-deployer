package no.fintlabs.consumer.deployer

import com.sun.org.slf4j.internal.Logger
import com.sun.org.slf4j.internal.LoggerFactory
import no.fintlabs.consumer.deployer.model.Consumer
import no.fintlabs.consumer.deployer.model.ConsumerRequest
import no.fintlabs.consumer.deployer.model.Operation
import org.springframework.stereotype.Service

@Service
class DeployerService(
    private val kubectl: KubectlService
) {

    private val log: Logger = LoggerFactory.getLogger(DeployerService::class.java)

    fun handleEvent(consumerRequest: ConsumerRequest) {
        when (consumerRequest.operation) {
            Operation.CREATE -> handleCreate(consumerRequest.consumer)
            Operation.UPDATE -> handleUpdate(consumerRequest.consumer)
            Operation.DELETE -> handleDelete(consumerRequest.consumer)
        }
    }

    private fun handleCreate(consumer: Consumer) {
        kubectl.takeIf { it.deploymentDoesntExist(consumer) }
            ?.create(consumer)
            ?: log.error("Skipping creation... Deployment already exists: {}", consumer.id)
    }

    private fun handleUpdate(consumer: Consumer) {
        kubectl.takeIf { it.deploymentExists(consumer) }
            ?.update(consumer)
            ?: log.error("Skipping update... Deployment doesn't exist: {}", consumer.id)
    }

    private fun handleDelete(consumer: Consumer) {
        kubectl.takeIf { it.deploymentExists(consumer) }
            ?.delete(consumer)
            ?: log.error("Skipping deletion... Deployment doesn't exist: {} ", consumer.id)
    }

}
