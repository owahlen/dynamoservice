package com.wahlen.dynamoservice.configuration

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

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

    @Primary
    @Bean
    fun dynamoDBMapper(amazonDynamoDB: AmazonDynamoDB): DynamoDBMapper {
        return DynamoDBMapper(amazonDynamoDB, DynamoDBMapperConfig.DEFAULT)
    }

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

    companion object {
        class LocalDateTimeConverter : DynamoDBTypeConverter<Date, LocalDateTime> {
            override fun convert(source: LocalDateTime): Date {
                return Date.from(source.toInstant(ZoneOffset.UTC))
            }

            override fun unconvert(source: Date): LocalDateTime {
                return source.toInstant().atZone(TimeZone.getDefault().toZoneId()).toLocalDateTime()
            }
        }
    }

}
