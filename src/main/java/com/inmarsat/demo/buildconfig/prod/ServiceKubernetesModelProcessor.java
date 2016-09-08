package com.inmarsat.demo.buildconfig.prod;

import java.util.HashMap;
import java.util.Map;

import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.KubernetesListBuilder;

public class ServiceKubernetesModelProcessor {

    public void on(KubernetesListBuilder builder) {
        builder.addNewServiceItem()
	        		.withNewMetadata()
	        			.withLabels(getLabels())
	        			.withName("contacts-example-service")
	        		.endMetadata()
	        		.withNewSpec()
	        			.addNewPort()
	        				.withPort(new Integer(8080))
	        				.withProtocol("TCP")
	        				.withTargetPort(new IntOrString(new Integer(8080)))
	        			.endPort()
	        			.withSelector(getSelectors())
	        			.withSessionAffinity("None")
	        			.withType("ClusterIP")
	        			.withLoadBalancerIP("")
	        		.endSpec()
        		.endServiceItem()
        	.build();
    }

    private Map<String, String> getLabels() {
        Map<String, String> labels = new HashMap<>();
        labels.put("app", "contacts-example");
        labels.put("project", "contacts-example");
        labels.put("version", "1.0.0-SNAPSHOT");
        labels.put("group", "com.inmarsat.demo");

        return labels;
    }
    
    private Map<String, String> getSelectors() {
        Map<String, String> selectors = new HashMap<>();
        selectors.put("app", "contacts-example");

        return selectors;
    }
}
