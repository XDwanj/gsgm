package cn.xdwanj.gsgm.cli.controller.impl

import cn.xdwanj.gsgm.base.LutrisConstant
import cn.xdwanj.gsgm.cli.controller.SyncController
import cn.xdwanj.gsgm.data.entity.LutrisGame
import cn.xdwanj.gsgm.data.mapper.LutrisGameMapper
import cn.xdwanj.gsgm.service.LibraryService
import cn.xdwanj.gsgm.service.LutrisService
import cn.xdwanj.gsgm.util.extensions.queryChain
import cn.xdwanj.gsgm.util.topfun.printlnGsgmGameDesc
import cn.xdwanj.kcolor.Ansi
import cn.xdwanj.kcolor.AttrTemplate.blueText
import cn.xdwanj.kcolor.AttrTemplate.italic
import cn.xdwanj.kcolor.AttrTemplate.redText
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
        println(Ansi.colorize("    同步成功", italic))
      } catch (e: Exception) {
        logger.error("    安装失败: $wrapper", e)
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
        println(Ansi.colorize("    强制同步成功", italic))
      } catch (e: Exception) {
        println((Ansi.colorize("    安装失败: $wrapper", redText)))
      }
    }

    0
  }

  override suspend fun syncActionLTG(libraryPathList: List<File>): Int = coroutineScope {

    val lutrisGameList = lutrisGameMapper.queryChain()
      .likeRight(LutrisGame::slug, LutrisConstant.SLUG_PREFIX)
      .list()

    lutrisGameList.forEach { lutrisGame ->
      val newWrapper = libraryService.getGsgmWrapperByLutrisGame(lutrisGame)
      printlnGsgmGameDesc(newWrapper)

      println(Ansi.colorize("    :: ", blueText) + "pgaDb 同步中...")
      lutrisService.upsertLutrisDB(newWrapper)
      println(Ansi.colorize("    :: ", blueText) + "info 同步中...")
      libraryService.installGsgmInfo(newWrapper)
      println(Ansi.colorize("    :: ", blueText) + "setting 同步中...")
      libraryService.installGsgmSetting(newWrapper)
      println(Ansi.colorize("    :: ", blueText) + "history 同步中...")
      libraryService.installGsgmHistory(newWrapper)
      println(Ansi.colorize("    游戏已同步", italic))
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