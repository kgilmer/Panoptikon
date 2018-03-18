package org.kgilmer.panoptikon.examples

import org.kgilmer.panoptikon.Panoptikon

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