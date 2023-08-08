package cn.xdwanj.gsgm.util

import org.dromara.hutool.core.io.IoUtil
import org.dromara.hutool.setting.yaml.YamlUtil
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

object YamlUtils {
  inline fun <reified T> toBean(yaml: String): T {
    val bean = IoUtil.toUtf8Reader(ByteArrayInputStream(yaml.toByteArray())).use {
      YamlUtil.load(it, T::class.java)
    }
    return bean
  }

  fun <T> toYaml(bean: T): String {
    return ByteArrayOutputStream().use { outputStream ->
      IoUtil.toUtf8Writer(outputStream).use {
        // val options = DumperOptions().apply {
        //   defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
        //   // indicatorIndent = 2
        // }
        YamlUtil.dump(bean, it)
      }
      String(outputStream.toByteArray())
        .split("\n")
        .drop(1)
        .joinToString(separator = "\n")
    }
  }
}