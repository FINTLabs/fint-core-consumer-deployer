package no.fintlabs.consumer.deployer.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient

@Configuration
class RestClientConfig(private val fintProperties: FintProperties) {

    @Bean
    fun restClient(): RestClient = RestClient.create(fintProperties.consumerUrl)

}