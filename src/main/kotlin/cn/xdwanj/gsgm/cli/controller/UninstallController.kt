package cn.xdwanj.gsgm.cli.controller

import java.io.File

interface UninstallController {
  suspend fun uninstallActionByGsgmIDList(gsgmIdList: List<Long>): Int

  suspend fun uninstallActionByLibraryList(libraryPathList: List<File>): Int
}