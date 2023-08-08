package cn.xdwanj.gsgm.cli.operate

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import picocli.CommandLine
import picocli.CommandLine.IFactory

@SpringBootTest
class SyncOperateTest {

  @Autowired
  private lateinit var iFactory: IFactory

  @Autowired
  private lateinit var syncOperate: SyncOperate

  @BeforeEach
  fun beforeEach() {
    println("----------------------------------------------------")
  }

  @AfterEach
  fun afterEach() {
    println("----------------------------------------------------")
  }

  @Test
  fun `gsgm sync -hV`() {
    CommandLine(syncOperate, iFactory).execute(
      "-hV"
    )
  }
}