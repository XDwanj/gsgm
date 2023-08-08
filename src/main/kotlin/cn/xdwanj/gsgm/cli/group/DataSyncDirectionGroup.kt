package cn.xdwanj.gsgm.cli.group

import picocli.CommandLine.Option

class DataSyncDirectionGroup {

  @Option(
    required = false,
    names = ["-gtl", "--gsgm-to-lutris"],
    defaultValue = "false",
    description = ["同步 Gsgm 的数据到 Lutris"]
  )
  var activeGsgmToLutris: Boolean = false

  @Option(
    required = false,
    names = ["-ltg", "--lutris-to-gsgm"],
    defaultValue = "false",
    description = ["同步 Lutris 的数据到 Gsgm"],
  )
  var activeLutrisToGsgm: Boolean = false

}