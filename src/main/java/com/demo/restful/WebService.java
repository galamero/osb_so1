/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.demo.restful;

import com.demo.bean.Persona;
import com.demo.bean.Plataforma;
import com.demo.queue.QueueUtil;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.codehaus.jackson.map.ObjectMapper;

import javax.jms.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

/**
 *
 * @author mmendez
 */
@Path("/")
public class WebService {

    @GET
    @Path("/datosPlataforma")
    @Produces(MediaType.APPLICATION_JSON)
    public Plataforma getDatosPersona() {
        Plataforma plataforma = new Plataforma();



        //inicia consumidor de la cola

                String nombreCola = "queue.so1.segundo.parcial";
                String nombreServicio = "PrimerEntregable";
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
                                System.out.println("Result========> "+ text);
                                // mm. 21102017 codigo de conversion json a obj persona
                                ObjectMapper mapper = new ObjectMapper();
                                Plataforma objetoPlataforma = mapper.readValue(text, Plataforma.class);
                                plataforma=objetoPlataforma;

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

        return plataforma;

    }


    @Path("/enviarDatos")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Plataforma setDatosPersona(Plataforma plataforma) {
        try {
            //inicia producer

            String nombreCola = "queue.so1.segundo.parcial";
            String nombreServicio = "PrimerEntregable";
            String serverLocation = "failover:(tcp://135.132.1.35:61616)?timeout=3000";

            System.out.println("variables creadas:" + nombreCola +" - " + nombreServicio + " - " + serverLocation);
            ObjectMapper objectMapper = new ObjectMapper();
            String message = null;
            message = objectMapper.writeValueAsString(plataforma);

            System.out.println("Mensaje : " + message);
            try {

                QueueUtil.send(nombreCola, true, true, 0, nombreServicio, message, serverLocation);

                System.out.println("Enviando mensaje....");
                Thread.sleep(500);

            } catch (Exception e) {
                System.out.println("Error....");
                e.printStackTrace();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //finaliza producer


        return plataforma;
    }

}

