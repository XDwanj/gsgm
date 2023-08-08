package cn.xdwanj.kcolor

class AnsiFormat(
  vararg attributes: Attribute
) : MutableList<Attribute> by mutableListOf(*attributes) {

  fun format(text: String): String {
    return Ansi.colorize(text, *this.toTypedArray())
  }
}