package cn.xdwanj.gsgm.service

import cn.xdwanj.gsgm.data.dto.CommonState
import cn.xdwanj.gsgm.data.entity.LutrisGame
import cn.xdwanj.gsgm.data.setting.GsgmWrapper
import java.io.File

/**
 * 游戏库服务 针对游戏位置的服务
 *
 */
interface LibraryService {
  /**
   * 获取当前目录下的游戏实体
   *
   * @param path 当前目录路径
   * @return 游戏实体路径列表
   */
  suspend fun listGameFile(path: String): List<File>

  /**
   * 递归获取游戏实体目录
   *
   * @param path 当前目录路径
   * @return 游戏实体路径列表
   */
  suspend fun deepGameFile(path: String): List<File>

  /**
   * 通过具体游戏的配置，生成游戏信息实体
   *
   * @param gameFile
   * @return
   */
  suspend fun getGsgmWrapperByFile(gameFile: File): GsgmWrapper

  suspend fun getGsgmWrapperByLutrisGame(lutrisGame: LutrisGame): GsgmWrapper

  suspend fun checkGameGsgmDir(gameFile: File): CommonState<File>

  suspend fun checkGameInfo(gameFile: File): CommonState<File>

  suspend fun checkGameSetting(gameFile: File): CommonState<File>

  suspend fun checkGameHistory(gameFile: File): CommonState<File>

  suspend fun checkGameResource(gameFile: File): CommonState<File>
  suspend fun installGsgmInfo(wrapper: GsgmWrapper): CommonState<Unit>
  suspend fun installGsgmSetting(wrapper: GsgmWrapper): CommonState<Unit>
  suspend fun installGsgmHistory(wrapper: GsgmWrapper): CommonState<Unit>

  suspend fun assertAll(gameFile: File)
}