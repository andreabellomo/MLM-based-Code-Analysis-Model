package com.mycompany;

import java.util.List;
import java.util.Random;
import java.util.Arrays;
import java.util.Scanner;

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
    public static Scanner scanner = new Scanner(System.in);
    public static int num = 0;
    public static int maxLength = 450;
    public static List<String> paroleChiaveJava = Arrays.asList(
            "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class",
            "const", "continue", "default", "do", "double", "else", "enum", "extends", "final",
            "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int",
            "interface", "long", "native", "new", "package", "private", "protected", "public",
            "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this",
            "throw", "throws", "transient", "try", "void", "volatile", "while"
        );
     
    public static JSONObject apply_mask_token_len_1(Statement statement) {

        num++;

        JSONObject result = new JSONObject();
        String raw = statement.toString();
        List<String> words = new ArrayList<>(new Sentence(raw).words()); // Converti in una lista mutabile
        
        String real_token = "";
        int indexToReplace = 0 ;
        int tentativi = 0;

        if (!words.isEmpty() && words.size() > 2) {
            
            if(words.size() > maxLength)
                words = words.subList(0, maxLength);

            do {
                indexToReplace = random.nextInt(words.size() - 2) + 1;
                real_token = words.get(indexToReplace);
                tentativi ++;
                
            //} while (real_token.length() != 1 && indexToReplace !=0 && indexToReplace != words.size()-1 && tentativi < words.size()* 2 );
            } while (real_token.length() != 1  && tentativi < words.size()* 2 );

            if(tentativi < words.size()* 2 ){
                
                words.set(indexToReplace, "<mask>");
                raw = String.join(" ", words);
                System.out.println("n° : " + num);
                result.put("X", raw);
                result.put("Y", real_token);
                
            }
               
        }

        return result;
    }

    public static List<Integer> trovaOccorrenze(List<String> listaA, List<String> listaDaControllare) {
        List<Integer> indici = new ArrayList<>();

        for (int i = 0; i < listaA.size(); i++) {
            String parolaA = listaA.get(i);
            if (listaDaControllare.contains(parolaA)) {
                indici.add(i);
            }
        }

        return indici;
    }


    public static JSONObject apply_mask(Statement statement) {

        num++;

        JSONObject result = new JSONObject();
        
        String raw = statement.toString();
        List<String> words = new ArrayList<>(new Sentence(raw).words()); // Converti in una lista mutabile
        String real_token = "";
        int indexToReplace = 0 ;
        int tentativi = 0;
        //System.out.println("WORDS SIZE 1 : " + words.size());

        if (!words.isEmpty() && words.size() > 2) {
            
            if(words.size() > maxLength)
                words = words.subList(0, maxLength);


                List<Integer> indici = trovaOccorrenze(words,paroleChiaveJava);
                //System.out.println("WORDS SIZE 2 : " + words.size());

               // for (int indice : indici) {
               //     System.out.println("Indice trovato: " + indice);
               // }

                if (!indici.isEmpty()) {
                    //System.out.println("WORDS SIZE 3 : " + words.size());
                    indexToReplace = indici.get(random.nextInt(indici.size()));
                    //System.out.println("INDEX_TO_REPLACE : " + indexToReplace);
                    //System.out.println("WORDS SIZE 4 : " + words.size());
                    real_token = words.get(indexToReplace);
                    System.out.println("Indice scelto : " + indexToReplace + " valore : " +  real_token);
                    words.set(indexToReplace, "<mask>");
                    raw = String.join(" ", words);
                    System.out.println("n° : " + num);
                    result.put("X", raw);
                    result.put("Y", real_token);

                } 

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
                    //JSONObject message = apply_mask_token_len_1(statement); // 1° modalità
                    
                    JSONObject message = apply_mask(statement); // 2° modalità
                    
                    
                    String msg = message.toString();
                    if(msg.equals("{}")) continue;

                    channel.basicPublish("", RabbitConnect.getQueueName(), null, msg.getBytes("UTF-8"));
                    System.out.println(" [x] Sent '" + msg + "'");
                   
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
