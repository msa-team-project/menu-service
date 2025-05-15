package com.example.menuservice.sqs;



import com.example.menuservice.sqs.SqsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SqsController {

    private final SqsService sqsService;

    public SqsController(SqsService sqsService) {
        this.sqsService = sqsService;
    }

    @PostMapping("/send-message")
    public String sendMessage(@RequestBody String message) {
        sqsService.sendMessageToSqs(message);
        return "Message Sent!";
    }
}

