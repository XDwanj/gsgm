package cn.xdwanj.gsgm.cli.operate

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import picocli.CommandLine
import picocli.CommandLine.IFactory

@SpringBootTest
class CheckOperateTest {

  @Autowired
  private lateinit var checkOperate: CheckOperate

  @Autowired
  private lateinit var iFactory: IFactory

  @BeforeEach
  fun beforeEach() {
    println("----------------------------------------------------")
  }

  @AfterEach
  fun afterEach() {
    println("----------------------------------------------------")
  }

  @Test
  fun `gsgm check -hV`() {
    CommandLine(checkOperate, iFactory).execute(
      "-hV"
    )
  }
}