package com.mycompany;

import java.util.List;
import java.util.Random;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.github.javaparser.ast.stmt.Statement;
import org.json.*;
import java.util.StringTokenizer;

import com.github.javaparser.ast.NodeList;

import com.github.javaparser.Token;


public class App {

    private static final Random random = new Random();

     
    public static JSONObject apply_mask(Statement statement) {
        JSONObject result = new JSONObject();

        String raw = statement.toString();
        String real_token = "";
        String[] words = raw.split("\\s+");

        if (words.length > 0) {
            int indexToReplace = random.nextInt(words.length);
            real_token = words[indexToReplace];
            words[indexToReplace] = "<mask>";
            raw = String.join(" ", words);

            result.put("X", raw);
            result.put("Y", real_token);

           // System.out.println(result);
        }

        return result;
    }
    
   /* 
    public static JSONObject applyMask(Statement statement) {
        JSONObject result = new JSONObject();

        // Ottieni il TokenRange dello statement
        NodeList<TokenRange> tokenRanges = statement.getTokenRange().orElse(new NodeList<>()).getTokenRanges();
        if (!tokenRanges.isEmpty()) {
            int indexToReplace = new Random().nextInt(tokenRanges.size());
            TokenRange tokenRange = tokenRanges.get(indexToReplace);
            String realToken = tokenRange.toString();
            statement.replace(tokenRange, "<mask>");
            String maskedStatement = statement.toString();
            result.put("X", maskedStatement);
            result.put("Y", realToken);
        }


        return result;
    }
    
*/
     public static void main(String[] args) {

        
        try {

            String filePath = "/producer/Sorgente.txt";
            //String filePath = "C://Users//bello//Desktop//porg//Java_Parser_App//producer//Sorgente.txt";
            List<Statement> statements = FileParser.parseFile(filePath);
            // Connessione a RabbitMQ
            try (Connection connection = RabbitConnect.createConnection();
                 Channel channel = RabbitConnect.createChannel(connection)) { // creo canale

                RabbitConnect.declareQueue(channel); //dichiaro coda chiamando la funzona della classe

                // Parsing del file
                //String filePath = "Sorgente.txt";
                //List<Statement> statements = FileParser.parseFile(filePath); // parsing file usando la classe definita

                for (Statement statement : statements){

                    //JSONObject message = new JSONObject();
                    JSONObject message = apply_mask(statement);
                    
                   // message.put("X", result[raw]);
                  //  message.put("Y", statement.toString());

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
