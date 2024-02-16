'''
from consumer import Consumer

if __name__ == '__main__':
    consumer = Consumer()
    print("sti chiamando il consumer")
    consumer.start_consumer()
'''

from transformers import RobertaTokenizer, RobertaForMaskedLM, pipeline

model = RobertaForMaskedLM.from_pretrained('microsoft/codebert-base-mlm')
tokenizer = RobertaTokenizer.from_pretrained('microsoft/codebert-base-mlm')
#code_example = "if (x is not None) <mask> (x>1)"
#code_example = "int result = <mask> + 10;"
code_example = "if (x <mask> 0){}"

fill_mask = pipeline('fill-mask', model=model, tokenizer=tokenizer)

outputs = fill_mask(code_example)
print(outputs)