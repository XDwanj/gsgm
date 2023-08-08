package cn.xdwanj.gsgm.cli.operate

import cn.xdwanj.gsgm.cli.controller.SearchController
import cn.xdwanj.gsgm.cli.converter.FileConverter
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
  name = "search",
  description = ["查询"],
  mixinStandardHelpOptions = true,
  sortOptions = false,
  usageHelpAutoWidth = true,
  sortSynopsis = false,
)
class SearchOperate(
  private val searchController: SearchController,
) : Callable<Int> {

  private val logger = LoggerFactory.getLogger(SearchOperate::class.java)

  @Option(
    names = ["-k", "--keyword"],
    required = true,
    description = ["查询关键字"]
  )
  lateinit var keyword: String

  @Parameters(
    index = "0",
    description = ["游戏库位置"],
    paramLabel = "<libraryPath>",
    arity = "1..*"
  )
  var libraryPathList: List<File> = emptyList()

  override fun call(): Int = runBlocking {
    logger.info("keyword = {}", keyword)
    logger.info("libraryPathList = {}", libraryPathList)

    searchController.searchAction(keyword, libraryPathList)
  }
}