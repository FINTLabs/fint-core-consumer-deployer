package no.fintlabs.consumer.deployer

import io.fabric8.kubernetes.api.model.StatusDetails
import io.fabric8.kubernetes.client.dsl.MixedOperation
import io.fabric8.kubernetes.client.dsl.Resource
import no.fintlabs.consumer.deployer.config.FintProperties
import no.fintlabs.consumer.deployer.flais.model.Application
import no.fintlabs.consumer.deployer.flais.model.ApplicationList
import no.fintlabs.consumer.state.interfaces.Consumer
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class KubectlService(
    private val applicationClient: MixedOperation<Application, ApplicationList, Resource<Application>>,
    private val fintProperties: FintProperties
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun deploymentExists(consumer: Consumer): Boolean =
        applicationClient
            .inNamespace(formatNameSpace(consumer.org))
            .withName(createDeploymentName(consumer.domain, consumer.`package`))
            .get() != null

    fun deploymentDoesntExist(consumer: Consumer) = !deploymentExists(consumer)

    fun create(consumer: Consumer): Application =
        applicationClient
            .inNamespace(formatNameSpace(consumer.org))
            .resource(Application.fromConsumer(consumer, fintProperties.env))
            .create()

    fun update(consumer: Consumer) = TODO("Edit already existing Application")

    fun delete(consumer: Consumer): MutableList<StatusDetails> {
        val statusDetails = applicationClient
            .inNamespace(formatNameSpace(consumer.org))
            .withName(createDeploymentName(consumer.domain, consumer.`package`))
            .delete()

        statusDetails.forEach { status ->
            logger.info("Deleted resource: ${status.name} of kind: ${status.kind} in namespace: ${consumer.org}")
        }

        return statusDetails
    }

    fun createDeploymentName(domain: String, `package`: String) = "fint-core-consumer-$domain-$`package`"
    fun formatNameSpace(org: String) = org.replace(".", "-")

}