package com.wahlen.dynamoservice.persistence.extension

import com.amazonaws.services.dynamodbv2.local.main.ServerRunner
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer
import org.junit.jupiter.api.extension.*

/**
 * Local in memory DynamoDB instance for tests
 */
class LocalDynamoExtension : BeforeAllCallback, AfterAllCallback, ParameterResolver {

    private var dynamoDBProxyServer: DynamoDBProxyServer? = null

    init {
        System.setProperty("sqlite4java.library.path", "native-libs")
    }

    override fun beforeAll(extensionContext: ExtensionContext) {
        val port = "8000"
        val server = ServerRunner.createServerFromCommandLineArgs(arrayOf("-inMemory", "-port", port))
        this.dynamoDBProxyServer = server
        server.start()
    }

    override fun afterAll(extensionContext: ExtensionContext) {
        this.stopUnchecked(dynamoDBProxyServer)
    }

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean {
        return parameterContext.parameter.type == DynamoDBProxyServer::class.java
    }

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any {
        val store = extensionContext.root.getStore(ExtensionContext.Namespace.GLOBAL)
        return store.get(DynamoDBProxyServer::class.java, DynamoDBProxyServer::class.java)
    }

    protected fun stopUnchecked(dynamoDbServer: DynamoDBProxyServer?) {
        try {
            dynamoDbServer!!.stop()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

}