package cn.xdwanj.gsgm.cli.controller

import java.io.File

interface SyncController {
  suspend fun syncActionLTG(libraryPathList: List<File>): Int

  suspend fun syncActionLTGByForce(libraryPathList: List<File>): Int
}