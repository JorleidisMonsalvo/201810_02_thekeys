import json
import requests
from kafka import KafkaConsumer

consumer = KafkaConsumer('alarma.conjunto1.baja.1-101',
                         bootstrap_servers=['172.24.42.103:8090'])

for message in consumer:
    elTipo= message.value.decode('utf-8')
    data={"tipo":elTipo}
    data_string = json.dumps(data)
#	json_data = json.loads(message.value.decode('utf-8'))
#	sensetime = json_data['dataValue']
    print(data_string)
    print(message.value)
    print(message.value.decode('utf-8'))
    print(message.topic)
#	sense = json_data['temperature']
#	location = message.topic.split('.')
#	sensor_code = location[3]

#	url = 'http://172.24.42.73:8080/sensors/' + sensor_code + '/realtimedata'
#	payload = {
#		'samplingTime': sensetime,
#		'dataValue': sense['data']
#    }
#	response = requests.post(url, data=json.dumps(payload),
#							 headers={'Content-type': 'application/json'})

#	print("Response Status Code: " + str(response.status_code))