package cn.xdwanj.gsgm.cli.operate

import cn.xdwanj.gsgm.cli.controller.InitController
import cn.xdwanj.gsgm.cli.converter.FileConverter
import cn.xdwanj.gsgm.cli.group.GameTypeGroup
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import picocli.CommandLine.*
import picocli.CommandLine.Model.CommandSpec
import java.io.File
import java.util.concurrent.Callable

@Scope(SCOPE_PROTOTYPE)
@Component
@Command(
  name = "init",
  mixinStandardHelpOptions = true,
  description = ["游戏初始化"],
  sortOptions = false,
  usageHelpAutoWidth = true,
  sortSynopsis = false,
)
class InitOperate(
  private val initController: InitController,
) : Callable<Int> {

  private val logger = LoggerFactory.getLogger(InitOperate::class.java)

  @Spec
  private lateinit var spec: CommandSpec

  @ArgGroup(
    heading = "扫描的游戏类型%n",
    exclusive = true,
    multiplicity = "1",
  )
  lateinit var gameTypeGroup: GameTypeGroup

  @Option(
    names = ["--is-library"],
    required = false,
    description = ["是否是 Gsgm 游戏库"]
  )
  var isLibrary: Boolean = false

  @Option(
    required = false,
    names = ["-i", "--interactive-mode"],
    description = ["交互模式初始化游戏数据"]
  )
  var activeInteractiveMode: Boolean = false

  @Parameters(
    index = "0",
    arity = "1..*",
    converter = [FileConverter::class],
    description = ["游戏路径"],
    paramLabel = "<gameOrLibraryPath>"
  )
  lateinit var pathList: List<File>

  override fun call(): Int = runBlocking {
    logger.info("isLibrary = {}", isLibrary)
    logger.info("pathList = {}", pathList)
    logger.info("activeInteractiveMode = {}", activeInteractiveMode)
    logger.info("gameTypeGroup = {}", gameTypeGroup)

    if (activeInteractiveMode.not() && gameTypeGroup.isMixed) {
      throw ParameterException(spec.commandLine(), "非交互模式必须确认游戏平台如：--windows-all")
    }

    if (activeInteractiveMode) {
      initController.initActionInteract(isLibrary, gameTypeGroup, pathList)
    } else {
      initController.initActionDefault(isLibrary, gameTypeGroup, pathList)
    }
  }
}

