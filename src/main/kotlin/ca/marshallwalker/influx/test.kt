package ca.marshallwalker.influx

import kotlinx.coroutines.runBlocking

fun main() {
    val influxClient by InfluxClientFactory {
        url = "http://192.168.1.245:8086"
    }

    runBlocking {
        println("Ping: ${influxClient.ping()}")
        println("Version: ${influxClient.version()}")
        println("Databases: ${influxClient.getDatabases()}")
    }
}