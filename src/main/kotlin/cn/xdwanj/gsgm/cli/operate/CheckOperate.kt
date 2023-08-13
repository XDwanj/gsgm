package cn.xdwanj.gsgm.cli.operate

import cn.xdwanj.gsgm.cli.controller.CheckController
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
  name = "check",
  mixinStandardHelpOptions = true,
  description = ["检查游戏"],
  sortOptions = false,
  sortSynopsis = false,
  usageHelpAutoWidth = true,
)
class CheckOperate(
  private val checkController: CheckController,
) : Callable<Int> {

  private val logger = LoggerFactory.getLogger(CheckOperate::class.java)

  @Option(
    names = ["--is-library"],
    required = false,
    description = ["是否是 Gsgm 游戏库"],
  )
  var isLibrary: Boolean = false

  @Parameters(
    index = "0",
    arity = "1..*",
    description = ["游戏路径"],
    paramLabel = "<gamePath>",
  )
  lateinit var gamePathList: List<File>

  override fun call(): Int = runBlocking {
    // log
    logger.info(this@CheckOperate.toString())
    logger.info("isLibrary = $isLibrary")
    logger.info("gamePathList = $gamePathList")

    checkController.checkAction(isLibrary, gamePathList)
  }
}