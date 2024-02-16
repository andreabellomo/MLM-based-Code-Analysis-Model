package com.mycompany;

import java.util.List;
import java.util.Random;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.github.javaparser.ast.stmt.Statement;
import org.json.*;


public class App {

    private static final Random random = new Random();

    public static String apply_mask(Statement statement){

        String raw = statement.toString();
        String[] words = raw.split("\\s+"); // Dividi la stringa in parole
        if (words.length > 0) {
            int indexToReplace = random.nextInt(words.length);;
            words[indexToReplace] = "<mask>"; // Sostituisci la parola con <mask>
            raw = String.join(" ", words); // Riunisci le parole in una stringa
           // modifiedStatements.add(raw); // Aggiungi la stringa modificata alla lista
            System.out.println(raw);
        }
        return raw;

    }

     public static void main(String[] args) {
        try {
            // Connessione a RabbitMQ
            try (Connection connection = RabbitConnect.createConnection();
                 Channel channel = RabbitConnect.createChannel(connection)) { // creo canale

                RabbitConnect.declareQueue(channel); //dichiaro coda chiamando la funzona della classe

                // Parsing del file
                String filePath = "Sorgente.txt";
                List<Statement> statements = FileParser.parseFile(filePath); // parsing file usando la classe definita

                for (Statement statement : statements){

                    JSONObject message = new JSONObject();
                    message.put("X", apply_mask(statement));
                    message.put("Y", statement);

                    String jsonMessage = message.toString();

                    channel.basicPublish("", RabbitConnect.getQueueName(), null, jsonMessage.getBytes("UTF-8"));
                    System.out.println(" [x] Sent '" + jsonMessage + "'");
                   
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
