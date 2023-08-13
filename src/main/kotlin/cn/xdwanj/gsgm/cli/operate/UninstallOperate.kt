package cn.xdwanj.gsgm.cli.operate

import cn.xdwanj.gsgm.cli.controller.UninstallController
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
  name = "uninstall",
  mixinStandardHelpOptions = true,
  description = ["卸载游戏，但不删游戏文件"],
  sortOptions = false,
  usageHelpAutoWidth = true,
  sortSynopsis = false,
)
class UninstallOperate(
  private val uninstallController: UninstallController,
) : Callable<Int> {

  private val logger = LoggerFactory.getLogger(UninstallOperate::class.java)

  @Option(
    names = ["-l", "--library-path"],
    description = ["Gsgm 游戏库位置"],
    arity = "0..*",
    required = false,
  )
  var libraryPathList: List<File> = emptyList()

  @Parameters(
    index = "0",
    arity = "1..*",
    description = ["Gsgm 游戏id"],
    paramLabel = "<gsgm id>"
  )
  var gsgmIdList: List<Long> = emptyList()

  override fun call(): Int = runBlocking {
    logger.info("libraryPath = {}", libraryPathList)
    logger.info("gsgmIdList = {}", gsgmIdList)

    uninstallController.removeAction(libraryPathList, gsgmIdList)
  }
}

