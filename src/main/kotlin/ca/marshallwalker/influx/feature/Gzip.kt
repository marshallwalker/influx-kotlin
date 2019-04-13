package ca.marshallwalker.influx.feature

import io.ktor.client.HttpClient
import io.ktor.client.call.HttpClientCall
import io.ktor.client.features.HttpClientFeature
import io.ktor.client.features.observer.wrapWithContent
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.response.HttpReceivePipeline
import io.ktor.client.response.HttpResponse
import io.ktor.client.response.readBytes
import io.ktor.util.AttributeKey
import io.ktor.util.pipeline.PipelineContext
import kotlinx.coroutines.io.ByteReadChannel
import java.nio.charset.StandardCharsets.UTF_8
import java.util.zip.GZIPInputStream

class Gzip {

    companion object Feature : HttpClientFeature<Unit, Gzip> {
        override val key: AttributeKey<Gzip> = AttributeKey("Gzip")

        override fun prepare(block: Unit.() -> Unit) = Gzip()

        override fun install(feature: Gzip, scope: HttpClient) {
            scope.receivePipeline.intercept(HttpReceivePipeline.Before) { unzip(this) }
            scope.requestPipeline.intercept(HttpRequestPipeline.Render) { zip(this) }
        }

        private suspend fun unzip(context: PipelineContext<HttpResponse, HttpClientCall>) {
            val subject = context.subject

            if (subject.headers["Content-Encoding"] != "gzip") {
                return
            }

            val stream = subject.readBytes().inputStream()
            val text = GZIPInputStream(stream).bufferedReader(UTF_8).use { it.readText() }

            val channel = ByteReadChannel(text)
            val newCall = subject.call.wrapWithContent(channel, true)
            context.proceedWith(newCall.response)
        }

        private suspend fun zip(context: PipelineContext<Any, HttpRequestBuilder>) {
            val ctx = context.context
            val subject = context.subject

            if (ctx.headers["Content-Encoding"] != "gzip") {
                return
            }

            //todo gzip outbound content

        }
    }
}
