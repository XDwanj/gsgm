package cn.xdwanj.gsgm.data.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.math.BigDecimal

@TableName("games")
data class LutrisGame(
  /**
   * 游戏的唯一标识符
   */
  @TableId(type = IdType.AUTO)
  var id: Long? = null,

  /**
   * 游戏的名称
   * not null
   */
  var name: String? = null,

  /**
   * 游戏的slug
   * not null
   */
  var slug: String? = null,

  /**
   * 游戏安装程序的slug
   * null
   */
  var installerSlug: String? = null,

  /**
   * 父游戏的slug
   * null
   */
  var parentSlug: String? = null,

  /**
   * 游戏的平台
   * default: Windows
   */
  var platform: String? = null,

  /**
   * 游戏的运行器
   * default: wine
   */
  var runner: String? = null,

  /**
   * 可执行的游戏文件
   * null
   */
  var executable: String? = null,

  /**
   * 游戏的目录
   * null
   */
  var directory: String? = null,

  /**
   * 游戏的更新时间
   * null
   */
  var updated: String? = null,

  /**
   * 游戏最后玩的时间
   * default: null
   * 单位: 秒
   */
  var lastplayed: Long? = null,

  /**
   * 游戏是否已安装
   * default: 1
   */
  var installed: Int? = null,

  /**
   * 游戏安装的时间
   * default: 时间戳前10位
   * 单位: 秒
   */
  var installedAt: Long? = null,

  /**
   * 游戏的发布年份
   * null
   */
  var year: Int? = null,

  /**
   * 游戏的配置path
   * default: $slug-${时间戳前10位}
   */
  var configpath: String? = null,

  /**
   * 游戏是否有自定义横幅
   * default: 0
   */
  var hasCustomBanner: Int? = null,

  /**
   * 游戏是否有自定义图标
   * default: 0
   */
  var hasCustomIcon: Int? = null,

  /**
   * 游戏是否有自定义大封面艺术
   * default: 0
   */
  var hasCustomCoverartBig: Int? = null,

  /**
   * 游戏的游玩时间
   * default: 0
   * 单位：小时
   */
  var playtime: BigDecimal? = null,

  /**
   * 游戏是否隐藏
   * default: 0
   */
  var hidden: Int? = null,

  /**
   * 游戏的服务
   * null
   */
  var service: String? = null,

  /**
   * 游戏服务的ID
   * null
   */
  var serviceId: String? = null,

  /**
   * 游戏的Discord ID
   * null
   */
  var discordId: String? = null
)

