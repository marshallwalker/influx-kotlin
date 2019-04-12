package ca.marshallwalker.influx

import com.fasterxml.jackson.databind.JsonNode
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.response.HttpResponse

class InfluxService internal constructor(
    private val httpClient: HttpClient,
    private val url: String) {

    private suspend inline fun <reified T> query(query: String) = httpClient.post<T>("$url/query") {
        parameter("q", query)
    }

    suspend fun ping(): Pong {
        val startEpoch = System.currentTimeMillis()
        val response = httpClient.get<HttpResponse>("$url/ping")
        val influxVersion = response.headers["X-Influxdb-Version"] ?: "unknown"
        return Pong(influxVersion, System.currentTimeMillis() - startEpoch)
    }

    suspend fun version() = ping().version

    suspend fun createDatabase(database: String): Unit =
        query("CREATE DATABASE \"$database\"")

    suspend fun deleteDatabase(database: String): Unit =
        query("DROP DATABASE \"$database\"")

    suspend fun describeDatabases(): List<String> {
        val response = query<JsonNode>("SHOW DATABASES")
        val result = response["results"].first()
        val series = result["series"].first()

        return series["values"].map { it.first().asText() }.toList()
    }

    suspend fun databaseExists(database: String) =
            describeDatabases().contains(database)
}