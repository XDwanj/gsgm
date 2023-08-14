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
import cn.xdwanj.gsgm.util.extensions.queryChain
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.File

@Component
class SyncControllerImpl(
  private val libraryService: LibraryService,
  private val lutrisGameMapper: LutrisGameMapper,
) : SyncController {

  private val logger = LoggerFactory.getLogger(SyncControllerImpl::class.java)

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
      val newWrapper = libraryService.getGsgmWrapperByLutrisGame(lutrisGame)
      logger.info("lutrisGame=$lutrisGame")
      logger.info("wrapper=$wrapper")
      logger.info("newWrapper=$newWrapper")
      GsgmPrinter.printlnGsgmGameDesc(wrapper)

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

  override suspend fun syncActionLTGByForce(libraryPathList: List<File>): Int = coroutineScope {
    // todo: 这里要解耦和
    syncActionLTG(libraryPathList)
  }
}