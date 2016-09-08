package com.inmarsat.demo.buildconfig.prod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.ContainerPort;
import io.fabric8.kubernetes.api.model.ExecAction;
import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.KubernetesListBuilder;
import io.fabric8.kubernetes.api.model.ObjectReference;
import io.fabric8.kubernetes.api.model.Probe;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.ResourceRequirements;
import io.fabric8.openshift.api.model.DeploymentTriggerImageChangeParams;
import io.fabric8.openshift.api.model.DeploymentTriggerPolicy;
import io.fabric8.openshift.api.model.RollingDeploymentStrategyParams;
import io.fabric8.utils.Lists;

public class DeploymentConfigKubernetesModelProcessor {

    public void on(KubernetesListBuilder builder) {
        builder.addNewDeploymentConfigItem()
			    .withNewMetadata()
			        .withName("contacts-example")
			        .withLabels(getLabels())
			    .endMetadata()
	        	.withNewSpec()
                    .withReplicas(1)
                    .withSelector(getSelectors())
                    .withNewStrategy()
                        .withType("Rolling")
                        .withRollingParams(getRollingDeploymentStrategyParams())
                    .endStrategy()
		             .withNewTemplate()
		             	.withNewMetadata()
		             		.withLabels(getLabels())
		             	.endMetadata()
		             	.withNewSpec()
		             		.withContainers(getContainers())
		             		.withRestartPolicy("Always")
		                .endSpec()
		             .endTemplate()
		             .withTriggers(getTriggers())
	             .endSpec()
                .endDeploymentConfigItem()
            .build();
    }

    private RollingDeploymentStrategyParams getRollingDeploymentStrategyParams() {
        RollingDeploymentStrategyParams rolling = new RollingDeploymentStrategyParams();
        rolling.setTimeoutSeconds(new Long(240));
        rolling.setMaxSurge(new IntOrString("30%"));
        rolling.setMaxUnavailable(new IntOrString("20%"));

        return rolling;
    }

    private List<DeploymentTriggerPolicy> getTriggers() {
        DeploymentTriggerPolicy configChange = new DeploymentTriggerPolicy();
        configChange.setType("ConfigChange");

        ObjectReference from = new ObjectReference();
        from.setName("${IMAGE_NAME}:${IMAGE_TAG}");
        from.setNamespace("${SOURCE_NAMESPACE}");
        from.setKind("ImageStreamTag");

        DeploymentTriggerImageChangeParams imageChangeParms = new DeploymentTriggerImageChangeParams();
        imageChangeParms.setFrom(from);
        imageChangeParms.setAutomatic(false);

        DeploymentTriggerPolicy imageChange = new DeploymentTriggerPolicy();
        imageChange.setType("ImageChange");
        imageChange.setImageChangeParams(imageChangeParms);
        imageChangeParms.setContainerNames(Lists.newArrayList("contacts-example"));

        List<DeploymentTriggerPolicy> triggers = new ArrayList<DeploymentTriggerPolicy>();
        triggers.add(imageChange);
        triggers.add(configChange);

        return triggers;
    }

    private List<ContainerPort> getPorts() {
        List<ContainerPort> ports = new ArrayList<ContainerPort>();

        ContainerPort cxf = new ContainerPort();
        cxf.setContainerPort(8080);
        cxf.setProtocol("TCP");
        cxf.setName("http");

        ContainerPort jolokia = new ContainerPort();
        jolokia.setContainerPort(8778);
        jolokia.setProtocol("TCP");
        jolokia.setName("jolokia");

        ports.add(cxf);
        ports.add(jolokia);

        return ports;
    }

    private Container getContainers() {
        String livenessProbe= "(curl -f 127.0.0.1:8080) >/dev/null 2>&1; test $? != 7";
        String readinessProbe = "(curl -f 127.0.0.1:8080) >/dev/null 2>&1; test $? != 7";

        Container container = new Container();
        container.setImage("contacts-example:latest");
        container.setImagePullPolicy("Always");
        container.setName("contacts-example");
        container.setPorts(getPorts());
        container.setLivenessProbe(getProbe(livenessProbe, new Integer(30), new Integer(60)));
        container.setReadinessProbe(getProbe(readinessProbe, new Integer(30), new Integer(1)));
        container.setResources(getResourceRequirements());

        return container;
    }

    private Map<String, String> getSelectors() {
        Map<String, String> selectors = new HashMap<>();
        selectors.put("app", "contacts-example");
        selectors.put("deploymentconfig", "contacts-example");

        return selectors;
    }

    private Probe getProbe(String curl, Integer initialDelaySeconds, Integer timeoutSeconds) {
        List<String> commands = new ArrayList<String>();
        commands.add("/bin/bash");
        commands.add("-c");
        commands.add(curl);

        ExecAction execAction = new ExecAction();
        execAction.setCommand(commands);

        Probe probe = new Probe();
        probe.setInitialDelaySeconds(initialDelaySeconds);
        probe.setTimeoutSeconds(timeoutSeconds);
        probe.setExec(execAction);

        return probe;
    }

    private ResourceRequirements getResourceRequirements() {
        ResourceRequirements resourceRequirements = new ResourceRequirements();
        resourceRequirements.setRequests(getRequests());
        resourceRequirements.setLimits(getLimits());

        return resourceRequirements;
    }

    private Map<String, Quantity> getRequests() {
        Map<String, Quantity> limits = new HashMap<String, Quantity>();
        limits.put("memory", new Quantity("512Mi"));

        return limits;
    }

    private Map<String, Quantity> getLimits() {
        Map<String, Quantity> limits = new HashMap<String, Quantity>();
        limits.put("memory", new Quantity("1024Mi"));

        return limits;
    }
    
    private Map<String, String> getLabels() {
        Map<String, String> labels = new HashMap<>();
        labels.put("app", "contacts-example");
        labels.put("deploymentconfig", "contacts-example");
        labels.put("artifact", "demo");
        labels.put("version", "1.0-SNAPSHOT");
        labels.put("group", "com.inmarsat.demo");

        return labels;
    }
}