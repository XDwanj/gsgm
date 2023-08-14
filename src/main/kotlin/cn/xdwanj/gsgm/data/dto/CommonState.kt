package cn.xdwanj.gsgm.data.dto

/**
 * 表示状态留言
 *
 * @property level
 * @property messageList 留言列表，已经是彩色的了
 */
data class CommonState<T>(
  var data: T? = null,
  var level: Int = 0,
  val messageList: MutableList<String> = mutableListOf(),
) {
  operator fun plusAssign(commonState: CommonState<T>) {
    this.level += commonState.level
    this.messageList += commonState.messageList
  }

  operator fun plus(commonState: CommonState<T>): CommonState<T> {
    return CommonState(
      data = this.data,
      level = this.level + commonState.level,
      messageList = (this.messageList + commonState.messageList).toMutableList()
    )
  }
}
