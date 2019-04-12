package ca.marshallwalker.influx

import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.response.HttpResponse

class InfluxClient internal constructor(
    private val objectMapper: ObjectMapper,
    private val httpClient: HttpClient,
    private val url: String,
    private val username: String,
    private val password: String) {

    suspend fun ping(): Pong {
        val startEpoch = System.currentTimeMillis()
        val response = httpClient.get<HttpResponse>("$url/ping")
        val influxVersion = response.headers["X-Influxdb-Version"] ?: "unknown"
        return Pong(influxVersion, System.currentTimeMillis() - startEpoch)
    }

    suspend fun version() = ping().version
}