package cn.xdwanj.gsgm.cli.operate

import cn.xdwanj.gsgm.cli.controller.CleanController
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import picocli.CommandLine.Command
import java.util.concurrent.Callable

@Scope(SCOPE_PROTOTYPE)
@Component
@Command(
  name = "clean",
  mixinStandardHelpOptions = true,
  description = ["清空 Lutris 游戏库"],
  sortOptions = false,
  usageHelpAutoWidth = true,
  sortSynopsis = false,
)
class CleanOperate(
  private val cleanController: CleanController
) : Callable<Int> {

  override fun call(): Int = runBlocking {
    cleanController.cleanAction()
  }
}