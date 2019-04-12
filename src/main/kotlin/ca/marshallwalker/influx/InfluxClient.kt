package ca.marshallwalker.influx

import ca.marshallwalker.influx.model.Point

class InfluxClient internal constructor(
    repository: InfluxRepository): InfluxRepository by repository {

    lateinit var database: String

    suspend fun write(point: Point) =
            write(database, point)
}