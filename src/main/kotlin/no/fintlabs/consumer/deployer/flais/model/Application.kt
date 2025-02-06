package no.fintlabs.consumer.deployer.flais.model

import io.fabric8.kubernetes.api.model.Namespaced
import io.fabric8.kubernetes.api.model.ObjectMeta
import io.fabric8.kubernetes.client.CustomResource
import io.fabric8.kubernetes.model.annotation.Group
import io.fabric8.kubernetes.model.annotation.Plural
import io.fabric8.kubernetes.model.annotation.Singular
import io.fabric8.kubernetes.model.annotation.Version
import no.fintlabs.consumer.state.interfaces.Consumer
import javax.annotation.processing.Generated

@Version(value = "v1alpha1", storage = true, served = true)
@Group("fintlabs.no")
@Singular("application")
@Plural("applications")
@Generated("io.fabric8.java.generator.CRGeneratorRunner")
class Application : CustomResource<ApplicationSpec, ApplicationStatus>(), Namespaced {

    companion object {
        fun fromConsumer(consumer: Consumer, clusterEnv: String): Application {
            val org = consumer.org
            val appName = "fint-core-consumer-${consumer.domain}-${consumer.`package`}"

            return Application().apply {
                metadata = ObjectMeta().apply {
                    name = appName
                    namespace = org.replace(".", "-")
                    labels = mapOf(
                        "app.kubernetes.io/name"      to appName,
                        "app.kubernetes.io/instance"  to "${appName}_${org.replace(".", "_")}",
                        "app.kubernetes.io/version"   to "latest",
                        "app.kubernetes.io/component" to "consumer",
                        "app.kubernetes.io/part-of"   to "fint-felleskomponent",
                        "fintlabs.no/team"            to "core",
                        "fintlabs.no/org-id"          to org
                    )
                }

                spec = ApplicationSpec().apply {
                    port = 8080
                    orgId = org
                    image = "ghcr.io/fintlabs/fint-core-consumer:v${consumer.version}"
                    env = ApplicationSpec.envFromConsumer(consumer, clusterEnv)
                    kafka = ApplicationSpec.kafkaFromOrg(consumer.org)
                    ingress = ApplicationSpec.ingressFromConsumer(consumer, clusterEnv)
                    resources = ApplicationSpec.resourcesFromPodResources(consumer.podResources)
                    prometheus = ApplicationSpec.prometheusFromComponent(consumer.domain, consumer.`package`)
                }
            }
        }
    }
}
