package cn.xdwanj.gsgm.data.setting

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import java.math.BigDecimal
import java.util.*

@JsonInclude(Include.NON_EMPTY)
data class GsgmHistory(

  /**
   * 上次游玩时刻
   */
  var lastGameMoment: Date? = null,

  // /**
  //  * 上次游玩时间
  //  */
  // var lastGameTime: BigDecimal? = null,

  /**
   * 总共游玩时间
   */
  var gameTime: BigDecimal? = null,
)