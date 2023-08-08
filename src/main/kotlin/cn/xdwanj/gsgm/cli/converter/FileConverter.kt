package cn.xdwanj.gsgm.cli.converter

import org.dromara.hutool.core.io.file.FileUtil
import picocli.CommandLine.ITypeConverter
import java.io.File

/**
 * File 自定义转换器，同时校验文件或者文件夹是否存在
 *
 */
class FileConverter : ITypeConverter<File> {

  override fun convert(value: String?): File {
    if (value == null) throw IllegalArgumentException("路径为 null")
    if (value.isBlank()) throw IllegalArgumentException("路径内容为空")
    if (FileUtil.exists(value).not()) throw IllegalArgumentException("并非合法路径: $value")

    return File(value)
  }
}