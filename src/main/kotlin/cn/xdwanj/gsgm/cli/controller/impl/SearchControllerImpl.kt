package cn.xdwanj.gsgm.cli.controller.impl

import cn.xdwanj.gsgm.cli.controller.SearchController
import cn.xdwanj.gsgm.cli.print.GsgmPrinter
import cn.xdwanj.gsgm.cli.print.PrintLevel
import cn.xdwanj.gsgm.cli.print.output.printlnGsgmGameDesc
import cn.xdwanj.gsgm.cli.print.output.printlnLevelTask
import cn.xdwanj.gsgm.service.LibraryService
import cn.xdwanj.gsgm.util.extensions.toGsgmWrapperList
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
      .map { libraryService.deepGroupFile(it.absolutePath) }
      .flatten()
      .toGsgmWrapperList()

    // search
    val searchList = wrapperList.filter { it.gameFile!!.name.contains(keyword) }

    if (searchList.isEmpty()) {
      println("当前选择的库不存在有此关键字的游戏")
      GsgmPrinter.printlnLevelTask(
        message = "There is no game with this keyword in the currently selected library",
        level = PrintLevel.WARN
      )
      return@coroutineScope 0
    }

    println("related games are as follows")
    searchList.forEach {
      GsgmPrinter.printlnGsgmGameDesc(it)
    }

    0
  }
}