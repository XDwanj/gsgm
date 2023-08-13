package cn.xdwanj.gsgm.data.entity

import com.baomidou.mybatisplus.annotation.TableName

@TableName("games_categories")
data class LutrisRelGameToCategories(
  var gameId: Long? = null,
  var categoryId: Long? = null,
)