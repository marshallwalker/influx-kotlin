package ca.marshallwalker.influx

import ca.marshallwalker.influx.model.Point
import ca.marshallwalker.influx.model.Pong
import com.fasterxml.jackson.databind.JsonNode
import io.ktor.client.HttpClient
import io.ktor.client.request.*
import io.ktor.client.response.HttpResponse
import io.ktor.http.HttpMethod
import org.slf4j.LoggerFactory
import kotlin.system.measureTimeMillis

class InfluxService internal constructor(
    private val httpClient: HttpClient,
    private val url: String): InfluxRepository {

    private suspend inline fun <reified T: Any> query(query: String) = httpClient.post<T>("$url/query") {
        parameter("q", query)
    }.also { logIt("query", it)}

    private suspend fun write(database: String, line: String) = httpClient.post<HttpResponse>("$url/write") {
        parameter("db", database)
        body = line
    }.also { logIt("write", line)}

    override suspend fun ping(): Pong {
        var version = "unknown"
        val took = measureTimeMillis {
            val response = httpClient.get<HttpResponse>("$url/ping")
            version = response.headers["X-Influxdb-Version"] ?: version
        }

        return Pong(version, took)
    }

    override suspend fun version() = ping().version

    override suspend fun createDatabase(database: String): Unit =
        query("CREATE DATABASE \"$database\"")

    override suspend fun dropDatabase(database: String): Unit =
        query("DROP DATABASE \"$database\"")

    override suspend fun getDatabases(): List<String> {
        val response = query<JsonNode>("SHOW DATABASES")
        val result = response["results"].first()
        val series = result["series"].first()

        return series["values"].map { it.first().asText() }.toList()
    }

    override suspend fun write(database: String, point: Point) {
    }

    private fun <T: Any>logIt(method: String, any: T) {
        logger.info("$method - $any")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(InfluxService::class.java)
    }
}