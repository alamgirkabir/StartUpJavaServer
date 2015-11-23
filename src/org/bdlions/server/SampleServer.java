/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bdlions.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import java.sql.Connection;
import java.sql.SQLException;
import org.bdlions.db.Database;
import org.bdlions.exceptions.DBSetupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author alamgir
 */
public class SampleServer extends AbstractVerticle {
    final static Logger logger = LoggerFactory.getLogger(AbstractVerticle.class);
    private final int SERVER_PORT = 5050;
    
    @Override
    public void start() {
        
        
        
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);

        //this is a sample function, this will not go into production
        router.route("/").handler((RoutingContext routingContext) -> {
            HttpServerResponse response = routingContext.response();
            response.end("Running Initial Java web server");
        });
        
        //this is a sample function, this will not go into production
        router.route("/setupDatabase").handler((RoutingContext routingContext) -> {
            try {
                HttpServerResponse response = routingContext.response();
                
                Database db = Database.getInstance();
                Connection connection = db.getConnection();
                if(connection == null){
                    logger.info("Db connection not set.");
                }
                response.end("Database setup completed");
            } catch (DBSetupException | SQLException ex) {
                logger.error(ex.getMessage());
            }
        });
        
        //this is a sample function, this will not go into production
        router.route("/samplepost*").handler(BodyHandler.create());
        router.post("/samplepost").handler((RoutingContext routingContext) -> {
            
            String param1 = routingContext.request().getParam("param1");
            String param2 = routingContext.request().getParam("param2");
            
            HttpServerResponse response = routingContext.response();
            response.end("Param1 " + param1 + " " + " Param2: " + param2);
        });
        
        //this is a sample function, this will not go into production
        router.route("/sampleget").handler((RoutingContext routingContext) -> {
            String param1 = routingContext.request().getParam("param1"); 
            HttpServerResponse response = routingContext.response();
            response.end("Param1: " + param1);
        });
        
        server.requestHandler(router::accept).listen(SERVER_PORT);
    }

}
