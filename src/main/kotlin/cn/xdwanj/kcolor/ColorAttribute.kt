package cn.xdwanj.kcolor

abstract class ColorAttribute : Attribute {

  private var _color: List<String>

  /**
   * 构造函数（8 位颜色）。
   *
   * @param colorNumber 代表 8 位颜色的数字 (0-255)。
   */
  constructor(colorNumber: Int) {
    if (colorNumber in 0..255) {
      _color = listOf(colorNumber.toString())
    } else
      throw IllegalArgumentException("Color must be a number inside range [0-255]. Received: $colorNumber")
  }

  /**
   * 构造函数（真彩色）。
   *
   * @param r 代表红色分量的数字（0-255）。
   * @param g 代表绿色分量的数字（0-255）。
   * @param b 代表蓝色分量的数字 (0-255)。
   */
  constructor(r: Int, g: Int, b: Int) {
    if (r in 0..255 && g in 0..255 && b in 0..255) {
      _color = listOf(r.toString(), g.toString(), b.toString())
    } else throw java.lang.IllegalArgumentException(
      String.format("Color components must be a number inside range [0-255]. Received: %d, %d, %d", r, g, b)
    )
  }

  protected fun isTrueColor(): Boolean = _color.count() == 3

  protected abstract fun getColorAnsiPrefix(): String

  protected fun getColorAnsiCode(): String {
    return if (isTrueColor())
      _color[0] + Ansi.SEPARATOR + _color[1] + Ansi.SEPARATOR + _color[2]
    else
      _color[0]
  }

  override fun toString(): String {
    return getColorAnsiPrefix() + getColorAnsiCode()
  }

}