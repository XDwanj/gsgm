package cn.xdwanj.gsgm.data.script

import cn.xdwanj.gsgm.util.YamlUtils

/**
 * lutris 运行时脚本
 */
// data class LutrisRunScript(
//   var name: String,
//   var slug: String,
//   var gameExe: String,
//   var gamePrefix: String,
//   var platform: Platform = Platform.Windows,
//   var localeCharSet: LocaleCharSet = LocaleCharSet.China_UTF_8,
// )

// fun LutrisRunScript.toYaml(): String {
//   return if (platform == Platform.Windows) {
//     """
//     slug: "$slug"
//     game_slug: "$slug"
//     game:
//       exe: "$gameExe"
//       prefix: "$gamePrefix"
//     system:
//       locale: "${localeCharSet.value}"
//     ${platform.runner}: {}
//     """.trimIndent()
//   } else {
//     """
//     slug: "$slug"
//     game_slug: "$slug"
//     game:
//       exe: "$gameExe"
//     ${platform.runner}: {}
//     system:
//       locale: "${localeCharSet.value}"
//     """.trimIndent()
//   }
// }


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