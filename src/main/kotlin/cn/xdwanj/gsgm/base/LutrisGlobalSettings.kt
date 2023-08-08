package cn.xdwanj.gsgm.base

import org.dromara.hutool.core.io.file.FileUtil
import org.dromara.hutool.core.util.SystemUtil

/**
 * Lutris 的相关设置信息
 */
object LutrisGlobalSettings {
  private val configPath = "${SystemUtil.getUserHomePath()}/.config/lutris"
  private val cachePath = "${SystemUtil.getUserHomePath()}/.cache/lutris"
  private val localPath = "${SystemUtil.getUserHomePath()}/.local/share/lutris"

  val pgaDbPath = "$localPath/pga.db"
  val gameScriptPath = "$configPath/games".also { FileUtil.mkdir(it) }
  val coverartPath = "$cachePath/coverart".also { FileUtil.mkdir(it) }
  val bannerPath = "$cachePath/banners".also { FileUtil.mkdir(it) }
  val iconPath = "${SystemUtil.getUserHomePath()}/.local/share/icons/hicolor/128x128/apps".also { FileUtil.mkdir(it) }
}

object LutrisExtName {
  const val COVERART_SUFFIX = "jpg"
  const val BANNER_SUFFIX = "jpg"
  const val ICON_SUFFIX = "png"
  const val SCRIPT_SUFFIX = "yml"
}

object LutrisConstant {
  /**
   * lutris slug 前缀
   */
  const val SLUG_PREFIX = "gsgm-"

  const val SCRIPT_PREFIX = "gsgm-"
  const val BANNER_PREFIX = "gsgm-"
  const val COVERART_PREFIX = "gsgm-"
  const val ICON_PREFIX = "lutris_gsgm-"
}