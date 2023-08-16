package cn.xdwanj.gsgm.data.enum

/**
 * lutris 中各种图片的不同标准
 *
 * @property width 宽度
 * @property height 高度
 */
enum class LutrisImageStandard(val width: Int, val height: Int) {
  /**
   * 尺寸: 600x900 | 比例: 2:3
   *
   */
  // Coverart(600, 900),
  Coverart(264, 352),

  /**
   * 尺寸: 184x69 | 比例: 8:3
   *
   */
  Banner(184, 69),

  /**
   * 尺寸: 128x128 256x256 | 比例: 1:1
   *
   */
  Icon(128, 128)
}