package cn.xdwanj.gsgm.cli.print.output

import cn.xdwanj.gsgm.cli.print.GsgmPrinter
import cn.xdwanj.gsgm.cli.print.PrintLevel
import cn.xdwanj.kcolor.Ansi

fun GsgmPrinter.printlnLevelTask(
  message: String,
  level: PrintLevel = this.globalLevel,
) {
  val msg = Ansi.colorize(level.messageHeader, level.format) + ": " + message
  println(msg)
}