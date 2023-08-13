package cn.xdwanj.gsgm.cli.controller.impl

import cn.xdwanj.gsgm.base.LutrisConstant
import cn.xdwanj.gsgm.cli.controller.SyncController
import cn.xdwanj.gsgm.cli.print.GsgmPrinter
import cn.xdwanj.gsgm.cli.print.output.printSingleTask
import cn.xdwanj.gsgm.cli.print.output.printlnGsgmGameDesc
import cn.xdwanj.gsgm.cli.print.output.printlnLevelTask
import cn.xdwanj.gsgm.data.entity.LutrisGame
import cn.xdwanj.gsgm.data.entity.gsgmId
import cn.xdwanj.gsgm.data.mapper.LutrisGameMapper
import cn.xdwanj.gsgm.service.LibraryService
import cn.xdwanj.gsgm.service.LutrisService
import cn.xdwanj.gsgm.util.extensions.queryChain
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.File

@Component
class SyncControllerImpl(
  private val libraryService: LibraryService,
  private val lutrisGameMapper: LutrisGameMapper,
  private val lutrisService: LutrisService,
) : SyncController {

  private val logger = LoggerFactory.getLogger(SyncControllerImpl::class.java)

  override suspend fun syncActionGTL(libraryPathList: List<File>): Int = coroutineScope {
    val gsgmWrapperList = libraryPathList
      .map { libraryService.deepGameFile(it.absolutePath) }
      .flatten()
      .onEach { assertAll(it) } // check
      .map { async { libraryService.getGsgmWrapperByFile(it) } }
      .awaitAll()

    gsgmWrapperList.map { wrapper ->
      printlnGsgmGameDesc(wrapper)

      try {
        lutrisService.updateInstallLutrisGame(wrapper)
        GsgmPrinter.printSingleTask("Sync succeeded")
      } catch (e: Exception) {
        GsgmPrinter.printSingleTask("Sync failed: $wrapper")
      }
    }

    0
  }


  override suspend fun syncActionGTLByForce(libraryPathList: List<File>): Int = coroutineScope {
    val gsgmWrapperList = libraryPathList
      .map { libraryService.deepGameFile(it.absolutePath) }
      .flatten()
      .onEach { assertAll(it) } // check
      .map { async { libraryService.getGsgmWrapperByFile(it) } }
      .awaitAll()

    gsgmWrapperList.map { wrapper ->
      printlnGsgmGameDesc(wrapper)
      try {
        lutrisService.installLutrisGame(wrapper)
        GsgmPrinter.printSingleTask("Force sync succeeded")
      } catch (e: Exception) {
        GsgmPrinter.printSingleTask("Force sync failed: $wrapper")
      }
    }

    0
  }

  override suspend fun syncActionLTG(libraryPathList: List<File>): Int = coroutineScope {

    val wrapperMap = libraryPathList.map {
      libraryService.deepGameFile(it.absolutePath)
        .map { async { libraryService.getGsgmWrapperByFile(it) } }
        .awaitAll()
    }.flatten()
      .associateBy { it.gsgmInfo!!.id!! }

    val lutrisGameMap = lutrisGameMapper.queryChain()
      .likeRight(LutrisGame::slug, LutrisConstant.SLUG_PREFIX)
      .list()
      .filter { wrapperMap[it.gsgmId] != null }

    lutrisGameMap.forEach { lutrisGame ->
      val wrapper = wrapperMap[lutrisGame.gsgmId]!!
      val newWrapper = libraryService.getGsgmWrapperByLutrisGame(lutrisGame, wrapper.gameFile)
      logger.info("lutrisGame=$lutrisGame")
      logger.info("wrapper=$wrapper")
      logger.info("newWrapper=$newWrapper")
      printlnGsgmGameDesc(wrapper)

      // GsgmPrinter.printlnLevelTask("pgaDb syncing...")
      // lutrisService.upsertLutrisDB(newWrapper)

      GsgmPrinter.printlnLevelTask("info syncing...")
      libraryService.installGsgmInfo(newWrapper)

      GsgmPrinter.printlnLevelTask("setting syncing...")
      libraryService.installGsgmSetting(newWrapper)

      GsgmPrinter.printlnLevelTask("history syncing...")
      libraryService.installGsgmHistory(newWrapper)

      GsgmPrinter.printSingleTask(msg = "game synced")
    }

    0
  }

  // ---------------------------------------- private function

  private suspend fun assertAll(gameFile: File) = withContext(Dispatchers.IO) {

    val resultState = listOf(
      async { libraryService.checkGameInfo(gameFile) },
      async { libraryService.checkGameSetting(gameFile) },
      async { libraryService.checkGameHistory(gameFile) },
      async { libraryService.checkGameResource(gameFile) },
    ).awaitAll()
      .reduce { first, second -> first + second }

    if (resultState.level > 0) {
      resultState.messageList.forEach(::println)
      throw IllegalStateException("检查失败，游戏库格式错误: ${gameFile.absoluteFile}")
    }
  }
}