from transformers import AutoTokenizer, AutoModel, RobertaConfig,AutoModelForMaskedLM , pipeline


class Classifier:

    def __init__(self):
        self.model = AutoModelForMaskedLM.from_pretrained("/app/consumer/model")
        self.tokenizer = AutoTokenizer.from_pretrained("/app/consumer/tokenizer")
        self.fill_mask = pipeline('fill-mask', model=self.model, tokenizer=self.tokenizer)


    def start_classifier(self, message, etichetta):
        prediction = self.fill_mask(message)
        
        first_token = prediction[0]['token_str']
        sequence = prediction[0]['sequence']
        score = prediction[0]['score']

        print(f'Token predetto: {first_token}, Sequence: {sequence}, Score: {score}')
        print("etichetta: ", etichetta)
        print("-predetta: ", first_token)

        if first_token.replace(" ", "").replace(";", "") == etichetta.replace(" ", "").replace(";", ""):
            return True
            
            
            
        

        

    
        

