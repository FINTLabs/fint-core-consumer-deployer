package no.fintlabs.consumer.deployer.config

import io.fabric8.kubernetes.client.KubernetesClient
import io.fabric8.kubernetes.client.KubernetesClientBuilder
import io.fabric8.kubernetes.client.dsl.MixedOperation
import io.fabric8.kubernetes.client.dsl.Resource
import no.fintlabs.consumer.deployer.flais.model.Application
import no.fintlabs.consumer.deployer.flais.model.ApplicationList
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("!test")
class KubernetesConfig {

    @Bean
    fun kubernetesClient(): KubernetesClient = KubernetesClientBuilder().build()

    @Bean
    fun applicationClient(kubernetesClient: KubernetesClient): MixedOperation<Application, ApplicationList, Resource<Application>> =
        kubernetesClient.resources(Application::class.java, ApplicationList::class.java)

}