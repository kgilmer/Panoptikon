package org.kgilmer.panoptikon.examples

import org.kgilmer.panoptikon.Panoptikon
import java.util.*
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    val statusBar = Panoptikon("title", refreshDelayMillis = 250)

    var action: String by statusBar.field()
    var counter: Int by statusBar.field()
    var message: String by statusBar.field()

    Thread(statusBar).start()

    statusBar.use {
        // Wait some time before "data" comes back.
        Thread.sleep(2000)

        val measurements = mutableListOf<Float>()

        // Get the data
        val totalTime = measureTimeMillis {
            collectMeasurements(30).forEach { (index, measurement) ->
                measurements.add(measurement)
                counter = index
                message = "Reading sample $index at ${System.currentTimeMillis()}"
                action = when (index) {
                    in 0..2 -> "Calibrating"
                    in 9..18 -> "Sampling"
                    else -> "Finishing"
                }

                statusBar.println("Measured $measurement")

                Thread.sleep(500)
            }
        }

        // Summarize
        it.completionMessage =
                { "Collected ${measurements.size} measurements in $totalTime millis with average of ${measurements.average()}" }
    }
}

// Simulated sampling of some value
fun collectMeasurements(count: Int): Sequence<Pair<Int, Float>> =
        0.rangeTo(count).map {
            val rng = Random()
            Thread.sleep(rng.nextInt(200) + 200L)
            Pair(it, rng.nextFloat())
        }.toList().asSequence()