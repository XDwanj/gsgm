package cn.xdwanj.gsgm.cli.controller

import java.io.File

interface InstallController {
  suspend fun installActionByLibrary(libraryPathList: List<File>): Int
  suspend fun installActionLibraryByForce(libraryPathList: List<File>): Int
}