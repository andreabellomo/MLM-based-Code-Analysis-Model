'''
import pika

def callback(channel, method, properties, body):
    try:
        print(f'Ricevuto messaggio {body}')
        channel.basic_ack(delivery_tag=method.delivery_tag)
    except Exception as e:
        print(f'Errore durante l\'elaborazione del messaggio: {e}')

class Consumer:
    HOST = 'rabbitmq'
    PORT = 5672
    USER = 'guest'
    PASSWORD = 'guest'
    QUEUE_NAME = 'hello'

    def start_consumer(self):
        print("Sono nel consumer")

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
                print("Mi sono collegato")

                try:
                    print(" [*] Waiting for subimages. Press CTRL+C to interrupt")
                    channel.basic_consume(queue=Consumer.QUEUE_NAME, on_message_callback=callback)
                    channel.start_consuming()
                except KeyboardInterrupt:
                    print(" [x] Interrupted. Closing the consumer.")
        except Exception as e:
            print(f'Errore durante la connessione o la creazione del canale: {e}')
'''
