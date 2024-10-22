package com.nerdware.roomy.features.activitystream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ActivityProducer {

    private final KafkaTemplate<String, Activity> kafkaTemplate;

    @Autowired
    public ActivityProducer(KafkaTemplate<String, Activity> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(Activity activity) {
        this.kafkaTemplate.send("ActivityStream", activity);
    }
}
