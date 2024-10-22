package com.nerdware.roomy.features.activitystream;

import com.nerdware.roomy.features.offices.dtos.requests.CreateOfficeDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stream")
@Tag(name = "Activity Stream", description = "API for managing offices")
public class ActivityController {

    private final ActivityProducer producer;

    public ActivityController(ActivityProducer producer) {
        this.producer = producer;
    }

    @PostMapping("/activities")
    public void send(@RequestBody Activity request) {
        producer.send(request);
    }
}
