package cn.xdwanj.gsgm.cli.controller

import java.io.File

interface CheckController {
  /**
   * check command 服务
   *
   * @param isLibrary 是否是游戏库
   * @param gamePathList 游戏路径列表
   * @return 状态码
   */
  suspend fun checkAction(isLibrary: Boolean, gamePathList: List<File>): Int
}