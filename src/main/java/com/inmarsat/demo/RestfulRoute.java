/*
 * Copyright 2005-2015 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.inmarsat.demo;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ContextName;
import org.apache.camel.cdi.Uri;
import org.apache.camel.model.rest.RestBindingMode;

import javax.inject.Inject;

/**
 * Configures all our Camel routes, components, endpoints and beans
 */
@ContextName("myJettyCamel")
public class RestfulRoute extends RouteBuilder {

    @Inject
    @Uri("file:individual-report")
    private Endpoint fileEndpoint;

    @Override
    public void configure() throws Exception {

        rest("/api").id("rest-api")
            .get("/hello")
                .produces("application/json")
                .to("direct:hello-world")
            .post("/hello")
                .consumes("application/xml")
                .type(Contacts.class)
                .to("direct:hello");

        from("direct:hello-world").id("direct-helloworld")
            .log(LoggingLevel.ERROR, "got a GET request")
            .setBody(constant("{\"resp\": \"This endpoint does not support GET requests\"}"));

        from("direct:hello").id("say-hello")
            .log("got a new Post")
            .inOnly("seda:split")
            .process(new Processor() {
                @Override
                public void process(Exchange exchange) throws Exception {
                    exchange.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, 202);
                }
            });

        from("seda:split").id("splitter")
                .split(simple("${body.contacts}"))
                .transform(simple("${body.toString}"))
                .log(LoggingLevel.INFO, "${body}")
                .to(fileEndpoint);

        restConfiguration()
            .component("jetty")
            .host("0.0.0.0")
            .port(8080)
            .bindingMode(RestBindingMode.xml);
    }
}
