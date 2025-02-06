package no.fintlabs.consumer.deployer

import io.fabric8.kubernetes.client.KubernetesClient
import io.fabric8.kubernetes.client.dsl.MixedOperation
import io.fabric8.kubernetes.client.dsl.Resource
import no.fintlabs.consumer.deployer.config.TestKubernetesConfig
import no.fintlabs.consumer.deployer.flais.model.Application
import no.fintlabs.consumer.deployer.flais.model.ApplicationList
import no.fintlabs.consumer.state.model.ConsumerRequest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import kotlin.random.Random

@SpringBootTest
@Import(TestKubernetesConfig::class)
@ActiveProfiles("test")
class KubectlServiceTest {

    @Autowired
    lateinit var kubectlService: KubectlService

    @Autowired
    lateinit var kubernetesClient: KubernetesClient

    @Autowired
    lateinit var applicationClient: MixedOperation<Application, ApplicationList, Resource<Application>>

    private val testOrg = "fintlabs.no"
    private val testConsumer = ConsumerRequest("utdanning", "vurdering", "3.19.3", testOrg)
    private val logger = LoggerFactory.getLogger(this::class.java)
    private var generatedCount: Int = 0

    @BeforeEach
    fun setup() {
        generatedCount = Random.nextInt(10, 100)
    }

    @AfterEach
    fun teardown() {
        applicationClient
            .inAnyNamespace()
            .list()
            .items
            .forEach { app ->
                applicationClient
                    .inNamespace(app.metadata.namespace)
                    .withName(app.metadata.name)
                    .delete()
            }

        kubernetesClient.namespaces()
            .list()
            .items
            .forEach { ns ->
                kubernetesClient.namespaces()
                    .withName(ns.metadata.name)
                    .delete()
            }
    }

    @Test
    fun `test ensure namespace`() {
        val originalNamespaceCount = namespaceCount()

        kubectlService.ensureNamespace(testOrg)

        val expectedNamespace = kubectlService.formatNamespace(testOrg)

        assertTrue(
            namespaceCount() >= originalNamespaceCount + 1,
            "Expected at least one new namespace, but none appeared"
        )

        val namespaceResource = kubernetesClient.namespaces().withName(expectedNamespace).get()
        assertNotNull(namespaceResource, "Namespace $expectedNamespace was not found after ensureNamespace()")
    }

    @Test
    fun `test delete Application verbose`() =
        triggerVerboseTesting({ `test delete Application`() }, 0)

    @Test
    fun `test delete Application`() {
        val createApplication = kubectlService.createApplication(testConsumer)
        val delete = kubectlService.delete(testConsumer)
        assertEquals(createApplication.metadata.name, delete.first().name)
        assertTrue(delete.size == 1)
    }

    @Test
    fun `test update Application verbose`() =
        triggerVerboseTesting({ `test update Application`() }, 1)

    @Test
    fun `test update Application`() {
        kubectlService.createApplication(testConsumer)
        val existingApplication = kubectlService.getApplication(testConsumer)

        kubectlService.updateApplication(testConsumer.copy(version = "3.20.5"))

        val updatedApplication = kubectlService.getApplication(testConsumer)

        assertNotEquals(existingApplication!!.spec.image, updatedApplication!!.spec.image)
    }

    @Test
    fun `test create Application verbose`() =
        triggerVerboseTesting({ `test create Application`() }, 1)

    @Test
    fun `test create Application`() {
        val createApplication = kubectlService.createApplication(testConsumer)
        assertNotNull(createApplication)
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

    fun applications(): ApplicationList = applicationClient.inAnyNamespace().list()

    fun applicationCount(): Int = applicationClient.inAnyNamespace().list().items.size

    fun namespaceCount(): Int = kubernetesClient.namespaces().list().items.size

    fun triggerVerboseTesting(action: () -> Unit, unitApplicationCount: Int) {
        val generateRandomApplications = generateRandomApplications(generatedCount)

        action()

        assertEquals(generatedCount + unitApplicationCount, applicationCount())
        verifyApplications(generateRandomApplications)
    }

    fun generateRandomApplications(amount: Int): List<Application> {
        val createdApplications: MutableList<Application> = mutableListOf()
        for (i in 1..amount) {
            val application = kubectlService.createApplication(TestTools.randomConsumerRequest())
            createdApplications.add(application)
        }
        return createdApplications
    }

    fun verifyApplications(createdApplications: List<Application>) {
        val clusterApplications = applications().items

        logger.info("Verifying ${createdApplications.size} applications")
        createdApplications.forEach { expected ->
            val found = clusterApplications.find { actual ->
                actual.metadata.name == expected.metadata.name &&
                        actual.metadata.namespace == expected.metadata.namespace
            }

            assertNotNull(found, "Could not find Application '${expected.metadata.name}' in the cluster.")

            found?.let { actual -> TestTools.verifySpec(expected.spec, actual.spec) }
        }
    }

}
