package cn.xdwanj.gsgm.cli.operate

import cn.xdwanj.gsgm.cli.controller.InstallController
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
  name = "install",
  mixinStandardHelpOptions = true,
  description = ["安装游戏到 Lutris 中"],
  sortOptions = false,
  usageHelpAutoWidth = true,
  sortSynopsis = false,
)
class InstallOperate(
  private val installController: InstallController,
) : Callable<Int> {

  private val logger = LoggerFactory.getLogger(InstallOperate::class.java)

  @Option(
    names = ["-f", "--force"],
    description = ["强制覆盖同步"],
    required = false,
  )
  var isForce: Boolean = false

  @Parameters(
    index = "0",
    arity = "1..*",
    description = ["Gsgm 游戏库位置"],
    paramLabel = "<libraryPath>"
  )
  var libraryPathList: List<File> = emptyList()

  override fun call(): Int = runBlocking {
    logger.info("libraryPath = {}", libraryPathList)

    if (isForce) {
      installController.installActionLibraryByForce(libraryPathList)
    } else {
      // todo: 需要考虑已经不存在的游戏要删除
      installController.installActionByLibrary(libraryPathList)
    }
  }
}