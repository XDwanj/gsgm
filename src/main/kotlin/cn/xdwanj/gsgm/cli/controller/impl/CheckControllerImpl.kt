package cn.xdwanj.gsgm.cli.controller.impl

import cn.xdwanj.gsgm.cli.controller.CheckController
import cn.xdwanj.gsgm.cli.print.GsgmPrinter
import cn.xdwanj.gsgm.cli.print.output.printlnListTask
import cn.xdwanj.gsgm.data.dto.CheckState
import cn.xdwanj.gsgm.service.LibraryService
import cn.xdwanj.kcolor.Ansi
import cn.xdwanj.kcolor.AttrTemplate
import kotlinx.coroutines.*
import org.springframework.stereotype.Component
import java.io.File

@Component
class CheckControllerImpl(
  private val libraryService: LibraryService,
) : CheckController {

  override suspend fun checkAction(
    isLibrary: Boolean,
    gamePathList: List<File>,
  ): Int = coroutineScope {
    val checkResult = if (isLibrary) {
      gamePathList
        .map { async { libraryService.deepGameFile(it.absolutePath) } }
        .awaitAll()
        .flatten()
        .map { async { checkAllState(it) } }
        .awaitAll()
    } else {
      gamePathList.map {
        async { checkAllState(it) }
      }.awaitAll()
    }

    // print
    checkResult.sortedBy { it.level }
      .forEach { state ->
        val header = Ansi.colorize(state.gameFile.toString(), AttrTemplate.greenText)
        val msgList = state.messageList
        GsgmPrinter.printlnListTask(
          heading = header,
          msgList = msgList
        )
        // println(header)
        // state.messageList.forEach {
        //   println("    $it")
        // }
      }

    0
  }

  private suspend fun checkAllState(
    gameFile: File,
  ): CheckState = withContext(Dispatchers.Default) {
    val checkState = CheckState(gameFile)

    listOf(
      async { libraryService.checkGameGsgmDir(gameFile) },
      async { libraryService.checkGameInfo(gameFile) },
      async { libraryService.checkGameSetting(gameFile) },
      async { libraryService.checkGameHistory(gameFile) },
      async { libraryService.checkGameResource(gameFile) },
    ).awaitAll()
      .forEach { checkState += it }

    checkState
  }


}