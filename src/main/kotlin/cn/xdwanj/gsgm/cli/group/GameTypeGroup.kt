package cn.xdwanj.gsgm.cli.group

import picocli.CommandLine.Option

class GameTypeGroup {

  @Option(
    names = ["--linux-all"],
    description = ["是否全是 Linux 游戏"],
    required = true,
  )
  var isLinuxAll: Boolean = false

  @Option(
    names = ["--windows-all"],
    description = ["是否全是 Windows 游戏"],
    required = true,
  )
  var isWindowsAll: Boolean = false

  @Option(
    names = ["--mixed"],
    description = ["混合游戏"],
    required = true,
  )
  var isMixed: Boolean = false
}