package no.fintlabs.consumer.deployer

import io.fabric8.kubernetes.client.KubernetesClient
import no.fintlabs.consumer.deployer.config.TestKubernetesConfig
import no.fintlabs.consumer.state.interfaces.Consumer
import no.fintlabs.consumer.state.model.ConsumerRequest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@Import(TestKubernetesConfig::class)
@ActiveProfiles("test")
class KubectlServiceTest {

    @Autowired
    lateinit var kubectlService: KubectlService

    @Autowired
    lateinit var kubernetesClient: KubernetesClient

    private val testOrg = "fintlabs.no"
    private val testConsumer = ConsumerRequest("utdanning", "vurdering", "3.19.3", testOrg)

    fun expectedDeploymentName(consumer: Consumer): String =
        "fint-core-consumer-${consumer.domain}-${consumer.`package`}"

    @BeforeEach
    fun setup() {
        try {
            kubernetesClient.namespaces().withName(testOrg).delete()
        } catch (_: Exception) {
        }
    }

    @Test
    fun `test get existing Application returns null`() =
        assertNull(kubectlService.getApplication(testConsumer))

    @Test
    fun `test get existing Application`() {
        val createdApplication = kubectlService.createApplication(testConsumer)
        val existingApplication = kubectlService.getApplication(testConsumer)

        assertEquals(createdApplication.metadata.name, existingApplication!!.metadata.name)
    }

    @Test
    fun `test ensureNamespaceExists creates a namespace if missing`() {
        val namespace = kubectlService.formatNamespace(testOrg)
        val nsBefore = kubernetesClient.namespaces().withName(namespace).get()
        assertNull(nsBefore)

        kubectlService.ensureNamespaceExists(testOrg)

        val nsAfter = kubernetesClient.namespaces().withName(namespace).get()
        assertNotNull(nsAfter)
    }

    @Test
    fun `test format deployment name`() =
        assertEquals(
            "fint-core-consumer-${testConsumer.domain}-${testConsumer.`package`}",
            kubectlService.formatDeploymentName(testConsumer.domain, testConsumer.`package`)
        )

    @Test
    fun `test format namespace`() =
        assertEquals(
            testOrg.replace(".", "-"),
            kubectlService.formatNamespace(testOrg)
        )


}
