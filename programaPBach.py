import json
import requests
from kafka import KafkaConsumer

consumer = KafkaConsumer('alarma.conjunto1.baja.1-101',
                         bootstrap_servers=['172.24.42.103:8090'])

for message in consumer:
    elTipo= message.value.decode('utf-8')
    data={"tipo":elTipo}
    data_string = json.dumps(data)
    directorio = message.topic.split('.')
    ubicacion=directorio[3]
    url = "http://172.24.42.79:8080/inmuebles/"+ubicacion+"/alarmas"
    response = requests.post(url,data_string,headers={'Content-type': 'application/json'})
    print("Response Status Code: " + str(response.status_code))
#    }
