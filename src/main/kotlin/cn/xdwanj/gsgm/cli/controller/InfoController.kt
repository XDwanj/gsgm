package cn.xdwanj.gsgm.cli.controller

import java.io.File


interface InfoController {
  /**
   * 打印游戏详细信息
   *
   * @param libraryPathList
   * @param gsgmIdList
   * @return
   */
  suspend fun infoAction(libraryPathList: List<File>?, gsgmIdList: List<Long>): Int
}