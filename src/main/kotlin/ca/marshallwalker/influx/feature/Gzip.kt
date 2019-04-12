package ca.marshallwalker.influx.feature

import io.ktor.client.HttpClient
import io.ktor.client.features.HttpClientFeature
import io.ktor.client.features.observer.wrapWithContent
import io.ktor.client.response.HttpReceivePipeline
import io.ktor.client.response.readBytes
import io.ktor.util.AttributeKey
import kotlinx.coroutines.io.ByteReadChannel
import java.nio.charset.StandardCharsets.UTF_8
import java.util.zip.GZIPInputStream

class Gzip {

    companion object Feature : HttpClientFeature<Unit, Gzip> {
        override val key: AttributeKey<Gzip> = AttributeKey("Gzip")

        override fun prepare(block: Unit.() -> Unit) = Gzip()

        override fun install(feature: Gzip, scope: HttpClient) {
            scope.receivePipeline.intercept(HttpReceivePipeline.Before) {
                if (subject.headers["Content-Encoding"] != "gzip") return@intercept

                val stream = subject.readBytes().inputStream()
                val text = GZIPInputStream(stream).bufferedReader(UTF_8).use { it.readText() }

                val channel = ByteReadChannel(text)
                val newCall = subject.call.wrapWithContent(channel, true)
                proceedWith(newCall.response)
            }

            //todo gzip outgoing content
//            scope.requestPipeline.intercept(HttpRequestPipeline.Render) { payload ->
//                if(context.headers["Content-Encoding"] != "gzip") return@intercept
//
//
//            }
        }
    }
}