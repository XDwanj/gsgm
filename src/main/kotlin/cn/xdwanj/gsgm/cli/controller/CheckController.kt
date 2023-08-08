package cn.xdwanj.gsgm.cli.controller

import java.io.File

interface CheckController {
  /**
   * check command 服务
   *
   * @param isLibrary
   * @param gamePathList
   * @return
   */
  suspend fun checkAction(isLibrary: Boolean, gamePathList: List<File>): Int
}