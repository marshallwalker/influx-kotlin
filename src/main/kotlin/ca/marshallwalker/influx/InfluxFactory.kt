package ca.marshallwalker.influx

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.auth.Auth
import io.ktor.client.features.auth.providers.BasicAuthConfig
import io.ktor.client.features.auth.providers.basic
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.JsonSerializer
import kotlin.reflect.KProperty

class InfluxFactory(
    private val objectMapper: ObjectMapper = jacksonObjectMapper(),
    private val username: String = "",
    private val password: String = "",
    private val httpClient: HttpClient = createHttpClient(username, password),
    private val url: String = "http://localhost:8086") {

    operator fun getValue(nothing: Nothing?, property: KProperty<*>) =
        InfluxClient(httpClient, url)

    companion object {
        private fun createHttpClient(username: String, password: String) = HttpClient(Apache) {
            install(JsonFeature) {
                //todo apply provided object mapper
                serializer = JacksonSerializer()
            }

            install(Auth) {
                basic {
                    this.username = username
                    this.password = password
                }
            }
        }
    }
}