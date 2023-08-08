package cn.xdwanj.kcolor

object AttrTemplate {
  // 效果

  /**
   * 无效果。恢复终端的默认格式。
   */
  val none: Attribute
    get() = SimpleAttribute("")

  /**
   * 清除任何格式。恢复终端的默认格式。
   *
   * @return 清除格式的属性。
   */
  val clear: Attribute get() = SimpleAttribute("0")


  /**
   * 加粗效果。
   *
   * @return 加粗的属性。
   */
  val bold: Attribute get() = SimpleAttribute("1")


  /**
   * 加粗效果的别名。
   *
   * @return 加粗的属性。
   */
  val saturated: Attribute get() = SimpleAttribute("1")


  /**
   * 暗淡效果。
   *
   * @return 暗淡的属性。
   */
  val dim: Attribute get() = SimpleAttribute("2")


  /**
   * 暗淡效果的别名。
   *
   * @return 暗淡的属性。
   */
  val desaturated: Attribute get() = SimpleAttribute("2")


  /**
   * 斜体效果。
   *
   * @return 斜体的属性。
   */
  val italic: Attribute get() = SimpleAttribute("3")


  /**
   * 下划线效果。
   *
   * @return 下划线的属性。
   */
  val underline: Attribute get() = SimpleAttribute("4")


  /**
   * 缓慢闪烁效果。
   *
   * @return 缓慢闪烁的属性。
   */
  val slowBlink: Attribute get() = SimpleAttribute("5")


  /**
   * 快速闪烁效果。
   *
   * @return 快速闪烁的属性。
   */
  val rapidBlink: Attribute get() = SimpleAttribute("6")


  /**
   * 反转（前景色和背景色互换）效果。
   *
   * @return 反转的属性。
   */
  val reverse: Attribute get() = SimpleAttribute("7")


  /**
   * 隐藏效果。
   *
   * @return 隐藏的属性。
   */
  val hidden: Attribute get() = SimpleAttribute("8")


  /**
   * 删除线效果。
   *
   * @return 删除线的属性。
   */
  val strikethrough: Attribute get() = SimpleAttribute("9")


  /**
   * 带框效果。
   *
   * @return 带框的属性。
   */
  val framed: Attribute get() = SimpleAttribute("51")


  /**
   * 环绕效果。
   *
   * @return 环绕的属性。
   */
  val encircled: Attribute get() = SimpleAttribute("52")


  /**
   * 上划线效果。
   *
   * @return 上划线的属性。
   */
  val overLined: Attribute get() = SimpleAttribute("53")


  // 颜色（前景）

  /**
   * 黑色文本颜色。
   *
   * @return 黑色文本的属性。
   */
  val blackText: Attribute get() = SimpleAttribute("30")


  /**
   * 红色文本颜色。
   *
   * @return 红色文本的属性。
   */
  val redText: Attribute get() = SimpleAttribute("31")


  /**
   * 绿色文本颜色。
   *
   * @return 绿色文本的属性。
   */
  val greenText: Attribute get() = SimpleAttribute("32")


  /**
   * 黄色文本颜色。
   *
   * @return 黄色文本的属性。
   */
  val yellowText: Attribute get() = SimpleAttribute("33")


  /**
   * 蓝色文本颜色。
   *
   * @return 蓝色文本的属性。
   */
  val blueText: Attribute get() = SimpleAttribute("34")


  /**
   * 紫色文本颜色。
   *
   * @return 紫色文本的属性。
   */
  val purpleText: Attribute get() = SimpleAttribute("35")


  /**
   * 青色文本颜色。
   *
   * @return 青色文本的属性。
   */
  val cyanText: Attribute get() = SimpleAttribute("36")


  /**
   * 白色文本颜色。
   *
   * @return 白色文本的属性。
   */
  val whiteText: Attribute get() = SimpleAttribute("37")


  // 颜色（背景）

  /**
   * 黑色背景颜色。
   *
   * @return 黑色背景的属性。
   */
  val blackBack: Attribute get() = SimpleAttribute("40")


  /**
   * 红色背景颜色。
   *
   * @return 红色背景的属性。
   */
  val redBack: Attribute get() = SimpleAttribute("41")


