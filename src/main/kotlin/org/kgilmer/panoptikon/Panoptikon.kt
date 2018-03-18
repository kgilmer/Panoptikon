package org.kgilmer.panoptikon

import java.io.Closeable
import java.io.PrintStream
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class Panoptikon(
        private val title: String? = null,
        private val printStream: PrintStream = System.out,
        private val spinnerSequence: String? = "-\\|/",
        private val refreshDelayMillis: Long = 500L,
        var completionMessage: () -> String? = { "\n" }): Closeable, Runnable {

    private val properties: MutableMap<String, Any> = mutableMapOf()
    private val lines: MutableList<CharSequence> = CopyOnWriteArrayList()

    private var running = false

    private var animationIndex = 0

    fun draw() {
        if (lines.isNotEmpty()) {
            printStream.print("\r")
            lines.forEach {
                printStream.print("$it")
            }
            printStream.println()
            lines.clear()
        }

        StringBuilder().apply {
            append('\r')

            spinnerSequence?.let { append("${spinnerSequence[animationIndex % spinnerSequence.length]} ") }
            title?.let { append("$it ") }

            properties.keys.forEach {
                append("$it: ${properties[it]} ")
            }

            printStream.print(toString())
        }

        animationIndex++
    }

    override fun run() {
        running = true
        while (running) {
            draw()
            Thread.sleep(refreshDelayMillis)
        }

        finish()
    }

    override fun close() {
        running = false
    }

    fun finish() {
        completionMessage()?.let { printStream.print("\r$it") }
    }

    fun println(message: CharSequence) {
        if (running) {
            lines.add(message)
        } else {
            System.out.println(message)
        }
    }

    fun <T : Any> field(): ReadWriteProperty<Nothing?, T> {

        return object : ReadWriteProperty<Nothing?, T> {
            override fun getValue(thisRef: Nothing?, property: KProperty<*>): T {
                return properties[property.name] as T
            }

            override fun setValue(thisRef: Nothing?, property: KProperty<*>, value: T) {
                properties[property.name] = value
            }
        }
    }
}


