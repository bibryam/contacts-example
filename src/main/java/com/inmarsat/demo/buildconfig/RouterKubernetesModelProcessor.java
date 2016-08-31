package com.inmarsat.demo.buildconfig;

import java.util.HashMap;
import java.util.Map;

import io.fabric8.openshift.api.model.TemplateBuilder;

public class RouterKubernetesModelProcessor {

    public void on(TemplateBuilder builder) {
        builder.addNewRouteObject()
                .withNewMetadata()
                    .withName("contacts-example-route")
                    .withLabels(getLabels())
                .endMetadata()
                .withNewSpec()
                    .withNewTo()
                        .withKind("Service")
                        .withName("contacts-example-service")
                    .endTo()
                .endSpec()
            .endRouteObject()
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
