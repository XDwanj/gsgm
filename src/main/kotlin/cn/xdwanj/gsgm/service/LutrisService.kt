package cn.xdwanj.gsgm.service

import cn.xdwanj.gsgm.data.dto.CommonState
import cn.xdwanj.gsgm.data.entity.LutrisGame
import cn.xdwanj.gsgm.data.script.LutrisInstallScript
import cn.xdwanj.gsgm.data.script.LutrisRunScript
import cn.xdwanj.gsgm.data.setting.GsgmWrapper

interface LutrisService {

  // ---------------------------------------------------- install or upsert

  /**
   * 读取普通图片，转换成符合 lutris 的图标(128x128 png 空白的地方用透明代替) 并写入磁盘
   *
   * @param wrapper
   * @return
   */
  suspend fun installGameIcon(wrapper: GsgmWrapper): CommonState<Any>

  suspend fun upsertGameIcon(wrapper: GsgmWrapper): CommonState<Any>

  /**
   * 读取普通图片，转换成符合 lutris 的 coverart(jpg) 并写入磁盘
   *
   * @param wrapper
   * @return
   */
  suspend fun installGameCoverart(wrapper: GsgmWrapper): CommonState<Any>

  suspend fun upsertGameCoverart(wrapper: GsgmWrapper): CommonState<Any>

  /**
   * 读取普通图片，转换成符合 lutris 的 banner(jpg)  并写入磁盘
   *
   * @param wrapper
   * @return
   */
  suspend fun installGameBanner(wrapper: GsgmWrapper): CommonState<Any>

  suspend fun upsertGameBanner(wrapper: GsgmWrapper): CommonState<Any>

  suspend fun insertLutrisDb(gsgmWrapper: GsgmWrapper): CommonState<Any>

  suspend fun upsertLutrisDB(gsgmWrapper: GsgmWrapper): CommonState<Any>

  suspend fun installRunScript(gsgmWrapper: GsgmWrapper): CommonState<Any>

  suspend fun upsertRunScript(gsgmWrapper: GsgmWrapper): CommonState<Any>

  /**
   * 对安装单个游戏进行集成
   *
   * @param gsgmWrapper
   * @return lutris数据库实体 和 gameWrapper
   */
  suspend fun installLutrisGame(gsgmWrapper: GsgmWrapper): CommonState<Any>

  suspend fun upsertLutrisGame(gsgmWrapper: GsgmWrapper): CommonState<Any>

  // ------------------------------------------------------------- Converter
  fun getLutrisGameByGsgmWrapper(gsgmWrapper: GsgmWrapper): LutrisGame

  /**
   * 通过游戏的 .gsgm 配置，生成 `lutris -i xxx.yaml` 的 yaml 文件
   *
   * @param gsgmWrapper
   * @return
   */
  fun getLutrisInstallScript(gsgmWrapper: GsgmWrapper): LutrisInstallScript

  /**
   * 通过游戏的 gsgm 配置，生成 lutris 运行时读取的 yaml 文件
   *
   * @param gsgmWrapper
   * @return
   */
  fun getLutrisRunScript(gsgmWrapper: GsgmWrapper): LutrisRunScript

  // ------------------------------------------------- clean

  /**
   * 清理 lutris 数据库中所有 gsgm 记录
   *
   * @return
   */
  suspend fun cleanLutrisDB(): Boolean

  /**
   * 清理 lutris coverart 资源
   *
   * @return
   */
  suspend fun cleanLutrisCover(): Boolean

  /**
   * 清理 lutris banner 资源
   *
   * @return
   */
  suspend fun cleanLutrisBanner(): Boolean

  /**
   * 清理 lutris icon 资源
   *
   * @return
   */
  suspend fun cleanLutrisIcon(): Boolean

  /**
   * 清理 lutris 运行脚本资源
   *
   * @return
   */
  suspend fun cleanLutrisRunScript(): Boolean

  // ------------------------------------------------- remove

  /**
   * 删除指定 lutris 游戏的全部资源
   *
   * @param slug
   * @return
   */
  suspend fun removeLutrisGameBySlug(slug: String): CommonState<Any>

  /**
   * 删除 lutris coverart 资源
   *
   * @param slug
   * @return
   */
  suspend fun removeLutrisCoverartBySlug(slug: String): CommonState<Any>

  /**
   * 删除 lutris banner 资源
   *
   * @param slug
   * @return
   */
  suspend fun removeLutrisBannerBySlug(slug: String): CommonState<Any>

  /**
   * 删除 lutris icon 资源
   *
   * @param slug
   * @return
   */
  suspend fun removeLutrisIconBySlug(slug: String): CommonState<Any>

  /**
   * 删除 lutris 数据库资源
   *
   * @param slug
   * @return
   */
  suspend fun removeLutrisDbBySlug(slug: String): CommonState<Any>

  /**
   * 删除 lutris 运行脚本资源
   *
   * @param slug
   * @return
   */
  suspend fun removeLutrisRunScriptBySlug(slug: String): CommonState<Any>
}