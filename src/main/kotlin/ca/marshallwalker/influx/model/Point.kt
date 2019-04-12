package ca.marshallwalker.influx.model

import ca.marshallwalker.influx.LineProtocol
import java.util.concurrent.TimeUnit

data class Point(val measurement: String) : LineProtocol {

    private val tags = mutableMapOf<String, String>()
    private val fields = mutableMapOf<String, Any>()

    private var time: Long = 0L
    private lateinit var precision: TimeUnit

    fun time(time: Long, precision: TimeUnit) {
        this.time = time
        this.precision = precision
    }

    fun tag(key: String, value: String) {
        tags[key] = value
    }

    private fun backingField(key: String, value: Any) {
        if (value is Number) {
            fields[key] = value.toFloat()
            return
        }

        fields[key] = value
    }

    fun field(key: String, value: Float) = backingField(key, value)

    fun field(key: String, value: Int) = backingField(key, value)

    fun field(key: String, value: String) = backingField(key, value)

    fun field(key: String, value: Boolean) = backingField(key, value)

    override fun toLineProtocol() = buildString {
        append(measurement)

        if (tags.isNotEmpty()) {
            append(',')
            append(tags.map { "${it.key}=${it.value}" }.joinToString(","))
        }

        if(fields.isNotEmpty()) {
            append(" ")

            append(fields.map {"${it.key}=${it.value}" }.joinToString(","))
        }

        append(" ${TimeUnit.NANOSECONDS.convert(time, precision)}")
    }
}