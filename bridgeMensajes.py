import json
import requests
from kafka import KafkaConsumer

consumer = KafkaConsumer('alarma.conjunto1.baja.1-101',
                         bootstrap_servers=['172.24.42.103:8090'])
for message in consumer:
 json_data = json.loads(message.value.decode('utf-8'))
 correo= json_data.get('correo')
 asunto= json_data.get('asunto')
 cuerpo=json_data.get('cuerpo')
 url = 'http://172.24.42.45:8080/correo'
 payload = {
 'correo': correo,
 'asunto': asunto,
 'cuerpo': cuerpo
 }
 response = requests.post(url, data=json.dumps(payload),headers={'Content-type': 'application/json'})
 print("Response Status Code: "+ str(response.status_code))
