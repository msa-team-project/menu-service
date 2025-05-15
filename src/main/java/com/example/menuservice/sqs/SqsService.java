package com.example.menuservice.sqs;

import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;
import software.amazon.awssdk.services.sqs.SqsClient;
import org.springframework.stereotype.Service;

@Service
public class SqsService {

    private final SqsClient sqsClient;
    private final SqsConfig sqsConfig;

    public SqsService(SqsClient sqsClient, SqsConfig sqsConfig) {
        this.sqsClient = sqsClient;
        this.sqsConfig = sqsConfig;
    }

    // 기본 호출용: 기존처럼 addQueueName 으로 메시지 보냄
    public void sendMessageToSqs(String messageBody) {
        sendMessageToSqs(messageBody, sqsConfig.getAddQueueName());
    }

    // 큐 이름 직접 지정 가능
    public void sendMessageToSqs(String messageBody, String queueName) {
        String queueUrl = sqsConfig.getQueueUrl(queueName);

        SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(messageBody)
                .build();

        SendMessageResponse sendMessageResponse = sqsClient.sendMessage(sendMessageRequest);

        System.out.println("Message Sent to SQS: " + sendMessageResponse.messageId() + " to queue: " + queueName);
    }
}
