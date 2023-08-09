package cn.xdwanj.gsgm.cli.controller

import cn.xdwanj.gsgm.cli.group.GameTypeGroup
import java.io.File

/**
 * 初始化命令控制器
 *
 */
interface InitController {

  /**
   * 交互模式初始化游戏
   *
   * @param isLibrary 是否是游戏库
   * @param gameTypeGroup 全局游戏类型
   * @param pathList 路径列表
   * @return 终端状态码
   */
  suspend fun initActionByInteract(isLibrary: Boolean, gameTypeGroup: GameTypeGroup, pathList: List<File>): Int

  /**
   * 以默认参数初始化游戏，并不指定游戏的 exe 路径，须自行修改
   *
   * @param isLibrary 是否是游戏库
   * @param gameTypeGroup 全局游戏类型
   * @param pathList 路径列表
   * @return 终端状态码
   */
  suspend fun initActionDefault(isLibrary: Boolean, gameTypeGroup: GameTypeGroup, pathList: List<File>): Int
}