package cn.xdwanj.kcolor

class SimpleAttribute(
  private val code: String,
) : Attribute() {

  override fun toString(): String {
    return code
  }
}