package cn.xdwanj.gsgm.cli.operate

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import picocli.CommandLine
import picocli.CommandLine.IFactory

@SpringBootTest
class ListOperateTest {

  @Autowired
  private lateinit var iFactory: IFactory

  @Autowired
  private lateinit var listOperate: ListOperate

  @BeforeEach
  fun beforeEach() {
    println("----------------------------------------------------")
  }

  @AfterEach
  fun afterEach() {
    println("----------------------------------------------------")
  }

  @Test
  fun `gsgm list -hV`() {
    CommandLine(listOperate, iFactory).execute(
      "-hV"
    )
  }
}