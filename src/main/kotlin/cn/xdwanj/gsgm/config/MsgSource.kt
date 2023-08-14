package cn.xdwanj.gsgm.config

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