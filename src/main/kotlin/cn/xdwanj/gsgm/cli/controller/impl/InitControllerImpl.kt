package cn.xdwanj.gsgm.cli.controller.impl

import cn.xdwanj.gsgm.base.GsgmFileName
import cn.xdwanj.gsgm.cli.controller.InitController
import cn.xdwanj.gsgm.cli.group.GameTypeGroup
import cn.xdwanj.gsgm.cli.print.GsgmPrinter
import cn.xdwanj.gsgm.cli.print.input.inputReadLn
import cn.xdwanj.gsgm.cli.print.output.printlnGsgmGameDesc
import cn.xdwanj.gsgm.cli.print.output.printlnLevelTask
import cn.xdwanj.gsgm.cli.print.output.printlnLine
import cn.xdwanj.gsgm.data.Defaults
import cn.xdwanj.gsgm.data.enum.Platform
import cn.xdwanj.gsgm.data.setting.GsgmInfo
import cn.xdwanj.gsgm.data.setting.GsgmSetting
import cn.xdwanj.gsgm.data.setting.GsgmWrapper
import cn.xdwanj.gsgm.service.LibraryService
import cn.xdwanj.gsgm.util.extensions.relationPath
import cn.xdwanj.kcolor.Ansi.colorize
import cn.xdwanj.kcolor.AttrTemplate.purpleText
import cn.xdwanj.kcolor.AttrTemplate.redText
import cn.xdwanj.kcolor.AttrTemplate.underline
import cn.xdwanj.kcolor.AttrTemplate.yellowText
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.coroutineScope
import org.dromara.hutool.core.data.id.IdUtil
import org.dromara.hutool.core.io.file.FileUtil
import org.dromara.hutool.json.JSONUtil
import org.springframework.stereotype.Component
import java.io.File

