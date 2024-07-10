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
    @Value("\${amazon.dynamodb.endpoint}")
    private val amazonDynamoDBEndpoint: String,

    @Value("\${amazon.aws.accesskey}")
    private val amazonAWSAccessKey: String,

    @Value("\${amazon.aws.secretkey}")
    private val amazonAWSSecretKey: String
) {


    @Bean
    @Profile("development", "production")
    fun amazonDynamoDB(): AmazonDynamoDB = AmazonDynamoDBClientBuilder
        .standard()
        .withCredentials(AWSStaticCredentialsProvider(amazonAWSCredentials()))
        .withEndpointConfiguration(EndpointConfiguration(amazonDynamoDBEndpoint, null))
        .build()

    @Bean
    @Profile("development", "production")
    fun amazonAWSCredentials(): AWSCredentials =
        BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey)

}
