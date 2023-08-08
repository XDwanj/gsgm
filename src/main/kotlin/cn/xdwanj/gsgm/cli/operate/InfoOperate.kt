package cn.xdwanj.gsgm.cli.operate

import cn.xdwanj.gsgm.cli.controller.InfoController
import cn.xdwanj.gsgm.cli.converter.FileConverter
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import picocli.CommandLine.*
import java.io.File
import java.util.concurrent.Callable

@Scope(SCOPE_PROTOTYPE)
@Component
@Command(
  name = "info",
  mixinStandardHelpOptions = true,
  description = ["查询具体游戏"],
  sortOptions = false,
  usageHelpAutoWidth = true,
  sortSynopsis = false,
)
class InfoOperate(
  private val infoController: InfoController
) : Callable<Int> {

  @Option(
    names = ["-lp", "--library-path"],
    description = ["Gsgm 游戏库位置，为空则仅查 Lutris 数据库"],
    arity = "0..*",
    paramLabel = "<libraryPath>",
    required = false,
  )
  var libraryPathList: List<File> = emptyList()

  @Parameters(
    index = "0",
    description = ["gsgm id: 一般保存到游戏的 .gsgm 文件夹中 info.json 文件中"],
    arity = "1..*",
    paramLabel = "<gsgmId>"
  )
  var gsgmIdList: List<Long> = emptyList()

  override fun call(): Int = runBlocking {
    infoController.infoAction(libraryPathList, gsgmIdList)
  }
}