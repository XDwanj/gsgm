package cn.xdwanj.kcolor

class BackColorAttribute : ColorAttribute {

  constructor(colorNumber: Int) : super(colorNumber)

  constructor(r: Int, g: Int, b: Int) : super(r, g, b)

  override fun getColorAnsiPrefix(): String {
    val ansi8bitColorPrefix = "48;5;"
    val ansiTrueColorPrefix = "48;2;"

    return if (isTrueColor())
      ansiTrueColorPrefix
    else
      ansi8bitColorPrefix

  }
}