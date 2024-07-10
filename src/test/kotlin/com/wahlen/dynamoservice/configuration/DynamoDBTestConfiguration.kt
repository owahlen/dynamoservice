package com.wahlen.dynamoservice.configuration

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DynamoDBTestConfiguration {
    private val amazonAWSAccessKey = "TestAccessKey"
    private val amazonAWSSecretKey = "TestSecretKey"
    private val amazonDynamoDBEndpoint = "http://localhost:8000/"

    @Bean
    fun amazonDynamoDB(): AmazonDynamoDB = AmazonDynamoDBClientBuilder
        .standard()
        .withCredentials(AWSStaticCredentialsProvider(amazonAWSCredentials()))
        .withEndpointConfiguration(EndpointConfiguration(amazonDynamoDBEndpoint, null))
        .build()

    @Bean
    fun amazonAWSCredentials(): AWSCredentials =
        BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey)

}
