# Usa un'immagine di Python ufficiale
FROM python:3.9-slim

WORKDIR /app

COPY . .

# Installa le dipendenze elencate nel file requirements.txt
RUN pip install --no-cache-dir -r requirements.txt


WORKDIR /app

CMD python -u app.py

