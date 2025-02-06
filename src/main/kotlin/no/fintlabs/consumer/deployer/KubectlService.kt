package no.fintlabs.consumer.deployer

import io.fabric8.kubernetes.api.model.Namespace
import io.fabric8.kubernetes.api.model.NamespaceBuilder
import io.fabric8.kubernetes.api.model.StatusDetails
import io.fabric8.kubernetes.client.KubernetesClient
import io.fabric8.kubernetes.client.dsl.MixedOperation
import io.fabric8.kubernetes.client.dsl.Resource
import no.fintlabs.consumer.deployer.config.FintProperties
import no.fintlabs.consumer.deployer.flais.model.Application
import no.fintlabs.consumer.deployer.flais.model.ApplicationList
import no.fintlabs.consumer.state.interfaces.Consumer
import no.fintlabs.consumer.state.interfaces.ConsumerIdentificator
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class KubectlService(
    private val applicationClient: MixedOperation<Application, ApplicationList, Resource<Application>>,
    private val kubernetesClient: KubernetesClient,
    private val fintProperties: FintProperties
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun createApplication(consumer: Consumer): Application =
        formatNamespace(consumer.org).let { namespace ->
            ensureNamespace(namespace)
            applicationClient
                .inNamespace(namespace)
                .resource(Application.fromConsumer(consumer, fintProperties.env))
                .create()
                .also { logger.info("Application created: ${it.metadata.name} in ${it.metadata.namespace}") }
        }

    fun updateApplication(consumer: Consumer): Application =
        getApplication(consumer)
            .also { it!!.spec = Application.fromConsumer(consumer, fintProperties.env).spec }
            .let { applicationClient.inNamespace(formatNamespace(consumer.org)).resource(it).update() }
            .also { logger.info("Application updated: ${it.metadata.name} in ${it.metadata.namespace}") }

    fun delete(consumer: Consumer): MutableList<StatusDetails> =
        applicationClient
            .inNamespace(formatNamespace(consumer.org))
            .withName(formatDeploymentName(consumer.domain, consumer.`package`))
            .delete()
            .onEach { logger.info("Application deleted: ${it.name} in ${formatNamespace(consumer.org)}") }

    fun getApplication(consumer: ConsumerIdentificator): Application? =
        applicationClient
            .inNamespace(formatNamespace(consumer.org))
            .withName(formatDeploymentName(consumer.domain, consumer.`package`))
            .get()

    fun applicationExists(consumer: Consumer): Boolean = getApplication(consumer) != null

    fun ensureNamespace(org: String): Namespace =
        formatNamespace(org).let { namespace ->
            kubernetesClient.namespaces().withName(namespace).get()?.also {
                logger.debug("Namespace $namespace already exists.")
            } ?: run {
                kubernetesClient.namespaces()
                    .resource(createNamespaceResource(namespace))
                    .create()
                    .also { logger.info("Namespace $namespace created") }
            }
        }

    private fun createNamespaceResource(namespace: String): Namespace =
        NamespaceBuilder()
            .withNewMetadata()
            .withName(namespace)
            .endMetadata()
            .build()

    fun formatDeploymentName(domain: String, `package`: String) = "fint-core-consumer-$domain-$`package`"
    fun formatNamespace(org: String) = org.replace(".", "-")

}