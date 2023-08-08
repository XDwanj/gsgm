package cn.xdwanj.gsgm.service

import cn.xdwanj.gsgm.data.entity.LutrisGame
import cn.xdwanj.gsgm.data.script.LutrisInstallScript
import cn.xdwanj.gsgm.data.script.LutrisRunScript
import cn.xdwanj.gsgm.data.setting.GsgmWrapper
import java.io.File

interface LutrisService {

  // ---------------------------------------------------- write disk

  /**
   * 读取普通图片，转换成符合 lutris 的图标(128x128 png 空白的地方用透明代替) 并写入磁盘
   *
   * @param wrapper
   * @param gsgmId
   * @return
   */
  suspend fun installGameIcon(wrapper: GsgmWrapper): Boolean

  /**
   * 读取普通图片，转换成符合 lutris 的 coverart(jpg) 并写入磁盘
   *
   * @param gameFile
   * @param slug
   * @return
   */
  suspend fun installGameCoverart(wrapper: GsgmWrapper): Boolean

  /**
   * 读取普通图片，转换成符合 lutris 的 banner(jpg)  并写入磁盘
   *
   * @param gameFile
   * @param slug
   * @return
   */
  suspend fun installGameBanner(wrapper: GsgmWrapper): Boolean

  // ------------------------------------------------------------- script

  /**
   * 通过游戏的 .gsgm 配置，生成 `lutris -i xxx.yaml` 的 yaml 文件
   *
   * @param gsgmWrapper
   * @return
   */
  suspend fun getLutrisInstallScript(gsgmWrapper: GsgmWrapper): LutrisInstallScript

  /**
   * 通过游戏的 gsgm 配置，生成 lutris 运行时读取的 yaml 文件
   *
   * @param gsgmWrapper
   * @return
   */
  suspend fun getLutrisRunScript(gsgmWrapper: GsgmWrapper): LutrisRunScript

  // ---------------------------------------------------------------- install

  /**
   * 通过游戏的 gsgm 配置，插入 lutris 数据库
   *
   * @param gsgmWrapper
   * @return
   */
  suspend fun insertLutrisDB(gsgmWrapper: GsgmWrapper): LutrisGame

  /**
   * 重载实现
   *
   * @param gameFile
   * @return
   */
  suspend fun insertLutrisDB(gameFile: File): LutrisGame

  suspend fun upsertLutrisDB(gsgmWrapper: GsgmWrapper): LutrisGame

  /**
   * 对安装单个游戏进行集成
   *
   * @param gsgmWrapper
   * @return lutris数据库实体 和 gameWrapper
   */
  suspend fun installLutrisGame(gsgmWrapper: GsgmWrapper): LutrisGame


  /**
   * TODO
   *
   * @param wrapper
   * @return
   */
  suspend fun updateInstallLutrisGame(wrapper: GsgmWrapper): LutrisRunScript

  // ------------------------------------------------- converter

  fun getLutrisGameByGsgmWrapper(gsgmWrapper: GsgmWrapper): LutrisGame

  // ------------------------------------------------- clean
  /**
   * 清理 lutris 数据库中所有 gsgm 记录
   *
   * @return
   */
  suspend fun cleanLutrisDB(): Boolean

  suspend fun cleanLutrisCover(): Boolean

  suspend fun cleanLutrisBanner(): Boolean

  suspend fun cleanLutrisIcon(): Boolean
  suspend fun cleanLutrisRunScript(): Boolean

}