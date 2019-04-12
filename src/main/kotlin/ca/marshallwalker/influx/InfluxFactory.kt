package ca.marshallwalker.influx

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import kotlin.reflect.KProperty

class InfluxFactory(
    private val objectMapper: ObjectMapper = jacksonObjectMapper(),
    private val httpClient: HttpClient = HttpClient(Apache),
    private val url: String = "http://localhost:8086",
    private val username: String = "",
    private val password: String = "") {

    operator fun getValue(nothing: Nothing?, property: KProperty<*>) =
        InfluxClient(objectMapper, httpClient, url, username, password)
}