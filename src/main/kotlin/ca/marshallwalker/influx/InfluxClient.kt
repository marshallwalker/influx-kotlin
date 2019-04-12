package ca.marshallwalker.influx

import io.ktor.client.HttpClient

class InfluxClient internal constructor(
    httpClient: HttpClient,
    url: String) {

    private val influxService = InfluxService(httpClient, url)

    suspend fun ping() = influxService.ping()

    suspend fun version() = influxService.version()

    suspend fun createDatabase(database: String) = influxService.createDatabase(database)

    suspend fun deleteDatabase(database: String) = influxService.deleteDatabase(database)

    suspend fun describeDatabases(): List<String> = influxService.describeDatabases()
}