package com.inmarsat.demo.buildconfig.dev;

import java.util.HashMap;
import java.util.Map;

import io.fabric8.kubernetes.api.model.KubernetesList;
import io.fabric8.kubernetes.api.model.KubernetesListBuilder;
import io.fabric8.kubernetes.generator.annotation.KubernetesProvider;


public class MainKubernetesModelProcessor {

	@KubernetesProvider("dev.yml")
	public KubernetesList withKubernetesListBuilder() {
		
		KubernetesListBuilder builder = new KubernetesListBuilder();
		
		builder.addNewTemplateItem()
			.addNewParameter()
					.withDisplayName("Nexus URL")
					.withName("NEXUS_URL")
					.withValue("http://ec2-54-171-132-33.eu-west-1.compute.amazonaws.com:8081")
				.endParameter()
				.addNewParameter()
					.withDisplayName("Repository Name")
					.withName("REPOSITORY_NAME")
					.withValue("snapshots")
				.endParameter()
				.addNewParameter()
					.withDisplayName("Group ID")
					.withName("GROUP_ID")
					.withValue("com.inmarsat.demo")
				.endParameter()				
				.addNewParameter()
					.withDisplayName("Artifact ID")
					.withName("ARTIFACT_ID")
					.withValue("demo")
				.endParameter()	
				.addNewParameter()
					.withDisplayName("Artifact Version")
					.withName("ARTIFACT_VERSION")
					.withValue("LATEST")
				.endParameter()
				.addNewParameter()
					.withDisplayName("Classifier")
					.withName("CLASSIFIER")
					.withValue("app")
				.endParameter()
				.addNewParameter()
					.withDisplayName("Extension")
					.withName("EXTENSION")
					.withValue("zip")
				.endParameter()	
			.withNewMetadata()
				.withName("contacts-example-dev")
				.withAnnotations(getAnnotations())
			.endMetadata()
		.endTemplateItem();
		
		new DeploymentConfigKubernetesModelProcessor().on(builder);
		new ImageStreamKubernetesModelProcessor().on(builder);
		new BuildConfigKubernetesModelProcessor().on(builder);
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
