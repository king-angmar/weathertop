package xyz.wongs.weathertop.compoent;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class TestConsumer {

    @KafkaListener(topics = {"wongs"})
    public void listen (ConsumerRecord<?, ?> record) throws Exception {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        Object message = kafkaMessage.get();
        log.error("----------------- record =" + record);
        log.error("------------------ message =" + message);
        if (kafkaMessage.isPresent()) {

        }
    }
}
