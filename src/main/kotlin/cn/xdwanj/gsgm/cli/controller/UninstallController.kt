package cn.xdwanj.gsgm.cli.controller

import java.io.File

interface UninstallController {
  suspend fun removeAction(libraryPathList: List<File>, gsgmIdList: List<Long>): Int
}