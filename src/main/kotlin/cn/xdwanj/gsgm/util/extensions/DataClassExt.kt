package cn.xdwanj.gsgm.util.extensions

import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor

/**
 * 深拷贝，支持data class
 *
 * @param T
 * @return
 */
fun <T : Any> T.deepCopy(): T {
  //①判断是否为数据类，不是的话直接返回
  if (!this::class.isData) {
    return this
  }

  //②数据类一定有主构造器，不用怕，这里放心使用 !! 来转为非空类型
  return this::class.primaryConstructor!!.let { primaryConstructor ->
    primaryConstructor.parameters
      .associate { parameter ->
        val value = (this::class as KClass<T>).declaredMemberProperties
          .first { it.name == parameter.name }
          .get(this)
        //③如果主构造器参数类型为数据类，递归调用
        if ((parameter.type.classifier as? KClass<*>)?.isData == true) {
          parameter to value?.deepCopy()
        } else {
          parameter to value
        }
      }.let(primaryConstructor::callBy)
  }
}