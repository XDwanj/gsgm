package cn.xdwanj.gsgm.cli.controller.impl

import cn.xdwanj.gsgm.cli.controller.CleanController
import cn.xdwanj.gsgm.cli.print.GsgmPrinter
import cn.xdwanj.gsgm.cli.print.output.printlnWaitTask
import cn.xdwanj.gsgm.service.LutrisService
import cn.xdwanj.kcolor.Ansi
import cn.xdwanj.kcolor.AttrTemplate
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Component

@Component
class CleanControllerImpl(
  private val lutrisService: LutrisService,
) : CleanController {
  override suspend fun cleanAction(): Int = coroutineScope {
    var commandCode = -1

    if (lutrisService.cleanLutrisDB()) {
      commandCode = 0
      val message = Ansi.colorize("pga db clean is success", AttrTemplate.greenText)
      GsgmPrinter.printlnWaitTask(message)
    } else {
      val message = Ansi.colorize("pga clean is error", AttrTemplate.redText)
      GsgmPrinter.printlnWaitTask(message)
    }

    if (lutrisService.cleanLutrisRunScript()) {
      commandCode = 0
      val message = Ansi.colorize("run script clean is success", AttrTemplate.greenText)
      GsgmPrinter.printlnWaitTask(message)
    } else {
      val message = Ansi.colorize("run script clean is error", AttrTemplate.redText)
      GsgmPrinter.printlnWaitTask(message)
    }

    if (lutrisService.cleanLutrisCover()) {
      commandCode = 0
      val message = Ansi.colorize("cover clean is success", AttrTemplate.greenText)
      GsgmPrinter.printlnWaitTask(message)
    } else {
      val message = Ansi.colorize("cover clean is error", AttrTemplate.redText)
      GsgmPrinter.printlnWaitTask(message)
    }


    if (lutrisService.cleanLutrisBanner()) {
      commandCode = 0
      val message = Ansi.colorize("banner clean is success", AttrTemplate.greenText)
      GsgmPrinter.printlnWaitTask(message)
    } else {
      val message = Ansi.colorize("banner clean is error", AttrTemplate.redText)
      GsgmPrinter.printlnWaitTask(message)
    }


    if (lutrisService.cleanLutrisIcon()) {
      commandCode = 0
      val message = Ansi.colorize("icon clean is success", AttrTemplate.greenText)
      GsgmPrinter.printlnWaitTask(message)
    } else {
      val message = Ansi.colorize("icon clean is error", AttrTemplate.redText)
      GsgmPrinter.printlnWaitTask(message)
    }

    commandCode
  }
}