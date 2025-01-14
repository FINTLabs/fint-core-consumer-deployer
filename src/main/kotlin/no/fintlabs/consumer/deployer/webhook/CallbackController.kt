package no.fintlabs.consumer.deployer.webhook

import no.fintlabs.consumer.deployer.DeployerService
import no.fintlabs.consumer.deployer.model.ConsumerRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/callback")
class CallbackController(
    private val deployerService: DeployerService
) {

    @PostMapping
    fun callback(consumerRequest: ConsumerRequest) = deployerService.handleEvent(consumerRequest)

}