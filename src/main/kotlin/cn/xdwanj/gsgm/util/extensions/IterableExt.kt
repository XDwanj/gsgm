package cn.xdwanj.gsgm.util.extensions

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

suspend inline fun <T, R> Iterable<T>.asyncMap(
  context: CoroutineContext = Dispatchers.IO,
  start: CoroutineStart = CoroutineStart.DEFAULT,
  crossinline transform: suspend (T) -> R,
): List<R> = withContext(context) {
  this@asyncMap
    .map { async(context, start) { transform(it) } }
    .awaitAll()
}