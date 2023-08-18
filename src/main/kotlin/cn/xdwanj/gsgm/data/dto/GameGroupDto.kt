package cn.xdwanj.gsgm.data.dto

import cn.xdwanj.gsgm.base.LutrisConstant
import java.io.File

data class GameGroupDto(
  val groupName: String = LutrisConstant.DEFAULT_CATEGORIES_NAME,
  val fileList: MutableList<File> = mutableListOf(),
)