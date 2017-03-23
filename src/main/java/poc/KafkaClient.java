package poc;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static java.lang.System.out;

public class KafkaClient {
  public static void main(String[] args){
    out.println("Connecting");
    new KafkaClient().connect();
  }

  private void connect() {
    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(connectionProperties());
    try {
      consumer.subscribe(Arrays.asList("test"));

      while (true) {
        ConsumerRecords<String, String> records = consumer.poll(Long.MAX_VALUE);
        for (ConsumerRecord<String, String> record : records) {
          Map<String, Object> data = new HashMap<>();
          data.put("partition", record.partition());
          data.put("offset", record.offset());
          data.put("value", record.value());
          System.out.println(data);
        }
      }
    } catch (WakeupException e) {
      // ignore for shutdown
    } finally {
      consumer.close();
    }
  }
  private Properties connectionProperties(){
    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:9092");
    props.put("group.id", "consumer-tutorial");
    props.put("key.deserializer", StringDeserializer.class.getName());
    props.put("value.deserializer", StringDeserializer.class.getName());
    return props;
  }
}
