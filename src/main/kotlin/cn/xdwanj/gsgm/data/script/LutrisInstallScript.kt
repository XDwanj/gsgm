package cn.xdwanj.gsgm.data.script

/**
 * lutris 安装脚本
 */
// data class LutrisInstallScript(
//   var name: String,
//   var slug: String,
//   var version: String,
//   var runner: String,
//   var gameExe: String,
//   var gamePrefix: String,
//   var platform: Platform = Platform.Windows,
// )


// fun LutrisInstallScript.toYaml(): String {
//   return """
//   name: "$name" # 游戏名称
//   game_slug: "$slug" # 游戏标符
//   version: "$version" # 安装程序版本
//   slug: "$slug" # 安装程序标识符
//   runner: "$runner" # 运行器
//
//   script:
//     game:
//       exe: "$gameExe" # 游戏可执行文件路径
//       prefix: "$gamePrefix" # wine 容器路径
//   """.trimIndent()
// }

data class LutrisInstallScript(
  var name: String? = null,
  var game_slug: String? = null,
  var version: String? = null,
  var slug: String? = null,
  var runner: String? = null,
  var script: GameScript? = null,
) {
  data class GameScript(
    var game: GameDetails? = null,
  )

  data class GameDetails(
    var exe: String? = null,
    var prefix: String? = null,
  )
}

