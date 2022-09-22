/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.demo.restful;

import com.demo.bean.Persona;
import com.demo.queue.QueueUtil;
import com.demo.threadWS.TestProducerThread;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import jdk.jfr.ContentType;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.codehaus.jackson.map.ObjectMapper;

import javax.jms.*;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author mmendez
 */
@Path("/")
public class WebService {

    @GET
    @Path("/datosPersona")
    @Produces(MediaType.APPLICATION_JSON)
    public Persona getDatosPersona() {
        Persona persona = new Persona();



        //inicia consumidor de la cola

                String nombreCola = "queue.so1.demo";
                String nombreServicio = "EjemploCola_";
                String serverLocation = "failover:(tcp://135.132.1.35:61616)?timeout=3000";

                try {
                    // Create a ConnectionFactory
                    ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(serverLocation);
                    // Create a Connection
                    Connection connection = connectionFactory.createConnection();
                    connection.start();
                    // Create a Session
                    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                    // Create the destination (Topic or Queue)
                    Destination destination = session.createQueue(nombreCola);
                    // Create a MessageConsumer from the Session to the Topic or Queue
                    MessageConsumer consumer = session.createConsumer(destination);

                        try {
                            Message message = consumer.receive(1000);
                            // extraccion de datos en cola
                            if (message instanceof TextMessage) {
                                TextMessage textMessage = (TextMessage) message;
                                String text = textMessage.getText();

                                // mm. 21102017 codigo de conversion json a obj persona
                                ObjectMapper mapper = new ObjectMapper();
                                Persona objetoPersona = mapper.readValue(text, Persona.class);
                                persona=objetoPersona;

                            } else {
                               // System.out.println("[" + threadId + "]Received: " + message);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                } catch (Exception e) {
                    e.printStackTrace();
                }

        //finaliza consumidor de la cola

        return persona;

    }

    @POST
    @Path("/enviarDatos")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    public Persona setDatosPersona(
            Persona persona
    ) {


            persona.setId(1);
            persona.setNombre("Luis Alberto");
            persona.setEdad(47);

            //inicia producer

                String nombreCola = "queue.so1.demo";
                String nombreServicio = "EjemploCola";
                String serverLocation = "failover:(tcp://135.132.1.35:61616)?timeout=3000";

                System.out.println("variables creadas:" + nombreCola +" - " + nombreServicio + " - " + serverLocation);

                String message = " {"
                        + " \"id\":" + "1" + ","
                        + " \"nombre\":\"" + "manuel" + "\","
                        + " \"edad\":" + "2"
                        + "}";

                System.out.println("Mensaje : " + message);
                try {

                    QueueUtil.send(nombreCola, true, true, 0, nombreServicio, message, serverLocation);

                    System.out.println("Enviando mensaje....");
                    Thread.sleep(500);

                } catch (Exception e) {
                    System.out.println("Error....");
                    e.printStackTrace();
                }
            //finaliza producer


        return persona;
    }

}

