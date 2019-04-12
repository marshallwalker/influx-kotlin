package ca.marshallwalker.influx

import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache

class InfluxConfig {
    var httpClient: HttpClient = HttpClient(Apache)

    var url: String = "http://localhost:8086"

    var username: String = ""
    var password: String = ""

    var compress = false
}