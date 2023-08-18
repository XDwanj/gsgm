package cn.xdwanj.gsgm.base

import org.dromara.hutool.core.io.file.FileUtil
import org.dromara.hutool.core.util.SystemUtil

/**
 * Gsgm 相关配置信息
 */
object GsgmGlobalSettings {

  private val localPath = "${SystemUtil.getUserHomePath()}/.local/share/gsgm".also { FileUtil.mkdir(it) }
  private val configPath = "${SystemUtil.getUserHomePath()}/.config/gsgm".also { FileUtil.mkdir(it) }
  private val cachePath = "${SystemUtil.getUserHomePath()}/.cache/gsgm".also { FileUtil.mkdir(it) }

  val picTemp = "$cachePath/pic_temp".also { FileUtil.mkdir(it) }
  val dbPath = "$localPath/gsgm.db"
}

object GsgmFileName {
  const val INFO = "info.json"
  const val SETTING = "setting.json"
  const val HISTORY = "history.json"
  const val GSGM_DIR = ".gsgm"
  const val COVER_NAME = "cover"
  const val IS_GROUP = ".is-group"
}
