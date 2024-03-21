package com.mycompany;

import java.util.List;
import java.util.Random;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.github.javaparser.ast.stmt.Statement;
import org.json.*;
import java.util.StringTokenizer;
import edu.stanford.nlp.simple.*;
import java.util.ArrayList;

import com.github.javaparser.ast.NodeList;

import com.github.javaparser.Token;


public class App {

    private static final Random random = new Random();
    public static int num = 0;
    public static int maxLength = 350;

     
    public static JSONObject apply_mask(Statement statement) {
        JSONObject result = new JSONObject();
        num++;
        

        String raw = statement.toString();
        List<String> words = new ArrayList<>(new Sentence(raw).words()); // Converti in una lista mutabile
        String real_token = "";

        if (!words.isEmpty()) {
            
            System.out.println("num : " +num + " SIZE = " + words.size());

            if(words.size() > maxLength)
                words = words.subList(0, maxLength);
            
            int indexToReplace = random.nextInt(words.size());
            real_token = words.get(indexToReplace);
            words.set(indexToReplace, "<mask>");
            raw = String.join(" ", words);

            result.put("X", raw);
            result.put("Y", real_token);
        }

        return result;
    }
    
     public static void main(String[] args) {

        
        try {

            String filePath = "/producer/Sorgente.txt";
            
            List<Statement> statements = FileParser.parseFile(filePath);
            // Connessione a RabbitMQ
            try (Connection connection = RabbitConnect.createConnection();
                 Channel channel = RabbitConnect.createChannel(connection)) { // creo canale

                RabbitConnect.declareQueue(channel); //dichiaro coda chiamando la funzione della classe

               
                for (Statement statement : statements){

                    
                    JSONObject message = apply_mask(statement);
                    String msg = message.toString();

                    channel.basicPublish("", RabbitConnect.getQueueName(), null, msg.getBytes("UTF-8"));
                    System.out.println(" [x] Sent '" + msg + "'");
                   
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
