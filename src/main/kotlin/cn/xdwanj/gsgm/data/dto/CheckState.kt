package cn.xdwanj.gsgm.data.dto

import java.io.File

/**
 * 表示状态留言
 *
 * @property level
 * @property messageList 留言列表，已经是彩色的了
 */
data class CheckState(
  var gameFile: File,
  var level: Int = 0,
  var messageList: MutableList<String> = mutableListOf(),
) {
  operator fun plusAssign(checkState: CheckState) {
    this.level += checkState.level
    this.messageList += checkState.messageList
  }

  operator fun plus(checkState: CheckState): CheckState {
    return CheckState(
      gameFile = this.gameFile,
      level = this.level + checkState.level,
      messageList = (this.messageList + checkState.messageList).toMutableList()
    )
  }
}