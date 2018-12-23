/**
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * https://github.com/javaee/tutorial-examples/LICENSE.txt
 */
package martinbradley.hospital.web.flow.prescription;

import java.io.Serializable;
import javax.enterprise.inject.Produces;
import javax.faces.flow.Flow;
import javax.faces.flow.builder.FlowBuilder;
import javax.faces.flow.builder.FlowBuilderParameter;
import javax.faces.flow.builder.FlowDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrescriptionFlow implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(PrescriptionFlow.class);
    private static final long serialVersionUID = 1L;

    public PrescriptionFlow()
    {
        logger.info("Construct the ... PrescriptionFlow");
    }

    @Produces
    @FlowDefinition
    public Flow defineFlow(@FlowBuilderParameter FlowBuilder flowBuilder) {

        System.out.println("Setting up the flow...............");
        final String flowId = "prescriptionFlow";
        flowBuilder.id("", flowId);
        flowBuilder.initializer("#{prescriptionFlowBean.startFlow()}");
        flowBuilder.viewNode(flowId, "/" + flowId + "/" + flowId + ".xhtml").
                             markAsStartNode();
        flowBuilder.viewNode(flowId, "/" + flowId + "/setEndDate.xhtml");
        flowBuilder.viewNode(flowId, "/" + flowId + "/setStartDate.xhtml");

        flowBuilder.returnNode("returnFromFlow").
                    //fromOutcome("");
                    fromOutcome("#{prescriptionFlowBean.cancel}");

        flowBuilder.finalizer("#{prescriptionFlowBean.doneFinished()}");

        logger.info("Returing the flow. prescriptionFlow");
        return flowBuilder.getFlow();
    }
}
