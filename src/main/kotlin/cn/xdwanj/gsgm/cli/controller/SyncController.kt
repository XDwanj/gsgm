package cn.xdwanj.gsgm.cli.controller

import java.io.File

interface SyncController {
  suspend fun syncActionGTL(libraryPathList: List<File>): Int
  suspend fun syncActionGTLByForce(libraryPathList: List<File>): Int
  suspend fun syncActionLTG(libraryPathList: List<File>): Int

}