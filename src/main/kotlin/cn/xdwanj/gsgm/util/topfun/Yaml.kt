package cn.xdwanj.gsgm.util.topfun

// fun <T> yamlToBean(yaml: String, clazz: Class<T>): T {
//   val bean = IoUtil.toUtf8Reader(ByteArrayInputStream(yaml.toByteArray())).use {
//     YamlUtil.load(it, clazz)
//   }
//   return bean
// }
//
// fun <T> beanToYaml(bean: T): String {
//   return ByteArrayOutputStream().use { outputStream ->
//     IoUtil.toUtf8Writer(outputStream).use {
//       YamlUtil.dump(bean, it)
//     }
//     String(outputStream.toByteArray())
//       .split("\n")
//       .drop(1)
//       .joinToString(separator = "\n")
//   }
// }