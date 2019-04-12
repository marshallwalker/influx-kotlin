package ca.marshallwalker.influx.model

data class Pong(
    val version: String,
    val responseTime: Long
)