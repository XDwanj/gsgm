package cn.xdwanj.gsgm.data.script

import cn.xdwanj.gsgm.util.YamlUtils

/**
 * lutris 运行时脚本
 */
data class LutrisRunScript(
  var slug: String? = null,
  var game_slug: String? = null,
  var game: GameDetails? = null,
  var system: SystemDetails? = null,
  var wine: WineDetails? = null,
) {
  data class GameDetails(
    var exe: String? = null,
    var prefix: String? = null,
  )

  data class SystemDetails(
    var locale: String? = null,
  )

  data class WineDetails(
    var version: String? = null,
  )
}

fun LutrisRunScript.toYaml(): String {
  return YamlUtils.toYaml(this)
}