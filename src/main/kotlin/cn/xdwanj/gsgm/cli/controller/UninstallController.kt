package cn.xdwanj.gsgm.cli.controller

import java.io.File

interface UninstallController {
  suspend fun removeActionByGsgmId(libraryPathList: List<File>, gsgmIdList: List<Long>): Int
  suspend fun removeActionByLibrary(libraryPathList: List<File>): Int
}