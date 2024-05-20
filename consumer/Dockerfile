#dockerfile per creare l immaggine base personalizzata per evitare di installare i requirements ogni volta 

FROM python:3.9-slim
WORKDIR /app
COPY requirements.txt requirements.txt
RUN pip install -r requirements.txt


