package com.example.menuservice.sqs;



import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;
import software.amazon.awssdk.services.sqs.SqsClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SqsService {

    private final SqsClient sqsClient;

    @Value("${aws.sqs.queueName}")
    private String queueName;

    public SqsService(SqsClient sqsClient) {
        this.sqsClient = sqsClient;
    }

    public void sendMessageToSqs(String messageBody) {
        // 큐 URL 가져오기
        String queueUrl = sqsClient.getQueueUrl(builder -> builder.queueName(queueName)).queueUrl();

        // 메시지 보내기
        SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(messageBody)
                .build();

        SendMessageResponse sendMessageResponse = sqsClient.sendMessage(sendMessageRequest);

        // 메시지 전송 결과 확인
        System.out.println("Message Sent to SQS: " + sendMessageResponse.messageId());
    }
}
