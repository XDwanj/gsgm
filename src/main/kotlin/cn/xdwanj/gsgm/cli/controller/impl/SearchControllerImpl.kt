package cn.xdwanj.gsgm.cli.controller.impl

import cn.xdwanj.gsgm.cli.controller.SearchController
import cn.xdwanj.gsgm.service.LibraryService
import cn.xdwanj.gsgm.util.topfun.printlnGsgmGameDesc
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.File

@Component
class SearchControllerImpl(
  private val libraryService: LibraryService,
) : SearchController {

  private val logger = LoggerFactory.getLogger(SearchControllerImpl::class.java)
  override suspend fun searchAction(keyword: String, libraryPathList: List<File>): Int = coroutineScope {
    val wrapperList = libraryPathList
      .map { libraryService.deepGameFile(it.absolutePath) }
      .flatten()
      .map { async { libraryService.getGsgmWrapperByFile(it) } }
      .awaitAll()

    // search
    val searchList = wrapperList.filter { it.gameFile!!.name.contains(keyword) }

    if (searchList.isEmpty()) {
      println("当前选择的库不存在有此关键字的游戏")
      return@coroutineScope 0
    }

    println("相关游戏如下:")
    searchList.forEach {
      printlnGsgmGameDesc(it)
    }

    0
  }
}