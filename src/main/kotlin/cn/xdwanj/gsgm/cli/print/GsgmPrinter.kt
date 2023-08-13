package cn.xdwanj.gsgm.cli.print

import cn.xdwanj.kcolor.AnsiFormat
import cn.xdwanj.kcolor.AttrTemplate.redText
import cn.xdwanj.kcolor.AttrTemplate.yellowText

object GsgmPrinter {

  val globalLevel: PrintLevel = PrintLevel.WARN
}

enum class PrintLevel(
  val messageHeader: String,
  val format: AnsiFormat,
) {
  ERROR("error", AnsiFormat(redText)),
  WARN("warn", AnsiFormat(yellowText)),
  INFO("info", AnsiFormat())
}