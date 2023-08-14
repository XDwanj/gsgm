package cn.xdwanj.gsgm.cli.operate

import cn.xdwanj.gsgm.cli.controller.SyncController
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import picocli.CommandLine.*
import java.io.File
import java.util.concurrent.Callable

@Scope(SCOPE_PROTOTYPE)
@Component
@Command(
  name = "sync",
  mixinStandardHelpOptions = true,
  description = ["将 Lutris 数据同步到 Gsgm"],
  sortOptions = false,
  usageHelpAutoWidth = true,
  sortSynopsis = false,
)
class SyncOperate(
  private val syncController: SyncController,
) : Callable<Int> {

  private val logger = LoggerFactory.getLogger(SyncOperate::class.java)

  // @ArgGroup(
  //   heading = "数据同步方向，默认 Gsgm => Lutris%n",
  //   multiplicity = "0..1",
  //   exclusive = true
  // )
  // var dataSyncDirection: DataSyncDirectionGroup? = null

  @Option(
    names = ["-f", "--force"],
    description = ["强制覆盖同步"],
    required = false,
  )
  var isForce: Boolean = false

  @Parameters(
    index = "0",
    arity = "1..*",
    paramLabel = "<libraryPath>",
    description = ["游戏库位置"],
  )
  lateinit var libraryPathList: List<File>

  override fun call(): Int = runBlocking {
    logger.info("libraryPathList = {}", libraryPathList)
    logger.info("isForce = {}", isForce)

    if (isForce) {
      syncController.syncActionLTGByForce(libraryPathList)
    } else {
      syncController.syncActionLTG(libraryPathList)
    }
  }
}



