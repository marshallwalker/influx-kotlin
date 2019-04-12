package ca.marshallwalker.influx


import ca.marshallwalker.influx.feature.Gzip
import io.ktor.client.features.auth.Auth
import io.ktor.client.features.auth.providers.basic
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import kotlin.reflect.KProperty

class InfluxClientFactory(block: InfluxConfig.() -> Unit = {}) {
    private val config = InfluxConfig().apply(block)

    private fun createHttpClient() = config.httpClient.config {
        install(JsonFeature, ::configureJson)

        if(config.username.isNotEmpty() && config.password.isNotEmpty()) {
            install(Auth, ::configureAuth)
        }

        if(config.compress) {
            install(Gzip)
        }
    }

    private fun configureJson(config: JsonFeature.Config) {
        config.serializer = JacksonSerializer()
    }

    private fun configureAuth(auth: Auth) = auth.basic {
        username = config.username
        password = config.password
    }

    operator fun getValue(nothing: Nothing?, property: KProperty<*>) =
        InfluxClient(InfluxService(createHttpClient(), config.url))
}