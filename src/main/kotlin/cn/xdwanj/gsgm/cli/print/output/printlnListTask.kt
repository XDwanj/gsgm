package cn.xdwanj.gsgm.cli.print.output

import cn.xdwanj.gsgm.cli.print.GsgmPrinter

/**
 *
 *
 * @param heading
 * @param msgList
 * @param spaceCount
 * @param spaceValue
 */
fun GsgmPrinter.printlnListTask(
  headerSign: String = "==>",
  heading: String,
  msgSign: String = "->",
  msgList: List<String>,
  spaceCount: Int = 2,
  spaceValue: String = " ",
) {
  val space = StringBuilder().apply {
    repeat(spaceCount) {
      this.append(spaceValue)
    }
  }.toString()
  println("$headerSign $heading")
  msgList.forEach { println("$space$msgSign $it") }
}

fun GsgmPrinter.printSingleTask(
  msg: String,
  msgSign: String = "==>",
) {
  println("$msgSign $msg")
}


