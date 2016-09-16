package com.redhat.cep.server;

import org.apache.camel.builder.RouteBuilder;

/**
 * This class defines the routes on the Server. 
 */

public class ServerRoutes extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // route from the events queue to our CEP application which is a spring bean registered with id=processEvent

    	from("jms:queue:events").to("processEvent");

    }

}
