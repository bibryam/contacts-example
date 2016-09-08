package com.inmarsat.demo.buildconfig.prod;

import java.util.HashMap;
import java.util.Map;

import com.inmarsat.demo.buildconfig.dev.ServiceKubernetesModelProcessor;

import io.fabric8.kubernetes.api.model.KubernetesList;
import io.fabric8.kubernetes.api.model.KubernetesListBuilder;
import io.fabric8.kubernetes.generator.annotation.KubernetesProvider;


public class MainKubernetesModelProcessor {

	@KubernetesProvider("prod.yml")
	public KubernetesList withKubernetesListBuilder() {
		
		KubernetesListBuilder builder = new KubernetesListBuilder();
		
		builder.addNewTemplateItem()
					.addNewParameter()
					.withDisplayName("Image Name")
					.withName("IMAGE_NAME")
					.withValue("contacts-example")
				.endParameter()
					.addNewParameter()
					.withDisplayName("Image Tag")
					.withName("IMAGE_TAG")
					.withValue("stage")
				.endParameter()
					.addNewParameter()
					.withDisplayName("Source Namespace")
					.withName("SOURCE_NAMESPACE")
					.withValue("staging")
				.endParameter()
					.withNewMetadata()
					.withName("contacts-example-prod")
					.withAnnotations(getAnnotations())
					.endMetadata()
				.endTemplateItem();
		
		new DeploymentConfigKubernetesModelProcessor().on(builder);
		new ServiceKubernetesModelProcessor().on(builder);
		
		return builder.build();
	}
	
	private Map<String, String> getAnnotations()
	{
		Map<String, String> annotations = new HashMap<>();
		annotations.put("description", "Example project demonstrating a Camel route with build pipeline integration");
		annotations.put("iconClass", "icon-jboss");
		return annotations;
	}
}
