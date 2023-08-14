package cn.xdwanj.gsgm.data

import cn.xdwanj.gsgm.base.GsgmFileName
import cn.xdwanj.gsgm.data.enum.LocaleCharSet
import cn.xdwanj.gsgm.data.enum.Platform
import cn.xdwanj.gsgm.data.setting.GsgmHistory
import cn.xdwanj.gsgm.data.setting.GsgmInfo
import cn.xdwanj.gsgm.data.setting.GsgmSetting
import org.dromara.hutool.core.data.id.IdUtil
import org.dromara.hutool.core.date.DateTime
import java.math.BigDecimal

object Defaults {
  val defaultGsgmHistory
    get() = GsgmHistory(
      lastGameMoment = DateTime.now(),
      gameTime = BigDecimal.ZERO,
    )

  val defaultGsgmInfo
    get() = GsgmInfo(
      id = IdUtil.getSnowflakeNextId(),
      initTime = DateTime()
    )

  val defaultGsgmSetting
    get() = GsgmSetting(
      executeLocation = "",
      winePrefix = "${GsgmFileName.GSGM_DIR}/winePrefix",
      localeCharSet = LocaleCharSet.China_UTF_8,
      platform = Platform.Windows
    )
}