@Component
class InitControllerImpl(
  private val libraryService: LibraryService,
  private val objectMapper: ObjectMapper,
) : InitController {

  override suspend fun initActionByInteract(
    isLibrary: Boolean,
    gameTypeGroup: GameTypeGroup,
    pathList: List<File>,
  ): Int = coroutineScope {

    // get gameFileList
    val gameFileList: List<File> = if (isLibrary) {
      pathList.map { libraryService.deepGameFile(it.absolutePath) }.flatten()
    } else {
      pathList
    }

    val platform: Platform? = getPlatformAll(gameTypeGroup)

    // 交互模式
    gameFileList.forEach { gameFile ->
      GsgmPrinter.printlnLine()

      val info = Defaults.defaultGsgmInfo.copy(id = IdUtil.getSnowflakeNextId())

      printlnGsgmGameDesc(GsgmWrapper(gsgmInfo = info, gameFile = gameFile, gsgmSetting = GsgmSetting(platform = null)))

      // 获取当前游戏平台
      val currentPlatform: Platform = platform ?: run {
        val inputTip = """
          Game platform options (serial number)
            ${colorize(Platform.Windows.code.toString(), purpleText)} ${colorize(Platform.Windows.value, underline)}
            ${colorize(Platform.Linux.code.toString(), purpleText)} ${colorize(Platform.Linux.value, underline)}
          please enter: 
        """.trimIndent()
        val currentPlatform =
          GsgmPrinter.inputReadLn(inputTip = inputTip,
            errorTip = colorize("The serial number is invalid, please enter a valid serial number", redText),
            condition = { it.toInt() == Platform.Windows.code || it.toInt() == Platform.Linux.code },
            converter = {
              when (it.lowercase()) {
                Platform.Windows.code.toString() -> Platform.Windows
                Platform.Linux.code.toString() -> Platform.Linux
                else -> Platform.Windows
              }
            })
        currentPlatform
      }

      // 获取游戏可执行文件列表
      val executableFileList = listExecutableFile(gameFile, currentPlatform)

      // 选择可执行文件
      println("Executable file (serial number):")
      executableFileList.forEachIndexed { index, file ->
        val relationPath = file.relationPath(gameFile)
        println("${colorize(index.toString(), purpleText)} ${colorize(relationPath, underline)}")
      }
      val exeFile = if (executableFileList.isNotEmpty()) {
        GsgmPrinter.inputReadLn(inputTip = "Select executable file (serial number): ",
          errorTip = colorize("The serial number entered is invalid!!", redText),
          condition = { it.toInt() in 0 until executableFileList.count() },
          converter = { executableFileList[it.toInt()] }
        )
      } else {
        null
      }

      val relationPath = exeFile?.relationPath(gameFile)
      if (relationPath.isNullOrBlank()) {
        GsgmPrinter.printlnLevelTask("Executable not found")
      } else {
        println("Executable selected: ${colorize(relationPath, underline)}")
      }

      val setting = Defaults.defaultGsgmSetting.copy(executeLocation = relationPath, platform = currentPlatform)

      val log = initGsgm(gameFile, info, setting)
      println(colorize(log, yellowText))

    }

    0
  }

  private fun getPlatformAll(gameTypeGroup: GameTypeGroup) = if (gameTypeGroup.isWindowsAll) {
    Platform.Windows
  } else if (gameTypeGroup.isLinuxAll) {
    Platform.Linux
  } else {
    null
  }

  private fun listExecutableFile(gameFile: File, platform: Platform): List<File> {
    // windows
    return when (platform) {
      Platform.Windows -> {
        // FileUtil.ls(gameFile.absolutePath)
        //   .filter { it.isFile }
        //   .filter { it.name.trim().endsWith(".exe", true) || it.name.trim().endsWith(".bat", true) }

        // 这里只获取两层目录的文件
        FileUtil.loopFiles(gameFile, 2) {
          it.name.trim().endsWith(".exe", true)
              || it.name.trim().endsWith(".bat", true)
        }
      }

      Platform.Linux -> {
        FileUtil.loopFiles(gameFile, 2, null)
      }
    }
  }

  private fun initGsgm(
    gameFile: File,
    gsgmInfo: GsgmInfo,
    gsgmSetting: GsgmSetting,
  ): String {
    val gsgmDirPath = "${gameFile.absolutePath}/${GsgmFileName.GSGM_DIR}".also { FileUtil.mkdir(it) }
    val infoPath = "$gsgmDirPath/${GsgmFileName.INFO}"
    val settingPath = "$gsgmDirPath/${GsgmFileName.SETTING}"

    val infoJson = objectMapper.writeValueAsString(gsgmInfo).let { JSONUtil.formatJsonStr(it) }
    val settingJson = objectMapper.writeValueAsString(gsgmSetting).let { JSONUtil.formatJsonStr(it) }

    FileUtil.writeUtf8String(infoJson, infoPath)
    FileUtil.writeUtf8String(settingJson, settingPath)

    return objectMapper.writeValueAsString(
      GsgmWrapper(gameFile = gameFile, gsgmInfo = gsgmInfo, gsgmSetting = gsgmSetting)
    ).let { JSONUtil.formatJsonStr(it) }
  }

  override suspend fun initActionDefault(
    isLibrary: Boolean,
    gameTypeGroup: GameTypeGroup,
    pathList: List<File>,
  ) = coroutineScope {

    // get gameFileList
    val gameFileList: List<File> = if (isLibrary) {
      pathList.map { libraryService.deepGameFile(it.absolutePath) }.flatten()
    } else {
      pathList
    }

    // 非交互模式
    val info = Defaults.defaultGsgmInfo.copy(id = IdUtil.getSnowflakeNextId())

    val platform: Platform? = getPlatformAll(gameTypeGroup)
    // 此处 platform 不可能为 null
    val setting = Defaults.defaultGsgmSetting.copy(platform = platform!!)

    gameFileList.forEach {
      printlnGsgmGameDesc(GsgmWrapper(gameFile = it, gsgmInfo = info, gsgmSetting = setting))
      val log = initGsgm(it, info, setting)
      println(colorize(log, yellowText))
    }

    0
  }
}