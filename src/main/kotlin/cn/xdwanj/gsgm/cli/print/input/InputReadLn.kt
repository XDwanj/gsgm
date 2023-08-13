package cn.xdwanj.gsgm.cli.print.input

import cn.xdwanj.gsgm.cli.print.GsgmPrinter

inline fun <T> GsgmPrinter.inputReadLn(
  inputTip: String,
  errorTip: String? = null,
  condition: (String) -> Boolean,
  converter: (String) -> T,
): T {
  val result: T
  do {
    print(inputTip)
    val inputText = readln().trim()

    if (inputText.isBlank()) {
      continue
    }

    val isSuccess = try {
      condition(inputText)
    } catch (e: Exception) {
      println(e)
      continue
    }

    if (isSuccess) {
      result = converter(inputText)
      break
    }
    if (errorTip.isNullOrEmpty().not()) {
      println(errorTip)
    }
    continue
  } while (true)
  return result
}

