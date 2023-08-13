package cn.xdwanj.gsgm.cli.operate

import kotlinx.coroutines.runBlocking
import org.dromara.hutool.core.io.file.FileUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import picocli.CommandLine.*
import java.io.File
import java.util.concurrent.Callable

@Deprecated("命令冗余")
@Scope(SCOPE_PROTOTYPE)
@Component
@Command(
  name = "install",
  mixinStandardHelpOptions = true,
  description = ["安装游戏"],
  sortOptions = false,
  usageHelpAutoWidth = true,
  sortSynopsis = false,
)
class InstallOperate : Callable<Int> {

  private val logger = LoggerFactory.getLogger(InstallOperate::class.java)

  @Option(
    names = ["--log"],
    description = ["只是打印过程，并不执行动作"],
    required = false,
  )
  var activeLog = false

  @Parameters(
    index = "0",
    description = ["游戏路径"],
    arity = "2..*",
  )
  var params: List<File> = emptyList()

  override fun call(): Int = runBlocking {
    logger.info("activeLog = $activeLog")
    logger.info("params = $params")

    val isFile = params.map { FileUtil.exists(it) }
    logger.info("isFile = $isFile")

    0
  }
}