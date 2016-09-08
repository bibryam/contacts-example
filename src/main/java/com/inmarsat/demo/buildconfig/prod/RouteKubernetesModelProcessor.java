package com.inmarsat.demo.buildconfig.prod;

import java.util.HashMap;
import java.util.Map;

import io.fabric8.kubernetes.api.model.KubernetesListBuilder;

public class RouteKubernetesModelProcessor {

    public void on(KubernetesListBuilder builder) {
        builder.addNewRouteItem()
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
            .endRouteItem()
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
