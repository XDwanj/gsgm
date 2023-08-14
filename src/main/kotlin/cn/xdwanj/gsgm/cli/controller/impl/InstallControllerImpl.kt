package cn.xdwanj.gsgm.cli.controller.impl

import cn.xdwanj.gsgm.cli.controller.InstallController
import cn.xdwanj.gsgm.cli.print.GsgmPrinter
import cn.xdwanj.gsgm.cli.print.output.printlnGsgmGameDesc
import cn.xdwanj.gsgm.cli.print.output.printlnListTask
import cn.xdwanj.gsgm.service.LibraryService
import cn.xdwanj.gsgm.service.LutrisService
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Component
import java.io.File

@Component
class InstallControllerImpl(
  private val libraryService: LibraryService,
  private val lutrisService: LutrisService,
) : InstallController {

  override suspend fun installActionByLibrary(libraryPathList: List<File>): Int = coroutineScope {
    val installWrapperList = libraryPathList.map { libraryService.deepGameFile(it.absolutePath) }
      .flatten()
      .onEach { libraryService.assertAll(it) }
      .map { async { libraryService.getGsgmWrapperByFile(it) } }
      .awaitAll()

    installWrapperList.forEach { wrapper ->
      GsgmPrinter.printlnGsgmGameDesc(wrapper)
      val state = lutrisService.upsertLutrisGame(wrapper)
      GsgmPrinter.printlnListTask(
        heading = "Installation begins...",
        msgList = state.messageList
      )
    }

    0
  }

  override suspend fun installActionLibraryByForce(libraryPathList: List<File>): Int = coroutineScope {
    val installWrapperList = libraryPathList.map { libraryService.deepGameFile(it.absolutePath) }
      .flatten()
      .onEach { libraryService.assertAll(it) }
      .map { async { libraryService.getGsgmWrapperByFile(it) } }
      .awaitAll()

    installWrapperList.forEach { wrapper ->
      GsgmPrinter.printlnGsgmGameDesc(wrapper)
      val state = lutrisService.installLutrisGame(wrapper)
      GsgmPrinter.printlnListTask(
        heading = "Forced installation to start...",
        msgList = state.messageList
      )
    }

    0
  }
}