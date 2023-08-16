package cn.xdwanj.gsgm.cli.controller

interface ListController {
  suspend fun listActionByKeyword(keyword: String): Int
}