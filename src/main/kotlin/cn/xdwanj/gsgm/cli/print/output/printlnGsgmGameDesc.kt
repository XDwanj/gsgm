package cn.xdwanj.gsgm.cli.print.output

import cn.xdwanj.gsgm.base.GsgmFileName
import cn.xdwanj.gsgm.cli.print.GsgmPrinter
import cn.xdwanj.gsgm.data.mapper.LutrisGameMapper
import cn.xdwanj.gsgm.data.mapper.LutrisRelGameToCategoriesMapper
import cn.xdwanj.gsgm.data.setting.GsgmInfo
import cn.xdwanj.gsgm.data.setting.GsgmSetting
import cn.xdwanj.gsgm.data.wrapper.GsgmWrapper
import cn.xdwanj.gsgm.data.wrapper.LutrisWrapper
import cn.xdwanj.kcolor.Ansi
import cn.xdwanj.kcolor.AttrTemplate
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.dromara.hutool.core.date.DateUtil
import org.dromara.hutool.core.io.file.FileUtil
import org.dromara.hutool.core.math.NumberUtil
import org.dromara.hutool.extra.spring.SpringUtil
import org.springframework.util.MimeTypeUtils
import java.math.BigDecimal

// val lutrisGameMapper = SpringUtil.getBean(LutrisGameMapper::class.java)
// val lutrisRelGameToCategoriesMapper = SpringUtil.getBean(LutrisRelGameToCategoriesMapper::class.java)
val objectMapper = SpringUtil.getBean(ObjectMapper::class.java)

fun GsgmPrinter.printlnGsgmGameDesc(gsgmWrapper: GsgmWrapper): String {
  // val objectMapper = ObjectMapper()
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
  val playtime: String = gameTime.formatHour()

  val builder = StringBuilder().apply {
    this.append(Ansi.colorize("$id", AttrTemplate.greenText))
    this.append("/")
    this.append(Ansi.colorize("${gsgmWrapper.gameFile?.name}", AttrTemplate.bold))
    this.append(" ")
    this.append(Ansi.colorize("${setting?.platform}", AttrTemplate.blueText))
    this.append(" ")
    this.append(
      Ansi.colorize(
        "(lastPlayed: ${DateUtil.formatDate(lastGameMoment)}, playTime: $playtime)",
        AttrTemplate.purpleText
      )
    )
    this.append("\n")
    this.append("    ")
    this.append("'${gsgmWrapper.gameFile?.absolutePath}'")
  }
  val message = builder.toString()
  println(message)

  return message
}

fun BigDecimal?.formatHour(): String = if (this == null) {
  "null"
} else if (this > BigDecimal("1")) {
  val hour = this.toInt()
  val minute = NumberUtil.sub(this, hour).let {
    NumberUtil.mul(it, 60)
  }
  "$hour h ${minute.toInt()} min"
} else {
  val minute = NumberUtil.mul(this, 60)
  "${minute.toInt()} min"
}

fun GsgmPrinter.printlnGsgmGameDesc(lutrisWrapper: LutrisWrapper) {
  TODO("未实现")
}

fun main() {
  MimeTypeUtils.TEXT_PLAIN_VALUE
}