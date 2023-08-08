package cn.xdwanj.kcolor


/**
 * 用直观的名称抽象 ANSI 代码。它将描述（例如 RED_TEXT）与代码（例如 31）进行映射。
 *
 * @see <a href="https://en.wikipedia.org/wiki/ANSI_escape_code#Escape_sequences">维基百科，获取所有可用代码的列表</a>
 * @see <a href="https://stackoverflow.com/questions/4842424/list-of-ansi-color-escape-sequences/33206814#33206814">StackOverflow，获取带有示例的代码列表</a>
 */
abstract class Attribute {
  abstract override fun toString(): String
}




