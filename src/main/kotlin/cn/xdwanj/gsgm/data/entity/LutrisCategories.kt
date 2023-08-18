package cn.xdwanj.gsgm.data.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName

@TableName("categories")
data class LutrisCategories(
  /**
   * 主键
   */
  @TableId(type = IdType.ASSIGN_ID)
  var id: Long? = null,

  /**
   * 类别名
   */
  var name: String? = null,
)