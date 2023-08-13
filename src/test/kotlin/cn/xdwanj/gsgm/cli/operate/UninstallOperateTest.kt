package cn.xdwanj.gsgm.cli.operate

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import picocli.CommandLine

@SpringBootTest
class UninstallOperateTest {

  @Autowired
  private lateinit var iFactory: CommandLine.IFactory

  @Autowired
  private lateinit var uninstallOperate: UninstallOperate

  @BeforeEach
  fun beforeEach() {
    println("----------------------------------------------------")
  }

  @AfterEach
  fun afterEach() {
    println("----------------------------------------------------")
  }

  @Test
  fun `gsgm remove -hV`(): Unit = runBlocking {
    CommandLine(uninstallOperate, iFactory).execute(
      "-hV"
    )
  }

}