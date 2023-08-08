package cn.xdwanj.gsgm.cli.controller.impl

import cn.xdwanj.gsgm.base.GsgmFileName
import cn.xdwanj.gsgm.cli.controller.InitController
import cn.xdwanj.gsgm.cli.group.GameTypeGroup
import cn.xdwanj.gsgm.data.Defaults
import cn.xdwanj.gsgm.data.enum.Platform
import cn.xdwanj.gsgm.data.setting.GsgmInfo
import cn.xdwanj.gsgm.data.setting.GsgmSetting
import cn.xdwanj.gsgm.data.setting.GsgmWrapper
import cn.xdwanj.gsgm.service.LibraryService
import cn.xdwanj.gsgm.util.topfun.inputReadLn
import cn.xdwanj.gsgm.util.topfun.printlnGsgmGameDesc
import cn.xdwanj.gsgm.util.topfun.printlnLine
import cn.xdwanj.kcolor.Ansi
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

  override suspend fun initActionInteract(
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

    gameFileList.forEach { gameFile ->
      printlnLine()
      // 交互模式

      val info = Defaults.defaultGsgmInfo.copy(id = IdUtil.getSnowflakeNextId())

      printlnGsgmGameDesc(GsgmWrapper(gsgmInfo = info, gameFile = gameFile))

      // 获取当前游戏平台
      val currentPlatform: Platform = platform ?: run {
        val inputTip = """
          游戏平台可选项(序号):
            ${Platform.Windows.code} ${Platform.Windows.value}
            ${Platform.Linux.code} ${Platform.Linux.value}
          输入: 
        """.trimIndent()
        val currentPlatform =
          inputReadLn(inputTip = inputTip, errorTip = Ansi.colorize("序号无效，请输入有效序号", redText),
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
      println("可执行文件(序号):")
      executableFileList.forEachIndexed { index, file ->
        println("${Ansi.colorize(index.toString(), purpleText)} ${Ansi.colorize(file.name, underline)}")
      }
      val exeFile =
        inputReadLn(inputTip = "选择可执行文件(序号): ", errorTip = Ansi.colorize("输入序号无效!!", redText),
          condition = {
            val index = it.toInt()
            index in 0 until executableFileList.count()
          }, converter = {
            val index = it.toInt()
            executableFileList[index]
          })
      println("已选择可执行文件 ${Ansi.colorize(exeFile.name, underline)}")

      val setting = Defaults.defaultGsgmSetting.copy(executeLocation = exeFile.name, platform = currentPlatform)

      val log = initGsgm(gameFile, info, setting)
      println(Ansi.colorize(log, yellowText))

      printlnLine()
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
        FileUtil.ls(gameFile.absolutePath)
          .filter { it.isFile }
          .filter { it.name.trim().endsWith(".exe", true) || it.name.trim().endsWith(".bat", true) }
      }

      Platform.Linux -> {
        FileUtil.ls(gameFile.absolutePath)
          .filter { it.isFile }
      }
    }
  }

  private fun initGsgm(
    gameFile: File,
    gsgmInfo: GsgmInfo,
    gsgmSetting: GsgmSetting,
  ): String {
    val gsgmDirPath = "${gameFile.absolutePath}/${GsgmFileName.GSGM_DIR}"
    val infoPath = "$gsgmDirPath/${GsgmFileName.INFO}"
    val settingPath = "$gsgmDirPath/${GsgmFileName.SETTING}"

    FileUtil.mkdir(gsgmDirPath)

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
      println(Ansi.colorize(log, yellowText))
    }

    0
  }
}