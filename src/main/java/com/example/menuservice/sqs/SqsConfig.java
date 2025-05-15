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
    private String queueName;

    @Autowired
    private SqsClient sqsClient;

    public String getQueueUrl() {
        return sqsClient.getQueueUrl(GetQueueUrlRequest.builder().queueName(queueName).build()).queueUrl();
    }
}
