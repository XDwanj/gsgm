package cn.xdwanj.kcolor

import java.util.concurrent.atomic.AtomicBoolean

object Ansi {

  private var state: AtomicBoolean = AtomicBoolean(true)

  private const val ESC = (27).toChar() // 用于开始 ANSI 代码的转义字符

  val NEWLINE = System.getProperty("line.separator") ?: "\n"

  /**
   * 每个 Ansi 转义码都以此前缀开头。
   */
  const val PREFIX = "$ESC["

  /**
   * 两个选项必须用此分隔符分隔。
   */
  const val SEPARATOR = ";"

  /**
   * 每个 Ansi 转义码必须以此 POSTFIX 结尾。
   */
  const val POSTFIX = "m"

  /**
   * 重置为终端默认格式的 Ansi 代码的简写。
   */
  val RESET = PREFIX + AttrTemplate.clear + POSTFIX

  fun disable() = state.set(false)

  fun enable() = state.set(true)

  private fun generateCode(vararg attributes: Attribute): String {
    val builder = StringBuilder()
    builder.append(PREFIX)
    for (option in attributes) {
      val code = option.toString()
      if (code.isEmpty()) continue
      builder.append(code)
      builder.append(SEPARATOR)
    }
    builder.append(POSTFIX)

    // 因为代码不能以 SEPARATOR 结尾
    return builder.toString().replace(SEPARATOR + POSTFIX, POSTFIX)
  }

  fun colorize(text: String, ansiCode: String): String {
    if (state.get().not()) return text

    val output = StringBuilder()
    /*
     * 每个格式化行应该：
     * 1) 从设置格式的代码开始
     * 2) 以重置格式的代码结尾
     * 这可以防止“溢出”格式到其他独立的打印，这
     * 当背景有色时很明显。
     */
    output.append(ansiCode)

    // 每行需要结束当前格式（RESET）并在下一行开始。
    // 这可以避免溢出，即。没有文本但格式化背景的长行
    val enclosedFormatting = text.replace(NEWLINE, RESET + NEWLINE + ansiCode)
    output.append(enclosedFormatting)
    output.append(RESET)
    return output.toString()
  }

  fun colorize(text: String, vararg attributes: Attribute): String {
    if (state.get().not()) return text

    val ansiCode = generateCode(*attributes)
    return colorize(text, ansiCode)
  }

  fun colorize(text: String, ansiFormat: AnsiFormat): String {
    return colorize(text, *ansiFormat.toTypedArray())
  }
}