package com.inmarsat.demo.buildconfig.dev;

import java.util.HashMap;
import java.util.Map;

import io.fabric8.kubernetes.api.model.KubernetesListBuilder;


public class ImageStreamKubernetesModelProcessor {

    public void on(KubernetesListBuilder builder) {
		        builder.addNewImageStreamItem()
			        .withNewMetadata()
			            .withName("contacts-example")
			            .withLabels(getLabels())
			        .endMetadata()
			        .withNewSpec()
			            .withDockerImageRepository("")
			        .endSpec()
			        .endImageStreamItem()
		       .build();
    }


    private Map<String, String> getLabels() {
        Map<String, String> labels = new HashMap<>();
        labels.put("app", "contacts-example");
        labels.put("artifact", "demo");
        labels.put("version", "1.0-SNAPSHOT");
        labels.put("group", "com.inmarsat.demo");

        return labels;
    }
}
