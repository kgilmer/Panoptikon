# Panoptikon

Console status bar for command-line Kotlin programs.

[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

### Example

```kotlin
fun main(args: Array<String>) {
    val statusBar = Panoptikon("Counter Example")
    var count: Int by statusBar.field()

    for (i in 0..30) {
        count = i
        statusBar.draw()
        Thread.sleep(500)
    }    

    statusBar.finish()
}
```

### Status
* Experimental, YMMV 

### Features

* Property Delegate style API (inspired by [kotlinx-cli](https://github.com/Kotlin/kotlinx.cli)) minimizes boilerplate.
* Threadless and threaded update models supported. 
* A sweet ASCII spinner is built in.

`kotlinx.cli` can be used to create user-friendly and flexible command-line interfaces
for Kotlin/JVM, Kotlin/Native, and any other Kotlin console applications.
Program defines what arguments are expected.
`kotlinx.cli` will figure out how to parse those, reporting errors if the program arguments are invalid,
and also generate help and usage messages as well.

### More Examples

#### Using a thread to update the status bar:

```kotlin
fun main(args: Array<String>) {
    val statusBar = Panoptikon("Counter Example")
    var count: Int by statusBar.field()

    Thread(statusBar).start()

    statusBar.use {
        for (i in 0..5) {
            count = i
            Thread.sleep(1500)
        }
    }
}
```

#### Multiple fields and scrolling lines:
```kotlin
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
```

