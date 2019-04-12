package ca.marshallwalker.influx

data class Pong(
    val version: String,
    val responseTime: Long
)