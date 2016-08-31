package com.inmarsat.demo.buildconfig;

import io.fabric8.kubernetes.generator.annotation.KubernetesModelProcessor;
import io.fabric8.openshift.api.model.TemplateBuilder;

@KubernetesModelProcessor
public class MainKubernetesModelProcessor {

	public void withTemplateBuilder(TemplateBuilder builder) {
		new DeploymentConfigKubernetesModelProcessor().on(builder);
		new ImageStreamKubernetesModelProcessor().on(builder);
		new BuildConfigKubernetesModelProcessor().on(builder);
		
		
	}
}
