package cn.xdwanj.gsgm.cli.print.output

import cn.xdwanj.gsgm.cli.print.GsgmPrinter
import cn.xdwanj.kcolor.Ansi
import cn.xdwanj.kcolor.AnsiFormat
import cn.xdwanj.kcolor.AttrTemplate.cyanText

fun GsgmPrinter.printlnWaitTask(
  message: String,
  format: AnsiFormat = AnsiFormat(cyanText),
): String {
  val msg = Ansi.colorize("::", format) + " " + message
  println(msg)
  return msg
}
