package cn.xdwanj.gsgm.cli.controller

import java.io.File

interface RemoveController {
  suspend fun removeAction(libraryPathList: List<File>, gsgmIdList: List<Long>): Int
}