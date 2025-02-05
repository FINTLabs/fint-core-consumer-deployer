package no.fintlabs.consumer.deployer.config

import io.fabric8.kubernetes.client.KubernetesClient
import io.fabric8.kubernetes.client.dsl.MixedOperation
import io.fabric8.kubernetes.client.dsl.Resource
import io.fabric8.kubernetes.client.server.mock.KubernetesServer
import no.fintlabs.consumer.deployer.flais.model.Application
import no.fintlabs.consumer.deployer.flais.model.ApplicationList
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

@TestConfiguration
class TestKubernetesConfig {

    private val server = KubernetesServer(true, true)

    init {
        server.before()
    }

    @Bean
    @Primary
    fun kubernetesClient(): KubernetesClient = server.client

    @Bean
    @Primary
    fun applicationClient(kubernetesClient: KubernetesClient): MixedOperation<Application, ApplicationList, Resource<Application>> =
        kubernetesClient.resources(Application::class.java, ApplicationList::class.java)

}
