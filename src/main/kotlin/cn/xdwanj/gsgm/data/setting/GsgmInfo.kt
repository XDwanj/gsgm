package cn.xdwanj.gsgm.data.setting

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include

@JsonInclude(Include.NON_EMPTY)
data class GsgmInfo(
  /**
   * 游戏标识符，不可重复
   */
  var id: Long? = null,

  // /**
  //  * 游戏图片
  //  */
  // var picture: String? = null,

  /**
   * 游戏介绍
   */
  var description: String? = null,
)