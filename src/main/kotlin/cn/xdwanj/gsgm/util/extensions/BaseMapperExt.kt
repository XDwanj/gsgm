package cn.xdwanj.gsgm.util.extensions

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.baomidou.mybatisplus.extension.kotlin.KtQueryChainWrapper
import com.baomidou.mybatisplus.extension.kotlin.KtUpdateChainWrapper


inline fun <reified T : Any> BaseMapper<T>.queryChain(): KtQueryChainWrapper<T> {
  return KtQueryChainWrapper(this, T::class.java)
}

inline fun <reified T : Any> BaseMapper<T>.updateChain(): KtUpdateChainWrapper<T> {
  return KtUpdateChainWrapper(this, T::class.java)
}
