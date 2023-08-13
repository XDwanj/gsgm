package cn.xdwanj.gsgm.util.extensions

import java.io.File

fun File.relationPath(parentFile: File): String {

  val parentPath = parentFile.absolutePath
  val currentPath = this.absolutePath

  if (currentPath.startsWith(parentPath).not()) {
    throw IllegalArgumentException("The relative address is not the parent directory of the current address: parentPath=$parentPath, currentPath=$currentPath")
  }

  return currentPath.removePrefix("$parentPath/")
}