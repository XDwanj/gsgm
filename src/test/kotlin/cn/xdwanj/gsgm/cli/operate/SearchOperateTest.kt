package cn.xdwanj.gsgm.cli.operate

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import picocli.CommandLine

@SpringBootTest
class SearchOperateTest {

  @Autowired
  private lateinit var searchOperate: SearchOperate

  @Autowired
  private lateinit var iFactory: CommandLine.IFactory

  @BeforeEach
  fun beforeEach() {
    println("----------------------------------------------------")
  }

  @AfterEach
  fun afterEach() {
    println("----------------------------------------------------")
  }

  @Test
  fun `gsgm search -hV`() {
    CommandLine(searchOperate, iFactory).execute(
      "-hV"
    )
  }
}