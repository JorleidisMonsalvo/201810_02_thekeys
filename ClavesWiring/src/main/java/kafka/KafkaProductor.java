/*
* Contenido extraido de https://gist.github.com/sdpatil/f803ff60dd0e9c5f8664fcd5fbb60242
 * The MIT License
 *
 * Copyright 2018 Universidad De Los Andes - Departamento de Ingeniería de Sistemas.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package kafka;

import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.ClaveDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.connect.json.JsonSerializer;

/**
 *
 * @author jc.sanguino10
 */
public class KafkaProductor {

    private static final String topicName = "claves.conjunto1.1-101";
    private static final String bootstrap_servers = "172.24.42.103:8090";

    public void publicar(ClaveDTO clave) {

        Properties configProperties = new Properties();
        configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap_servers);
        configProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArraySerializer");
        configProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.connect.json.JsonSerializer");

        org.apache.kafka.clients.producer.Producer producer = new KafkaProducer(configProperties);

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.valueToTree(clave);
        ProducerRecord<String, JsonNode> rec = new ProducerRecord<String, JsonNode>(topicName, jsonNode);
        producer.send(rec);
        producer.close();
    }

}
