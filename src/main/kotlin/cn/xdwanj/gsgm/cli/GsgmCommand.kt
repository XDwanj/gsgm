package cn.xdwanj.gsgm.cli

import cn.xdwanj.gsgm.cli.operate.*
import cn.xdwanj.gsgm.util.extensions.asyncMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.dromara.hutool.core.data.id.IdUtil
import org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import picocli.AutoComplete.GenerateCompletion
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import java.lang.Thread.sleep
import java.util.UUID
import java.util.concurrent.Callable
import kotlin.system.measureTimeMillis

@Scope(SCOPE_PROTOTYPE)
@Component
@Command(
  name = "gsgm",
  mixinStandardHelpOptions = true,
  version = ["gsgm v1.0.4"],
  usageHelpAutoWidth = true,
  description = ["Gsgm 管理工具"],
  subcommands = [
    SearchOperate::class,
    ListOperate::class,
    InfoOperate::class,
    InitOperate::class,
    InstallOperate::class,
    UninstallOperate::class,
    CheckOperate::class,
    SyncOperate::class,
    CleanOperate::class,
    GenerateCompletion::class,
    // ManPageGenerator::class
  ],
)
class GsgmCommand : Callable<Int> {

  @Option(
    // names = ["--spring.profiles.active=dev"]
    description = ["设置启动模式，可选项：[dev | pro]"],
    hidden = true,
    names = ["--spring.profiles.active"]
  )
  var active: String = "pro"

  override fun call(): Int = runBlocking {
    // 检查是否启动了多个 Gsgm
    0
  }
}