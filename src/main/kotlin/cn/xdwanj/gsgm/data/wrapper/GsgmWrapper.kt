package cn.xdwanj.gsgm.data.wrapper

import cn.xdwanj.gsgm.base.LutrisConstant
import cn.xdwanj.gsgm.data.setting.GsgmHistory
import cn.xdwanj.gsgm.data.setting.GsgmInfo
import cn.xdwanj.gsgm.data.setting.GsgmSetting
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import java.io.File

@JsonInclude(Include.NON_EMPTY)
data class GsgmWrapper(

  /**
   * 分组名
   */
  var groupName: String = LutrisConstant.DEFAULT_CATEGORIES_NAME,

  /**
   * 游戏设置
   */
  var gsgmSetting: GsgmSetting? = null,

  /**
   * 游戏历史
   */
  var gsgmHistory: GsgmHistory? = null,

  /**
   * 游戏信息
   */
  var gsgmInfo: GsgmInfo? = null,

  // -------------------------------------------------------- ignore

  /**
   * 游戏的位置
   */
  @JsonInclude(Include.ALWAYS)
  var gameFile: File? = null,

  @JsonInclude(Include.ALWAYS)
  var tagList: MutableList<String> = mutableListOf(),
)