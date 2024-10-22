package com.nerdware.roomy.features.activitystream;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ActivityConsumer {

    @KafkaListener(topics = "ActivityStream", groupId = "activity_group")
    public void consume(Activity activity) {
        System.out.println("Consumed message: " + activity);
    }
}
