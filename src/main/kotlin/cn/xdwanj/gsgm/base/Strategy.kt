package cn.xdwanj.gsgm.base

import org.dromara.hutool.core.io.file.FileUtil
import java.io.File

fun File.isGameDirectory() = this.name.startsWith("@")
    && this.name.startsWith("@@").not()
    && this.isDirectory

fun File.isIgnoreDirectory() = this.name.startsWith("@@")

fun File.isGameGroupDirectory(): Boolean {
  if (isFile) return false
  if (isIgnoreDirectory()) return false
  if (!isGameDirectory()) return false

  val exists = FileUtil.exists("${this.absolutePath}/${GsgmFileName.IS_GROUP}")
  return !exists.not()
}

fun File.isWindowsExe(): Boolean {
  val name = this.name.trim()
  if (this.isDirectory) {
    return false
  }
  return name.endsWith(".exe", true)
      || name.endsWith(".bat", true)
}