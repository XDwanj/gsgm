package cn.xdwanj.gsgm.data.enum

import com.fasterxml.jackson.annotation.JsonValue


/**
 * 当地时区的字符编码
 *
 * @property value 具体编码名称
 */
enum class LocaleCharSet(
  @get:JsonValue
  val value: String,
) {
  Japen_UTF_8("ja_JP.UTF-8"),
  Japen_EUC_JP("ja_JP.EUC-JP"),
  China_UTF_8("zh_CN.UTF-8"),
  China_GBK("zh_CN.GBK"),
  China_GB18030("zh_CN.GB18030"),
  ;


}