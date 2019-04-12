package ca.marshallwalker.influx

import kotlinx.coroutines.runBlocking

fun main() {
    val url = "http://192.168.1.245:8086"
    val influxClient: InfluxClient by InfluxFactory(url = url)

    runBlocking {
        val databases = influxClient.describeDatabases()
        println(databases.joinToString(", "))
    }
}