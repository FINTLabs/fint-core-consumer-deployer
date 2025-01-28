package no.fintlabs.consumer.deployer.flais.model

import io.fabric8.kubernetes.api.model.DefaultKubernetesResourceList
import io.fabric8.kubernetes.api.model.Namespaced
import io.fabric8.kubernetes.model.annotation.Group
import io.fabric8.kubernetes.model.annotation.Version
import javax.annotation.processing.Generated

@Version("v1alpha1")
@Group("fintlabs.no")
@Generated("io.fabric8.java.generator.CRGeneratorRunner")
class ApplicationList : DefaultKubernetesResourceList<Application>(), Namespaced
