package cn.xdwanj.gsgm.cli.controller.impl

import cn.xdwanj.gsgm.base.LutrisConstant
import cn.xdwanj.gsgm.cli.controller.UninstallController
import cn.xdwanj.gsgm.cli.print.GsgmPrinter
import cn.xdwanj.gsgm.cli.print.output.printlnGsgmGameDesc
import cn.xdwanj.gsgm.cli.print.output.printlnListTask
import cn.xdwanj.gsgm.data.entity.LutrisGame
import cn.xdwanj.gsgm.data.mapper.LutrisGameMapper
import cn.xdwanj.gsgm.data.wrapper.GsgmWrapper
import cn.xdwanj.gsgm.service.LibraryService
import cn.xdwanj.gsgm.service.LutrisService
import cn.xdwanj.gsgm.util.extensions.asyncMap
import cn.xdwanj.gsgm.util.extensions.queryChain
import kotlinx.coroutines.coroutineScope
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.File

@Component
class UninstallControllerImpl(
  private val lutrisService: LutrisService,
  private val lutrisGameMapper: LutrisGameMapper,
  private val libraryService: LibraryService,
) : UninstallController {

  private val logger = LoggerFactory.getLogger(UninstallControllerImpl::class.java)

  override suspend fun uninstallActionByGsgmIDList(gsgmIdList: List<Long>): Int = coroutineScope {
    val slugList = gsgmIdList.map { "${LutrisConstant.SLUG_PREFIX}$it" }
    val lutrisGameList = lutrisGameMapper.queryChain()
      .`in`(LutrisGame::slug, slugList)
      .list()

    val removeWrapperList = lutrisGameList.asyncMap {
      libraryService.getGsgmWrapperByLutrisGame(it)
    }

    uninstallLutrisByWrapperList(removeWrapperList)
    0
  }

  override suspend fun uninstallActionByLibraryList(libraryPathList: List<File>): Int = coroutineScope {
    val removeWrapperList = libraryPathList
      .asyncMap { libraryService.deepGroupFile(it.absolutePath) }
      .flatten()
      .asyncMap { group ->
        group.fileList.map {
          // todo: 这个方法还待优化
          libraryService.getGsgmWrapperByFileAndGroupName(
            groupName = group.groupName,
            gameFile = it
          )
        }
      }.flatten()

    uninstallLutrisByWrapperList(removeWrapperList)
    0
  }

  private suspend fun uninstallLutrisByWrapperList(removeWrapperList: List<GsgmWrapper>) {
    removeWrapperList.forEach { wrapper ->
      val slug = "${LutrisConstant.SLUG_PREFIX}${wrapper.gsgmInfo!!.id}"
      val state = lutrisService.removeLutrisGameBySlug(slug)

      GsgmPrinter.printlnGsgmGameDesc(wrapper)
      GsgmPrinter.printlnListTask(
        heading = "uninstall by gsgm id start...",
        msgList = state.messageList,
      )
    }
  }
}