package cn.xdwanj.gsgm.cli.group

import picocli.CommandLine.Option
import java.io.File

class UninstallMedium {

  @Option(
    names = ["-lib", "--library-path"],
    description = ["游戏库地址，卸载整个游戏库"],
    required = true,
    arity = "1..*",
  )
  var libraryPath: List<File> = emptyList()


  @Option(
    names = ["-id", "--gsgm-id"],
    description = ["Gsgm ID，删除 Lutris 中的相应游戏数据"],
    required = true,
    arity = "1..*",
  )
  var gsgmIdList: List<Long> = emptyList()

}

