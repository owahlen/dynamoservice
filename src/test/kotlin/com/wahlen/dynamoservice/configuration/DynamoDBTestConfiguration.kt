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
    private val awsAccessKey = "TestAccessKey"
    private val awsDynamoDBEndpoint = "http://localhost:8000/"
    private val awsRegion = null
    private val awsSecretKey = "TestSecretKey"

    @Bean
    fun amazonDynamoDB(): AmazonDynamoDB = AmazonDynamoDBClientBuilder
        .standard()
        .withCredentials(AWSStaticCredentialsProvider(amazonAWSCredentials()))
        .withEndpointConfiguration(EndpointConfiguration(awsDynamoDBEndpoint, awsRegion))
        .build()

    @Bean
    fun amazonAWSCredentials(): AWSCredentials =
        BasicAWSCredentials(awsAccessKey, awsSecretKey)

}
