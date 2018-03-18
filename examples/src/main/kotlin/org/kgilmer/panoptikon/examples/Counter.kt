package org.kgilmer.panoptikon.examples

import org.kgilmer.panoptikon.Panoptikon

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