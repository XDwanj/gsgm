package cn.xdwanj.gsgm.cli.controller

import cn.xdwanj.gsgm.cli.group.GameTypeGroup
import java.io.File

interface InitController {

  // suspend fun initAction(
  //   activeInteractiveMode: Boolean,
  //   isLibrary: Boolean,
  //   gameTypeGroup: GameTypeGroup,
  //   pathList: List<File>,
  // ): Int

  suspend fun initActionInteract(isLibrary: Boolean, gameTypeGroup: GameTypeGroup, pathList: List<File>): Int
  suspend fun initActionDefault(isLibrary: Boolean, gameTypeGroup: GameTypeGroup, pathList: List<File>): Int
}