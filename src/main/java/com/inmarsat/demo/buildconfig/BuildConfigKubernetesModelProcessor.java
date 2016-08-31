package com.inmarsat.demo.buildconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.ObjectReference;
import io.fabric8.kubernetes.generator.annotation.KubernetesProvider;
import io.fabric8.openshift.api.model.BuildTriggerPolicy;
import io.fabric8.openshift.api.model.ImageChangeTrigger;
import io.fabric8.openshift.api.model.TemplateBuilder;

public class BuildConfigKubernetesModelProcessor {

    public void on(TemplateBuilder builder) {
        builder.addNewBuildConfigObject()
                .withNewMetadata()
                    .withName("contacts-example")
                    .withLabels(getLabels())
                .endMetadata()
                .withNewSpec()
                    .withTriggers(getTriggers())
                    .withNewSource()
                        .withNewGit()
                            .withUri("https://github.com/Inmarsat-itcloudservices/contacts-example/")
                            .withRef("develop")
                        .endGit()
                        .withType("Git")
                    .endSource()
                    .withNewStrategy()
                        .withNewSourceStrategy()
                            .withNewFrom()
                                .withKind("ImageStreamTag")
                                .withName("fis-java-openshift:latest")
                                .withNamespace("openshift")
                            .endFrom()
                            .withIncremental(true)
                            .addNewEnv().withName("NEXUS_URL").withValue("${NEXUS_URL}").endEnv()
                            .addNewEnv().withName("REPOSITORY_NAME").withValue("${REPOSITORY_NAME}").endEnv()
                            .addNewEnv().withName("GROUP_ID").withValue("${GROUP_ID}").endEnv()
                            .addNewEnv().withName("ARTIFACT_ID").withValue("${ARTIFACT_ID}").endEnv()
                            .addNewEnv().withName("ARTIFACT_VERSION").withValue("${ARTIFACT_VERSION}").endEnv()
                        .endSourceStrategy()
                        .withType("Source")
                    .endStrategy()
                    .withNewOutput()
                        .withNewTo()
                            .withKind("ImageStreamTag")
                            .withName("contacts-example:latest")
                        .endTo()
                    .endOutput()
                .endSpec()
            .endBuildConfigObject()
            .build();
    }

    private BuildTriggerPolicy getTriggers() {
        ObjectReference from = new ObjectReference();
        from.setName("fis-java-openshift:latest");
        from.setKind("ImageStreamTag");
        from.setNamespace("openshift");

        ImageChangeTrigger imageChangeTrigger = new ImageChangeTrigger();
        imageChangeTrigger.setFrom(from);

        BuildTriggerPolicy policy = new BuildTriggerPolicy();
        policy.setType("ImageChange");
        policy.setImageChange(imageChangeTrigger);

        return policy;
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
