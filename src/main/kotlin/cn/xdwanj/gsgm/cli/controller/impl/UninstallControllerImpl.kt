package cn.xdwanj.gsgm.cli.controller.impl

import cn.xdwanj.gsgm.base.LutrisConstant.SLUG_PREFIX
import cn.xdwanj.gsgm.cli.controller.UninstallController
import cn.xdwanj.gsgm.cli.print.GsgmPrinter
import cn.xdwanj.gsgm.cli.print.output.printlnGsgmGameDesc
import cn.xdwanj.gsgm.cli.print.output.printlnListTask
import cn.xdwanj.gsgm.service.LibraryService
import cn.xdwanj.gsgm.service.LutrisService
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.File

@Component
class UninstallControllerImpl(
  private val libraryService: LibraryService,
  private val lutrisService: LutrisService,
) : UninstallController {

  private val logger = LoggerFactory.getLogger(UninstallControllerImpl::class.java)

  override suspend fun removeActionByGsgmId(libraryPathList: List<File>, gsgmIdList: List<Long>): Int = coroutineScope {
    val removeWrapperList = libraryPathList
      .map { libraryService.deepGameFile(it.absolutePath) }
      .flatten()
      .map { async { libraryService.getGsgmWrapperByFile(it) } }
      .awaitAll()
      .filter { gsgmIdList.contains(it.gsgmInfo?.id) }

    removeWrapperList.forEach { wrapper ->
      val slug = "$SLUG_PREFIX${wrapper.gsgmInfo!!.id!!}"
      val state = lutrisService.removeLutrisGameBySlug(slug)

      logger.info("wrapper=$wrapper")
      logger.info("state=$state")

      GsgmPrinter.printlnGsgmGameDesc(wrapper)
      GsgmPrinter.printlnListTask(
        heading = "uninstall by gsgm id start...",
        msgList = state.messageList,
      )
    }
    0
  }

  override suspend fun removeActionByLibrary(libraryPathList: List<File>): Int = coroutineScope {
    // gsgm
    val removeWrapperList = libraryPathList
      .map { libraryService.deepGameFile(it.absolutePath) }
      .flatten()
      .map { async { libraryService.getGsgmWrapperByFile(it) } }
      .awaitAll()

    removeWrapperList.forEach { wrapper ->
      val slug = "gsgm-${wrapper.gsgmInfo!!.id!!}"
      val state = lutrisService.removeLutrisGameBySlug(slug)

      logger.info("wrapper=$wrapper")
      logger.info("state=$state")

      GsgmPrinter.printlnGsgmGameDesc(wrapper)
      GsgmPrinter.printlnListTask(
        heading = "uninstall start...",
        msgList = state.messageList
      )
    }

    0
  }
}