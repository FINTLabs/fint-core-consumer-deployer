package no.fintlabs.consumer.deployer

import io.fabric8.kubernetes.api.model.NamespaceBuilder
import io.fabric8.kubernetes.api.model.StatusDetails
import io.fabric8.kubernetes.client.KubernetesClient
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
    private val kubernetesClient: KubernetesClient,
    private val fintProperties: FintProperties
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun create(consumer: Consumer): Application {
        val namespace = formatNameSpace(consumer.org)
        val application = applicationClient
            .inNamespace(namespace)
            .resource(Application.fromConsumer(consumer, fintProperties.env))
            .create()

        logger.info("Successfully created application: ${application.metadata.name} in namespace: $namespace")

        return application
    }

    fun update(consumer: Consumer): Application? {
        val namespace = formatNameSpace(consumer.org)
        val deploymentName = createDeploymentName(consumer.domain, consumer.`package`)
        val existingApplication = getExistingApplication(namespace, deploymentName)

        val updatedApplication = existingApplication.apply {
            spec = Application.fromConsumer(consumer, fintProperties.env).spec
        }

        val appliedApplication = applicationClient
            .inNamespace(namespace)
            .resource(updatedApplication)
            .update()

        logger.info("Successfully updated application: ${appliedApplication.metadata.name} in namespace: $namespace")

        return appliedApplication
    }

    fun delete(consumer: Consumer): MutableList<StatusDetails> {
        val namespace = formatNameSpace(consumer.org)
        val statusDetails = applicationClient
            .inNamespace(namespace)
            .withName(createDeploymentName(consumer.domain, consumer.`package`))
            .delete()

        statusDetails.forEach { status ->
            logger.info("Successfully deleted application: ${status.name} in namespace: $namespace")
        }

        return statusDetails
    }

    fun deploymentExists(consumer: Consumer): Boolean {
        val namespace = formatNameSpace(consumer.org)

        ensureNamespaceExists(namespace)

        return applicationClient
            .inNamespace(namespace)
            .withName(createDeploymentName(consumer.domain, consumer.`package`))
            .get() != null
    }

    fun deploymentDoesntExist(consumer: Consumer) = !deploymentExists(consumer)

    fun ensureNamespaceExists(namespace: String) {
        val existingNamespace = kubernetesClient.namespaces().withName(namespace).get()

        if (existingNamespace == null) {
            logger.info("Namespace $namespace does not exist. Creating...")

            val namespaceResource = NamespaceBuilder()
                .withNewMetadata()
                .withName(namespace)
                .endMetadata()
                .build()

            kubernetesClient.namespaces().resource(namespaceResource).create()
            logger.info("Namespace $namespace created successfully.")
        } else {
            logger.debug("Namespace $namespace already exists.")
        }
    }


    fun getExistingApplication(namespace: String, deploymentName: String): Application =
        applicationClient.inNamespace(namespace)
            .withName(deploymentName)
            .get()

    fun createDeploymentName(domain: String, `package`: String) = "fint-core-consumer-$domain-$`package`"
    fun formatNameSpace(org: String) = org.replace(".", "-")

}