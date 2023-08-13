package cn.xdwanj.gsgm.cli

import cn.xdwanj.gsgm.cli.operate.*
import cn.xdwanj.gsgm.service.LibraryService
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import picocli.AutoComplete.GenerateCompletion
import picocli.CommandLine.Command
import java.util.concurrent.Callable

@Scope(SCOPE_PROTOTYPE)
@Component
@Command(
  name = "gsgm",
  mixinStandardHelpOptions = true,
  version = ["gsgm v1.0.2"],
  usageHelpAutoWidth = true,
  description = ["Gsgm 管理工具"],
  subcommands = [
    SearchOperate::class,
    ListOperate::class,
    InfoOperate::class,
    // InstallOperate::class,
    InitOperate::class,
    UninstallOperate::class,
    CheckOperate::class,
    SyncOperate::class,
    CleanOperate::class,
    GenerateCompletion::class,
    // ManPageGenerator::class
  ],
)
class GsgmCommand(
  private val libraryService: LibraryService,
  private val objectMapper: ObjectMapper,
) : Callable<Int> {

  override fun call(): Int = runBlocking {
    // 检查是否启动了多个 Gsgm
    0
  }
}