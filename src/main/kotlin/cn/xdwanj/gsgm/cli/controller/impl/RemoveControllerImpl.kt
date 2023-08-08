package cn.xdwanj.gsgm.cli.controller.impl

import cn.xdwanj.gsgm.base.LutrisConstant
import cn.xdwanj.gsgm.cli.controller.RemoveController
import cn.xdwanj.gsgm.data.entity.LutrisGame
import cn.xdwanj.gsgm.data.entity.LutrisRelGameToCategories
import cn.xdwanj.gsgm.data.mapper.LutrisGameMapper
import cn.xdwanj.gsgm.data.mapper.LutrisRelGameToCategoriesMapper
import cn.xdwanj.gsgm.service.LibraryService
import cn.xdwanj.gsgm.util.extensions.queryChain
import cn.xdwanj.gsgm.util.extensions.updateChain
import cn.xdwanj.kcolor.Ansi
import cn.xdwanj.kcolor.AttrTemplate.purpleText
import cn.xdwanj.kcolor.AttrTemplate.yellowText
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.dromara.hutool.core.io.file.FileUtil
import org.dromara.hutool.json.JSONUtil
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.File

@Component
class RemoveControllerImpl(
  private val objectMapper: ObjectMapper,
  private val libraryService: LibraryService,
  private val lutrisGameMapper: LutrisGameMapper,
  private val lutrisRelGameToCategoriesMapper: LutrisRelGameToCategoriesMapper,
) : RemoveController {

  private val logger = LoggerFactory.getLogger(RemoveControllerImpl::class.java)

  override suspend fun removeAction(libraryPathList: List<File>, gsgmIdList: List<Long>): Int = coroutineScope {
    // gsgm
    val removeWrapperList = libraryPathList
      .map { libraryService.deepGameFile(it.absolutePath) }
      .flatten()
      .map { async { libraryService.getGsgmWrapperByFile(it) } }
      .awaitAll()
      .filter { gsgmIdList.contains(it.gsgmInfo?.id) }

    val slugList = gsgmIdList.map { LutrisConstant.SLUG_PREFIX + it }

    // lutris
    val removeLutrisGameList = lutrisGameMapper.queryChain()
      .`in`(LutrisGame::slug, slugList)
      .list()
      .filterNotNull()

    // remove gsgm
    removeWrapperList.forEach {
      println(Ansi.colorize(":: ", purpleText) + "Gsgm 游戏删除中...")
      FileUtil.del(it.gameFile)
    }

    // remove lutris
    // lutrisGameMapper.deleteBatchIds(removeLutrisGameList.map { it.id })
    lutrisGameMapper.updateChain()
      .`in`(LutrisGame::id, removeLutrisGameList.map { it.id })
      .remove()
    lutrisRelGameToCategoriesMapper.updateChain()
      .`in`(LutrisRelGameToCategories::gameId, gsgmIdList)
      .remove()

    println(Ansi.colorize("Gsgm 库删除成功的有:"))
    removeWrapperList.forEach {
      println(Ansi.colorize(objectMapper.writeValueAsString(it).let { JSONUtil.formatJsonStr(it) }, yellowText))
    }

    println(Ansi.colorize("Lutris 库删除成功的有:"))
    removeLutrisGameList.forEach {
      println(Ansi.colorize(objectMapper.writeValueAsString(it).let { JSONUtil.formatJsonStr(it) }, yellowText))
    }

    0
  }
}