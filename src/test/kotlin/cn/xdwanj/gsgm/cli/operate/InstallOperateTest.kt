package cn.xdwanj.gsgm.cli.operate

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import picocli.CommandLine

@SpringBootTest
class InstallOperateTest {

  @Autowired
  private lateinit var iFactory: CommandLine.IFactory

  @Autowired
  private lateinit var installOperate: InstallOperate

  @BeforeEach
  fun beforeEach() {
    println("----------------------------------------------------")
  }

  @AfterEach
  fun afterEach() {
    println("----------------------------------------------------")
  }

  @Test
  fun `gsgm install -hV`() {
    CommandLine(installOperate, iFactory).execute(
      "-hV"
    )
  }

  @Disabled
  @Test
  fun `gsgm install xxx`() {
    CommandLine(installOperate, iFactory).execute(
      "/mnt/Frequent/HGame"
    )
  }

  // @Disabled
  @Test
  fun `gsgm install -f xxx`() {
    CommandLine(installOperate, iFactory).execute(
      "-f", "/mnt/Frequent/HGame"
    )
  }
}