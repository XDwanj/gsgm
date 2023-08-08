package cn.xdwanj.kcolor

object AttrFactor {
  // 复杂颜色
  /**
   * @param colorNumber 代表 8 位颜色的数字 (0-255)。
   * @return 代表 8 位颜色前景的属性。
   */
  fun textColor(colorNumber: Int): Attribute {
    return TextColorAttribute(colorNumber)
  }

  /**
   * @param r 代表红色分量的数字（0-255）。
   * @param g 代表绿色分量的数字（0-255）。
   * @param b 代表蓝色分量的数字 (0-255)。
   * @return 代表具有真实颜色的前景的属性。
   */
  fun textColor(r: Int, g: Int, b: Int): Attribute {
    return TextColorAttribute(r, g, b)
  }

  /**
   * @param colorNumber 代表 8 位颜色的数字 (0-255)。
   * @return 代表 8 位颜色背景的属性。
   */
  fun backColor(colorNumber: Int): Attribute {
    return BackColorAttribute(colorNumber)
  }

  /**
   * @param r 代表红色分量的数字（0-255）。
   * @param g 代表绿色分量的数字（0-255）。
   * @param b 代表蓝色分量的数字 (0-255)。
   * @return 代表真实颜色背景的属性。
   */
  fun backColor(r: Int, g: Int, b: Int): Attribute {
    return BackColorAttribute(r, g, b)
  }
}