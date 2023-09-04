package cn.xdwanj.gsgm.data.wrapper

import cn.xdwanj.gsgm.data.entity.LutrisGame
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY

@JsonInclude(NON_EMPTY)
data class LutrisWrapper(
  var lutrisGame: LutrisGame? = null,
  var groupName: String? = null,
)