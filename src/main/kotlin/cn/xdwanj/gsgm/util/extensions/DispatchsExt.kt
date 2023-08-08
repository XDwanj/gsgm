package cn.xdwanj.gsgm.util.extensions

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * 游戏数据操作需要单线程
 */
val Dispatchers.GameDataBlockIO: CoroutineDispatcher
  get() = ThreadPoolExecutor(1, 1, Long.MAX_VALUE, TimeUnit.SECONDS, LinkedBlockingQueue(Int.MAX_VALUE))
    .asCoroutineDispatcher()