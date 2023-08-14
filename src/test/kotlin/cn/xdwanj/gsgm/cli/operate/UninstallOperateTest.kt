package cn.xdwanj.gsgm.cli.operate

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
  fun `gsgm uninstall -hV`() {
    CommandLine(uninstallOperate, iFactory).execute(
      "-hV"
    )
  }

  @Test
  fun `gsgm uninstall -l xxx`() {
    CommandLine(uninstallOperate, iFactory).execute(
      "-l", "/mnt/Frequent/HGame"
    )
  }

}