package cn.xdwanj.gsgm.cli.operate

import cn.xdwanj.gsgm.cli.controller.ListController
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import picocli.CommandLine.Command
import java.util.concurrent.Callable

@Scope(SCOPE_PROTOTYPE)
@Component
@Command(
  name = "list",
  mixinStandardHelpOptions = true,
  description = ["已安装列表"],
  sortOptions = false,
  usageHelpAutoWidth = true,
  sortSynopsis = false,
)
class ListOperate(
  private val listController: ListController,
) : Callable<Int> {

  private val logger = LoggerFactory.getLogger(ListOperate::class.java)

  // @Option(
  //   names = ["-s", "--sort"],
  //   required = false,
  //   description = ["排序方式，默认按照 ID 排序, 例: [asc/desc]:[排序字段]%n  id:asc%n  asc:name%n  lastplayed:asc"],
  // )
  // var sortRule: String = ""

  override fun call(): Int = runBlocking {

    listController.listAction()

  }
}