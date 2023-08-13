package cn.xdwanj.gsgm.cli.print.output

import cn.xdwanj.gsgm.base.GsgmFileName
import cn.xdwanj.gsgm.data.setting.GsgmInfo
import cn.xdwanj.gsgm.data.setting.GsgmSetting
import cn.xdwanj.gsgm.data.setting.GsgmWrapper
import cn.xdwanj.kcolor.Ansi
import cn.xdwanj.kcolor.AttrTemplate
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.dromara.hutool.core.date.DateUtil
import org.dromara.hutool.core.io.file.FileUtil
import org.dromara.hutool.core.math.NumberUtil
import java.math.BigDecimal

fun printlnGsgmGameDesc(gsgmWrapper: GsgmWrapper): String {
  val objectMapper = ObjectMapper()
  val infoPath = "${gsgmWrapper.gameFile?.absolutePath}/${GsgmFileName.GSGM_DIR}/${GsgmFileName.INFO}"
  val settingPath = "${gsgmWrapper.gameFile?.absolutePath}/${GsgmFileName.GSGM_DIR}/${GsgmFileName.SETTING}"
  val info = gsgmWrapper.gsgmInfo ?: run {
    try {
      if (FileUtil.exists(infoPath)) {
        objectMapper.readValue<GsgmInfo>(FileUtil.readUtf8String(infoPath))
      } else {
        null
      }
    } catch (e: Exception) {
      println(e)
      null
    }
  }

  val setting = gsgmWrapper.gsgmSetting ?: run {
    try {
      if (FileUtil.exists(settingPath)) {
        objectMapper.readValue<GsgmSetting>(FileUtil.readUtf8String(settingPath))
      } else {
        null
      }
    } catch (e: Exception) {
      println(e)
      null
    }
  }

  val id = info?.id
  val lastGameMoment = gsgmWrapper.gsgmHistory?.lastGameMoment
  val gameTime = gsgmWrapper.gsgmHistory?.gameTime
  val playtime: String = if (gameTime == null) {
    "null"
  } else if (gameTime > BigDecimal("1")) {
    val hour = gameTime.toInt()
    val minute = NumberUtil.sub(gameTime, hour).let {
      NumberUtil.mul(it, 60)
    }
    "$hour h ${minute.toInt()} min"
  } else {
    val minute = NumberUtil.mul(gameTime, 60)
    "${minute.toInt()} min"
  }

  val builder = StringBuilder().apply {
    append(Ansi.colorize("$id", AttrTemplate.greenText))
    append("/")
    append(Ansi.colorize("${gsgmWrapper.gameFile?.name}", AttrTemplate.bold))
    append(" ")
    append(Ansi.colorize("${setting?.platform}", AttrTemplate.blueText))
    append(" ")
    append(
      Ansi.colorize(
        "(lastPlayed: ${DateUtil.formatDate(lastGameMoment)}, playTime: $playtime)",
        AttrTemplate.purpleText
      )
    )
    append("\n")
    append("    ")
    append("'${gsgmWrapper.gameFile?.absolutePath}'")
  }
  val message = builder.toString()
  println(message)

  return message
}