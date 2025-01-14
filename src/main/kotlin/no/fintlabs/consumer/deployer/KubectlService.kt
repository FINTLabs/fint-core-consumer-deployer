package no.fintlabs.consumer.deployer

import no.fintlabs.consumer.deployer.model.Consumer
import org.springframework.stereotype.Service

@Service
class KubectlService {

    fun deploymentExists(consumer: Consumer): Boolean = TODO()

    fun deploymentDoesntExist(consumer: Consumer) = !deploymentExists(consumer)

    fun create(consumer: Consumer) = TODO()

    fun update(consumer: Consumer) = TODO()

    fun delete(consumer: Consumer) = TODO()

}