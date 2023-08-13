package cn.xdwanj.gsgm.data.enum

import com.fasterxml.jackson.annotation.JsonValue


enum class Platform(
  val code: Int,
  @get:JsonValue
  val value: String,
  val runner: String,
) {
  Windows(0, "Windows", "wine"),

  Linux(1, "Linux", "linux"),

}