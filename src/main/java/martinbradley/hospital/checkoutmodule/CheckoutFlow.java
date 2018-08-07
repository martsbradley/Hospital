/**
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * https://github.com/javaee/tutorial-examples/LICENSE.txt
 */
package martinbradley.hospital.checkoutmodule;

import java.io.Serializable;
import javax.enterprise.inject.Produces;
import javax.faces.flow.Flow;
import javax.faces.flow.builder.FlowBuilder;
import javax.faces.flow.builder.FlowBuilderParameter;
import javax.faces.flow.builder.FlowDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckoutFlow implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(CheckoutFlow.class);
    private static final long serialVersionUID = 1L;

    public CheckoutFlow()
    {
        System.out.println("Construct the ... CheckoutFlow");
        logger.warn("Construct the ... CheckoutFlow");
    }

    @Produces
    @FlowDefinition
    public Flow defineFlow(@FlowBuilderParameter FlowBuilder flowBuilder) {

        System.out.println("Setting up the flow...............");
        final String flowId = "checkoutFlow";
        flowBuilder.id("", flowId);
        flowBuilder.viewNode(flowId, 
                "/" + flowId + "/" + flowId + ".xhtml").
                markAsStartNode();

        flowBuilder.returnNode("returnFromCheckoutFlow").
                fromOutcome("#{checkoutFlowBean.returnValue}");

        flowBuilder.inboundParameter("param1FromJoinFlow", 
                "#{flowScope.param1Value}");
        flowBuilder.inboundParameter("param2FromJoinFlow", 
                "#{flowScope.param2Value}");

        flowBuilder.flowCallNode("calljoin").flowReference("", "joinFlow").
                outboundParameter("param1FromCheckoutFlow", 
                "#{checkoutFlowBean.name}").
                outboundParameter("param2FromCheckoutFlow", 
                "#{checkoutFlowBean.city}");
        logger.warn("Returing the flow. CheckoutFlow");
        return flowBuilder.getFlow();
    }
}
