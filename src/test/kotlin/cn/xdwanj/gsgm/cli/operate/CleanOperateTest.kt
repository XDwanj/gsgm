package cn.xdwanj.gsgm.cli.operate

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import picocli.CommandLine
import picocli.CommandLine.IFactory

@SpringBootTest
class CleanOperateTest {

  @Autowired
  private lateinit var cleanOperate: CleanOperate

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
  fun `gsgm clean -hV`() {
    CommandLine(cleanOperate, iFactory).execute(
      "-hV"
    )
  }

  @Test
  fun `gsgm clean xxx`() {
    CommandLine(cleanOperate, iFactory).execute()
  }
}