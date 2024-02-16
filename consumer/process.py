from transformers import AutoTokenizer, AutoModel, RobertaConfig,AutoModelForMaskedLM , pipeline


class classifier:

    def __init__(self):
        self.model = AutoModelForMaskedLM.from_pretrained("/app/consumer/model")
        self.tokenizer = AutoTokenizer.from_pretrained("/app/consumer/tokenizer")
        self.fill_mask = pipeline('fill-mask', model=self.model, tokenizer=self.tokenizer)

    def start_classifier(self,message):

        outputs = self.fill_mask(message)
        print(outputs)