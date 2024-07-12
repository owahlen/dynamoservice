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

    @PostConstruct
    @Synchronized
    fun init() {
        if (referenceCount == 0) {
            System.setProperty("sqlite4java.library.path", "native-libs")
            val port = "8000"
            val server = ServerRunner.createServerFromCommandLineArgs(arrayOf("-inMemory", "-port", port))
            server.start()
            dynamoDBProxyServer = server
        }
        referenceCount += 1
    }

    @PreDestroy
    @Synchronized
    fun destroy() {
        val server = dynamoDBProxyServer
        if (referenceCount == 1 && server != null) {
            server.stop()
            dynamoDBProxyServer = null
        }
        referenceCount -= 1
    }

    companion object {
        private var referenceCount = 0
        var dynamoDBProxyServer: DynamoDBProxyServer? = null
    }

}