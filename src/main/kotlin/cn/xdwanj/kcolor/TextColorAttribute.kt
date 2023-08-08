package cn.xdwanj.kcolor

class TextColorAttribute : ColorAttribute {

  constructor(colorNumber: Int) : super(colorNumber)
  constructor(r: Int, g: Int, b: Int) : super(r, g, b)

  override fun getColorAnsiPrefix(): String {
    val ansi8bitColorPrefix = "38;5;"
    val ansiTrueColorPrefix = "38;2;"

    return if (isTrueColor())
      ansiTrueColorPrefix
    else
      ansi8bitColorPrefix

  }
}