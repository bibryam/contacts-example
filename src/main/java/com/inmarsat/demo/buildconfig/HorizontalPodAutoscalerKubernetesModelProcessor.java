package com.inmarsat.demo.buildconfig;

import java.util.HashMap;
import java.util.Map;

import io.fabric8.kubernetes.api.model.extensions.CPUTargetUtilization;
import io.fabric8.openshift.api.model.TemplateBuilder;

public class HorizontalPodAutoscalerKubernetesModelProcessor {

    public void on(TemplateBuilder builder) {
        builder.addNewHorizontalPodAutoscalerObject()
                .withNewMetadata()
                    .withName("contacts-example")
                    .withLabels(getLabels())
                .endMetadata()
                .withNewSpec()
                    .withNewScaleRef()
                        .withApiVersion("v1")
                        .withSubresource("scale")
                        .withKind("DeploymentConfig")
                        .withName("contacts-example")
                    .endScaleRef()
                    .withCpuUtilization(new CPUTargetUtilization(80))
                    .withMinReplicas(1)
                    .withMaxReplicas(10)
                .endSpec()
            .endHorizontalPodAutoscalerObject()
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
