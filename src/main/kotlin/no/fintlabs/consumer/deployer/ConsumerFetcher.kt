package no.fintlabs.consumer.deployer

import no.fintlabs.consumer.deployer.model.Consumer
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class ConsumerFetcher(private val restClient: RestClient) {

    fun getExistingState(): List<Consumer>? =
        restClient.get()
            .retrieve()
            .body(object : ParameterizedTypeReference<List<Consumer>>() {})

}