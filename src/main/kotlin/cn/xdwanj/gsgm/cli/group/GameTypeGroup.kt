package cn.xdwanj.gsgm.cli.group

import picocli.CommandLine.Option

/**
 * 游戏类型选项组
 *
 */
class GameTypeGroup {

  @Option(
    names = ["-lp", "--linux-all"],
    description = ["是否全是 Linux 游戏"],
    required = true,
  )
  var isLinuxAll: Boolean = false

  @Option(
    names = ["-wp", "--windows-all"],
    description = ["是否全是 Windows 游戏"],
    required = true,
  )
  var isWindowsAll: Boolean = false

  @Option(
    names = ["-mi", "--mix", "--mixed"],
    description = ["混合游戏"],
    required = true,
  )
  var isMixed: Boolean = false
}