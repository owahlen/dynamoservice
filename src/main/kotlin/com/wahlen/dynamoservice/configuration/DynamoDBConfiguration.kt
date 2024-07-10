package com.wahlen.dynamoservice.configuration

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@EnableDynamoDBRepositories(basePackages = ["com.wahlen.dynamoservice.persistence.repository"])
class DynamoDBConfiguration(
    @Value("\${aws.accesskey}")
    private val awsAccessKey: String,

    @Value("\${aws.dynamodb.endpoint}")
    private val awsDynamoDBEndpoint: String,

    @Value("\${aws.region}")
    private val awsRegion: String,

    @Value("\${aws.secretkey}")
    private val awsSecretKey: String
) {


    @Bean
    @Profile("!test")
    fun amazonDynamoDB(): AmazonDynamoDB = AmazonDynamoDBClientBuilder
        .standard()
        .withCredentials(AWSStaticCredentialsProvider(amazonAWSCredentials()))
        .withEndpointConfiguration(EndpointConfiguration(awsDynamoDBEndpoint, awsRegion))
        .build()

    @Bean
    @Profile("!test")
    fun amazonAWSCredentials(): AWSCredentials =
        BasicAWSCredentials(awsAccessKey, awsSecretKey)

}
