package cn.xdwanj.gsgm.data.setting

import cn.xdwanj.gsgm.data.enum.LocaleCharSet
import cn.xdwanj.gsgm.data.enum.Platform
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include

@JsonInclude(Include.NON_EMPTY)
data class GsgmSetting(

  /**
   * 运行文件位置
   */
  var executeLocation: String? = null,

  /**
   * wine 容器位置
   */
  var winePrefix: String? = null,

  /**
   * 当地时区编码
   */
  var localeCharSet: LocaleCharSet? = null,

  /**
   * 游戏平台
   */
  var platform: Platform? = null,

  /**
   * 执行命令前缀
   */
  var preCommand: String? = null,

  /**
   * 命令参数
   */
  var argsCommand: String? = null,

  /**
   * 结尾命令
   */
  var endCommand: String? = null,
)
