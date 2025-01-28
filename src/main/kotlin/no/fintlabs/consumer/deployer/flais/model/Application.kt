package no.fintlabs.consumer.deployer.flais.model

import io.fabric8.kubernetes.api.model.Namespaced
import io.fabric8.kubernetes.client.CustomResource
import io.fabric8.kubernetes.model.annotation.Group
import io.fabric8.kubernetes.model.annotation.Plural
import io.fabric8.kubernetes.model.annotation.Singular
import io.fabric8.kubernetes.model.annotation.Version
import javax.annotation.processing.Generated

@Version(value = "v1alpha1", storage = true, served = true)
@Group("fintlabs.no")
@Singular("application")
@Plural("applications")
@Generated("io.fabric8.java.generator.CRGeneratorRunner")
class Application : CustomResource<ApplicationSpec, ApplicationStatus>(), Namespaced
