package cn.xdwanj.gsgm.cli.controller.impl

import cn.xdwanj.gsgm.base.LutrisConstant
import cn.xdwanj.gsgm.cli.controller.SyncController
import cn.xdwanj.gsgm.cli.print.GsgmPrinter
import cn.xdwanj.gsgm.cli.print.output.printlnGsgmGameDesc
import cn.xdwanj.gsgm.cli.print.output.printlnListTask
import cn.xdwanj.gsgm.data.dto.CommonState
import cn.xdwanj.gsgm.data.entity.LutrisGame
import cn.xdwanj.gsgm.data.entity.gsgmId
import cn.xdwanj.gsgm.data.mapper.LutrisGameMapper
import cn.xdwanj.gsgm.data.setting.GsgmWrapper
import cn.xdwanj.gsgm.service.LibraryService
import cn.xdwanj.gsgm.util.extensions.queryChain
import cn.xdwanj.kcolor.Ansi.colorize
import cn.xdwanj.kcolor.AttrTemplate.greenText
import kotlinx.coroutines.*
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

    var allFlag = false
    val (wrapperMap, lutrisGameList) = getLibraryWrapperMapAndLutrisGameList(libraryPathList)

    lutrisGameList.forEach { lutrisGame ->
      val oldWrapper = wrapperMap[lutrisGame.gsgmId]!!
      val newWrapper = libraryService.getGsgmWrapperByLutrisGame(lutrisGame)
      logger.info("lutrisGame=$lutrisGame")
      logger.info("oldWrapper=$oldWrapper")
      logger.info("newWrapper=$newWrapper")
      val state = CommonState<Unit>()
      var flag = false

      // info
      if (oldWrapper.gsgmInfo != newWrapper.gsgmInfo) {
        flag = true
        state += libraryService.installGsgmInfo(newWrapper)
      }

      // setting
      if (oldWrapper.gsgmSetting != newWrapper.gsgmSetting) {
        flag = true
        state += libraryService.installGsgmSetting(newWrapper)
      }

      // history
      if (oldWrapper.gsgmHistory != newWrapper.gsgmHistory) {
        flag = true
        state += libraryService.installGsgmHistory(newWrapper)
      }

      if (flag) {
        GsgmPrinter.printlnGsgmGameDesc(newWrapper)
        GsgmPrinter.printlnListTask(
          heading = "The data at both ends is inconsistent and is being synchronized...",
          msgList = state.messageList
        )
      } else {
        allFlag = true
      }
    }

    if (allFlag) {
      val log = colorize("All games are up to date, no sync required...", greenText)
      println(log)
    }
    0
  }

  private suspend fun getLibraryWrapperMapAndLutrisGameList(
    libraryPathList: List<File>,
  ): Pair<Map<Long, GsgmWrapper>, List<LutrisGame>> = withContext(Dispatchers.IO) {
    val wrapperMap = libraryPathList.map {
      libraryService.deepGameFile(it.absolutePath)
        .map { async { libraryService.getGsgmWrapperByFile(it) } }
        .awaitAll()
    }.flatten()
      .associateBy { it.gsgmInfo!!.id!! }

    val lutrisGameList = lutrisGameMapper.queryChain()
      .likeRight(LutrisGame::slug, LutrisConstant.SLUG_PREFIX)
      .list()
      .filter { wrapperMap[it.gsgmId] != null }

    Pair(wrapperMap, lutrisGameList)
  }

  override suspend fun syncActionLTGByForce(libraryPathList: List<File>): Int = coroutineScope {

    val (_, lutrisGameList) = getLibraryWrapperMapAndLutrisGameList(libraryPathList)

    lutrisGameList.forEach { lutrisGame ->

      val newWrapper = libraryService.getGsgmWrapperByLutrisGame(lutrisGame)
      logger.info("lutrisGame=$lutrisGame")
      logger.info("newWrapper=$newWrapper")
      val state = CommonState<Unit>()

      // info
      state += libraryService.installGsgmInfo(newWrapper)

      // setting
      state += libraryService.installGsgmSetting(newWrapper)

      // history
      state += libraryService.installGsgmHistory(newWrapper)

      GsgmPrinter.printlnGsgmGameDesc(newWrapper)
      GsgmPrinter.printlnListTask(
        heading = "The data at both ends is inconsistent and is being synchronized...",
        msgList = state.messageList
      )
    }
    0
  }
}