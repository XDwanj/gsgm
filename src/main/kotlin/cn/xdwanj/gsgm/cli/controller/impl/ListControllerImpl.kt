package cn.xdwanj.gsgm.cli.controller.impl

import cn.xdwanj.gsgm.cli.controller.ListController
import cn.xdwanj.gsgm.cli.print.GsgmPrinter
import cn.xdwanj.gsgm.cli.print.output.printlnGsgmGameDesc
import cn.xdwanj.gsgm.data.mapper.LutrisGameMapper
import cn.xdwanj.gsgm.service.LibraryService
import cn.xdwanj.gsgm.service.LutrisService
import cn.xdwanj.gsgm.util.extensions.queryChain
import kotlinx.coroutines.coroutineScope
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ListControllerImpl(
  private val lutrisService: LutrisService,
  private val lutrisGameMapper: LutrisGameMapper,
  private val libraryService: LibraryService,
) : ListController {

  private val logger = LoggerFactory.getLogger(ListControllerImpl::class.java)

  override suspend fun listAction(): Int = coroutineScope {
    val lutrisGameList = lutrisGameMapper.queryChain().list()

    lutrisGameList
      .sortedBy { it.id }
      .reversed()
      .forEach {
        val wrapper = try {
          libraryService.getGsgmWrapperByLutrisGame(it)
        } catch (e: Exception) {
          logger.error("", e)
          return@coroutineScope 1
        }
        GsgmPrinter.printlnGsgmGameDesc(wrapper)
      }

    0
  }
}