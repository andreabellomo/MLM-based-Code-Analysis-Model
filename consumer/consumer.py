import pika
from process import Classifier
import json
import os
from transformers import RobertaForMaskedLM, RobertaTokenizer

class Consumer:
    HOST = 'rabbitmq-container'
    PORT = 5672
    USER = 'guest'
    PASSWORD = 'guest'
    QUEUE_NAME = 'my_queue'
    positive = 0
    counter = 0
    
    

    def start_consumer(self):
        #print("Sono nel consumer")

        #scarico il modello se non esiste
        model_path = '/app/consumer/model'
        tokenizer_path = '/app/consumer/tokenizer'

        # Verifica se le cartelle esistono
        if not os.path.exists(model_path):
            os.makedirs(model_path)

        if not os.path.exists(tokenizer_path):
            os.makedirs(tokenizer_path)

        # Carica il modello e il tokenizer se le cartelle non esistono
        if not os.listdir(model_path):
            model = RobertaForMaskedLM.from_pretrained('microsoft/codebert-base-mlm')
            model.save_pretrained(model_path)

        if not os.listdir(tokenizer_path):
            tokenizer = RobertaTokenizer.from_pretrained('microsoft/codebert-base-mlm')
            tokenizer.save_pretrained(tokenizer_path)

        connection_params = pika.ConnectionParameters(
            host=Consumer.HOST,
            port=Consumer.PORT,
            credentials=pika.PlainCredentials(Consumer.USER, Consumer.PASSWORD),
        )

        

        try:
            with pika.BlockingConnection(connection_params) as connection:
                channel = connection.channel()
                channel.queue_declare(queue=Consumer.QUEUE_NAME)
                channel.basic_qos(prefetch_count=1)
                print("Connected to Rabbit")

                try:
                    print(" [*] Waiting for code. Press CTRL+C to interrupt")
                    channel.basic_consume(queue=Consumer.QUEUE_NAME, on_message_callback=self.callback)
                    channel.start_consuming()
                except KeyboardInterrupt:
                    print(" [x] Interrupted. Closing the consumer.")
        except Exception as e:
            print(f'Connection Error {e}')

    @staticmethod
    def callback(channel, method, properties, body,  ):
        try:
            #print(f'Ricevuto messaggio {body}')
            print('Ricevuto messaggio')
            decoded_body = body.decode('utf-8')
            decoded_body = json.loads(decoded_body)
            print("Valore di X : " ,decoded_body["X"].replace('\n', '') )
            print("Valore di Y : " ,decoded_body["Y"].replace('\n', '') )
            Consumer.counter +=1
            Consumer.accuracy = 0 if Consumer.counter == 0 else Consumer.positive / Consumer.counter
            print("Accuracy:", Consumer.accuracy)

            if(Classifier().start_classifier(decoded_body["X"],decoded_body["Y"].replace('\n', '')) == True):
                print("predizione corretta")
                Consumer.positive += 1
                print("Predizioni corrette: " , Consumer.positive , "/", Consumer.counter)

            channel.basic_ack(delivery_tag=method.delivery_tag)
        except Exception as e:
            print(f'Errore elaborazione del messaggio: {e}')