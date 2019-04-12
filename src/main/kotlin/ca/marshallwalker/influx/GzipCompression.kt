package ca.marshallwalker.influx

import io.ktor.client.HttpClient
import io.ktor.client.call.HttpClientCall
import io.ktor.client.features.*
import io.ktor.client.features.observer.wrapWithContent
import io.ktor.client.request.HttpRequest
import io.ktor.client.response.HttpResponse
import io.ktor.util.AttributeKey
import kotlinx.coroutines.io.ByteReadChannel

class GzipCompression {
    companion object Feature: HttpClientFeature<Unit, GzipCompression> {
        var enabled = false

        override val key: AttributeKey<GzipCompression> = AttributeKey("GzipCompression")

        override fun prepare(block: Unit.() -> Unit): GzipCompression = GzipCompression()

        override fun install(feature: GzipCompression, scope: HttpClient) {
            scope.feature(HttpSend)?.intercept { handleRequest(it) }
        }

        private suspend fun Sender.handleRequest(origin: HttpClientCall): HttpClientCall {
            if(!enabled) {
                return origin
            }

            val originalRequest = origin.request
            val body = originalRequest.content

            if(originalRequest.headers["Content-Encoding"] != null) {
               return origin
            }

            return origin.wrapWithContent(compress(origin.response), false)
        }

        private suspend fun compress(response: HttpResponse): ByteReadChannel {


            return response.content
        }
    }
}