package cn.xdwanj.gsgm.cli.controller.impl

import cn.xdwanj.gsgm.base.LutrisConstant
import cn.xdwanj.gsgm.cli.controller.InfoController
import cn.xdwanj.gsgm.cli.print.GsgmPrinter
import cn.xdwanj.gsgm.cli.print.output.printlnLine
import cn.xdwanj.gsgm.config.FlexibleDataSource
import cn.xdwanj.gsgm.data.entity.LutrisGame
import cn.xdwanj.gsgm.data.mapper.LutrisGameMapper
import cn.xdwanj.gsgm.data.setting.GsgmWrapper
import cn.xdwanj.gsgm.service.LibraryService
import cn.xdwanj.gsgm.util.extensions.queryChain
import cn.xdwanj.gsgm.util.extensions.toGsgmWrapperList
import cn.xdwanj.kcolor.Ansi
import cn.xdwanj.kcolor.AttrTemplate.greenText
import cn.xdwanj.kcolor.AttrTemplate.yellowText
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.dromara.hutool.json.JSONUtil
import org.springframework.stereotype.Component
import java.io.File

@Component
class InfoControllerImpl(
  private val flexibleDataSource: FlexibleDataSource,
  private val libraryService: LibraryService,
  private val objectMapper: ObjectMapper,
  private val lutrisGameMapper: LutrisGameMapper,
) : InfoController {

  override suspend fun infoAction(
    libraryPathList: List<File>?,
    gsgmIdList: List<Long>,
  ): Int = withContext(Dispatchers.IO) {

    // 有效的 gsgm list
    val gsgmWrapperList: List<GsgmWrapper> =
      libraryPathList?.map { libraryService.deepGroupFile(it.absolutePath) }
        ?.flatten()
        ?.toGsgmWrapperList()
        ?.filter { gsgmIdList.contains(it.gsgmInfo!!.id) }
        ?: emptyList()

    // 有效的 lutris list
    val lutrisSlugList = gsgmIdList.map { LutrisConstant.SLUG_PREFIX + it }
    val lutrisGameList = lutrisGameMapper.queryChain()
      .`in`(LutrisGame::slug, lutrisSlugList)
      .list()

    gsgmIdList.forEach { id ->
      val slug = LutrisConstant.SLUG_PREFIX + id
      val gsgmWrapper = gsgmWrapperList.firstOrNull { wrapper -> wrapper.gsgmInfo?.id == id }
      val lutrisGame = lutrisGameList.firstOrNull { it.slug == slug }

      GsgmPrinter.printlnLine()
      println(Ansi.colorize(slug, greenText))
      val gsgmJson = objectMapper.writeValueAsString(gsgmWrapper).let { JSONUtil.formatJsonStr(it) }
      val lutrisJson = objectMapper.writeValueAsString(lutrisGame).let { JSONUtil.formatJsonStr(it) }

      val log = Ansi.colorize(gsgmJson + "\n" + lutrisJson, yellowText)
      println(log)
      GsgmPrinter.printlnLine()
    }

    flexibleDataSource.popDB()
    0
  }
}