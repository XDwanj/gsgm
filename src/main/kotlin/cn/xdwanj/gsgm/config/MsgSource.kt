package cn.xdwanj.gsgm.config

import org.dromara.hutool.extra.spring.SpringUtil
import org.springframework.context.MessageSource
import java.util.*

// object MsgUtil {
//
//   private val messageSource: MessageSource = SpringUtil.getBean(MessageSource::class.java)
//
//   operator fun get(msgKey: String): String {
//     return try {
//       messageSource.getMessage(msgKey, null, Locale.getDefault())
//     } catch (e: Exception) {
//       println(e)
//       msgKey
//     }
//   }
//
// }