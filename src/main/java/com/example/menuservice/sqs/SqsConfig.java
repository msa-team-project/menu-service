package com.example.menuservice.sqs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;

@Component
@ConfigurationProperties(prefix = "aws.sqs")
@Getter
@Setter
public class SqsConfig {

    private String addQueueName;
    private String updateQueueName;
    private String deleteQueueName;

    private String ingredientAddQueueName;
    private String ingredientUpdateQueueName;
    private String ingredientDeleteQueueName;

    @Autowired
    private SqsClient sqsClient;

    public String getQueueUrl(String queueName) {
        return sqsClient.getQueueUrl(GetQueueUrlRequest.builder()
                        .queueName(queueName)
                        .build())
                .queueUrl();
    }
}
