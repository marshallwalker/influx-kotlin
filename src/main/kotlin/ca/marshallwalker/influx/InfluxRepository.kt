package ca.marshallwalker.influx

import ca.marshallwalker.influx.model.Point
import ca.marshallwalker.influx.model.Pong

interface InfluxRepository {

    suspend fun ping(): Pong

    suspend fun version(): String

    suspend fun createDatabase(database: String)

    suspend fun dropDatabase(database: String)

    suspend fun getDatabases(): List<String>

    suspend fun write(database: String, point: Point)
}