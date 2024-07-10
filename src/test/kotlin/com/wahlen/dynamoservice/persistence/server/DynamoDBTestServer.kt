package com.wahlen.dynamoservice.persistence.server

import com.amazonaws.services.dynamodbv2.local.main.ServerRunner
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Order(1)
@Component
class DynamoDBTestServer {

    lateinit var dynamoDBProxyServer: DynamoDBProxyServer

    @PostConstruct
    fun init() {
        System.setProperty("sqlite4java.library.path", "native-libs")
        val port = "8000"
        val server = ServerRunner.createServerFromCommandLineArgs(arrayOf("-inMemory", "-port", port))
        server.start()
        dynamoDBProxyServer = server
    }

    @PreDestroy
    fun destroy() {
        dynamoDBProxyServer.stop()
    }

}