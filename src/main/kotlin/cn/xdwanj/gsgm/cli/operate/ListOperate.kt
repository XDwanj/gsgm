package cn.xdwanj.gsgm.cli.operate

import cn.xdwanj.gsgm.cli.controller.ListController
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import picocli.CommandLine.Command
import picocli.CommandLine.Parameters
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

  @Parameters(
    index = "0",
    arity = "0..1",
    description = ["查询关键字"],
  )
  var keyword: String = ""

  override fun call(): Int = runBlocking {
    logger.info("keyword=$keyword")

    listController.listActionByKeyword(keyword)
  }
}