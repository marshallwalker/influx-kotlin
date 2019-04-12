package ca.marshallwalker.influx

interface LineProtocol {

    fun toLineProtocol(): String
}