  /**
   * 绿色背景颜色。
   *
   * @return 绿色背景的属性。
   */
  val greenBack: Attribute get() = SimpleAttribute("42")


  /**
   * 黄色背景颜色。
   *
   * @return 黄色背景的属性。
   */
  val yellowBack: Attribute get() = SimpleAttribute("43")


  /**
   * 蓝色背景颜色。
   *
   * @return 蓝色背景的属性。
   */
  val blueBack: Attribute get() = SimpleAttribute("44")


  /**
   * 紫色背景颜色。
   *
   * @return 紫色背景的属性。
   */
  val purpleBack: Attribute get() = SimpleAttribute("45")


  /**
   * 青色背景颜色。
   *
   * @return 青色背景的属性。
   */
  val cyanBack: Attribute get() = SimpleAttribute("46")


  /**
   * 白色背景颜色。
   *
   * @return 白色背景的属性。
   */
  val whiteBack: Attribute get() = SimpleAttribute("47")


  // 明亮的颜色（前景）

  /**
   * 明亮的黑色文本颜色。
   *
   * @return 明亮的黑色文本的属性。
   */
  val brightBlackText: Attribute get() = SimpleAttribute("90")


  /**
   * 明亮的红色文本颜色。
   *
   * @return 明亮的红色文本的属性。
   */
  val brightRedText: Attribute get() = SimpleAttribute("91")


  /**
   * 明亮的绿色文本颜色。
   *
   * @return 明亮的绿色文本的属性。
   */
  val brightGreenText: Attribute get() = SimpleAttribute("92")


  /**
   * 明亮的黄色文本颜色。
   *
   * @return 明亮的黄色文本的属性。
   */
  val brightYellowText: Attribute get() = SimpleAttribute("93")


  /**
   * 明亮的蓝色文本颜色。
   *
   * @return 明亮的蓝色文本的属性。
   */
  val brightBlueText: Attribute get() = SimpleAttribute("94")


  /**
   * 明亮的紫色文本颜色。
   *
   * @return 明亮的紫色文本的属性。
   */
  val brightPurpleText: Attribute get() = SimpleAttribute("95")


  /**
   * 明亮的青色文本颜色。
   *
   * @return 明亮的青色文本的属性。
   */
  val brightCyanText: Attribute get() = SimpleAttribute("96")


  /**
   * 明亮的白色文本颜色。
   *
   * @return 明亮的白色文本的属性。
   */
  val brightWhiteText: Attribute get() = SimpleAttribute("97")


  // 明亮的颜色（背景）

  /**
   * 明亮的黑色背景颜色。
   *
   * @return 明亮的黑色背景的属性。
   */
  val brightBlackBack: Attribute get() = SimpleAttribute("100")


  /**
   * 明亮的红色背景颜色。
   *
   * @return 明亮的红色背景的属性。
   */
  val brightRedBack: Attribute get() = SimpleAttribute("101")


  /**
   * 明亮的绿色背景颜色。
   *
   * @return 明亮的绿色背景的属性。
   */
  val brightGreenBack: Attribute get() = SimpleAttribute("102")


  /**
   * 明亮的黄色背景颜色。
   *
   * @return 明亮的黄色背景的属性。
   */
  val brightYellowBack: Attribute get() = SimpleAttribute("103")


  /**
   * 明亮的蓝色背景颜色。
   *
   * @return 明亮的蓝色背景的属性。
   */
  val brightBlueBack: Attribute get() = SimpleAttribute("104")


  /**
   * 明亮的紫色背景颜色。
   *
   * @return 明亮的紫色背景的属性。
   */
  val brightPurpleBack: Attribute get() = SimpleAttribute("105")


  /**
   * 明亮的青色背景颜色。
   *
   * @return 明亮的青色背景的属性。
   */
  val brightCyanBack: Attribute get() = SimpleAttribute("106")


  /**
   * 明亮的白色背景颜色。
   *
   * @return 明亮的白色背景的属性。
   */
  val brightWhiteBack: Attribute get() = SimpleAttribute("107")
}
  
