package cn.xdwanj.gsgm.base

import java.io.File

fun File.isGameDirectory() = this.name.substring(0..0) == "#"
    && this.name.substring(0..1) != "##"
    && this.isDirectory

fun File.isWindowsExe(): Boolean {
  val name = this.name.trim()
  if (this.isDirectory) {
    return false
  }
  return name.endsWith(".exe", true)
      || name.endsWith(".bat", true)
}