package cn.xdwanj.gsgm.cli.controller

import java.io.File

interface SearchController {

  suspend fun searchAction(keyword: String, libraryPathList: List<File>): Int
}