package cn.xdwanj.gsgm.cli.print.output

import cn.xdwanj.gsgm.cli.print.GsgmPrinter
import cn.xdwanj.kcolor.Ansi
import cn.xdwanj.kcolor.AnsiFormat

fun GsgmPrinter.printlnLine(lineLength: Int = 30, mark: String = "-", ansiFormat: AnsiFormat? = null): String {
  val builder = StringBuilder()
  for (i in 1..lineLength) {
    builder.append(mark)
  }
  val str = ansiFormat?.let {
    Ansi.colorize(builder.toString(), ansiFormat)
  } ?: builder.toString()

  println(str)

  return str
}