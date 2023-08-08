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

/*
  pamac --version
  pamac --help, -h     [操作]
  pamac search         [选项] <软件包>
  pamac list           [选项] <软件包>
  pamac info           [选项] <软件包>
  pamac install        [选项] <软件包>
  pamac reinstall      [选项] <软件包>
  pamac remove         [选项] [软件包]
  pamac checkupdates   [选项]
  pamac update,upgrade [选项]
  pamac clone          [选项] <软件包>
  pamac build          [选项] [软件包]
  pamac clean          [选项]
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@Command(
  name = "gsgm",
  mixinStandardHelpOptions = true,
  version = ["gsgm v1.0.1"],
  usageHelpAutoWidth = true,
  description = ["Gsgm 管理工具"],
  subcommands = [
    SearchOperate::class,
    ListOperate::class,
    InfoOperate::class,
    // InstallOperate::class,
    InitOperate::class,
    RemoveOperate::class,
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