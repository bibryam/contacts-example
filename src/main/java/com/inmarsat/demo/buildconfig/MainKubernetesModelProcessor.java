package com.inmarsat.demo.buildconfig;

import java.util.HashMap;
import java.util.Map;

import io.fabric8.kubernetes.generator.annotation.KubernetesModelProcessor;
import io.fabric8.openshift.api.model.TemplateBuilder;

@KubernetesModelProcessor
public class MainKubernetesModelProcessor {

	public void withTemplateBuilder(TemplateBuilder builder) {
		builder.addNewParameter()
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
					.withDisplayName("Classifier")
					.withName("CLASSIFIER")
					.withValue("app")
				.endParameter()
				.addNewParameter()
					.withDisplayName("Extension")
					.withName("EXTENSION")
					.withValue("zip")
				.endParameter()	
				.addNewParameter()
					.withDisplayName("Artifact Version")
					.withName("ARTIFACT_VERSION")
					.withValue("LATEST")
				.endParameter()
			.withNewMetadata()
				.withAnnotations(getAnnotations())
			.endMetadata();
		new DeploymentConfigKubernetesModelProcessor().on(builder);
		new ImageStreamKubernetesModelProcessor().on(builder);
		new BuildConfigKubernetesModelProcessor().on(builder);
		new ServiceKubernetesModelProcessor().on(builder);
	}
	
	private Map<String, String> getAnnotations()
	{
		Map<String, String> annotations = new HashMap<>();
		annotations.put("description", "Example project demonstrating a Camel route with build pipeline integration");
		annotations.put("iconClass", "icon-jboss");
		return annotations;
	}
}
