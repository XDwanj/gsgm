package cn.xdwanj.gsgm.service.impl

import cn.xdwanj.gsgm.base.*
import cn.xdwanj.gsgm.base.GsgmFileName.COVER_NAME
import cn.xdwanj.gsgm.data.dto.CommonState
import cn.xdwanj.gsgm.data.dto.GameGroupDto
import cn.xdwanj.gsgm.data.entity.LutrisGame
import cn.xdwanj.gsgm.data.enum.LocaleCharSet
import cn.xdwanj.gsgm.data.enum.Platform
import cn.xdwanj.gsgm.data.script.LutrisRunScript
import cn.xdwanj.gsgm.data.setting.GsgmHistory
import cn.xdwanj.gsgm.data.setting.GsgmInfo
import cn.xdwanj.gsgm.data.setting.GsgmSetting
import cn.xdwanj.gsgm.data.setting.GsgmWrapper
import cn.xdwanj.gsgm.service.LibraryService
import cn.xdwanj.gsgm.util.YamlUtils
import cn.xdwanj.gsgm.util.extensions.relationPath
import cn.xdwanj.kcolor.Ansi.colorize
import cn.xdwanj.kcolor.AttrTemplate.redText
import cn.xdwanj.kcolor.AttrTemplate.yellowText
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import org.dromara.hutool.core.date.DateTime
import org.dromara.hutool.core.io.file.FileUtil
import org.dromara.hutool.json.JSONUtil
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.File

@Service
class LibraryServiceImpl(
  private val objectMapper: ObjectMapper,
) : LibraryService {
  private val logger = LoggerFactory.getLogger(LibraryServiceImpl::class.java)

  override suspend fun listGameFile(path: String): List<File> = withContext(Dispatchers.IO) {
    File(path).listFiles { it ->
      it.isGameDirectory()
    }?.toList() ?: emptyList()
  }

  override suspend fun deepGameFile(path: String): List<File> = withContext(Dispatchers.IO) {

    val tmpList = mutableListOf<File>()
    FileUtil.ls(path)
      .filter { it.isDirectory }
      .filter { it.isIgnoreDirectory().not() }
      .forEach { parentFile ->
        logger.debug("deepGameParent => {}", parentFile)
        walkGameFolder(parentFile) { tmpList += it }
      }

    tmpList
  }

  override suspend fun deepGroupFile(path: String): List<GameGroupDto> = withContext(Dispatchers.IO) {
    // 分组路径列表
    val groupFileList = mutableListOf<File>()
    FileUtil.ls(path)
      .filter { it.isDirectory }
      .filter { it.isIgnoreDirectory().not() }
      .forEach { parentFile ->
        logger.debug("deepGameGroupFile => {}", parentFile)
        walkGroupFolder(parentFile) { groupFileList += it }
      }

    // 分组的游戏列表
    val groupList = groupFileList.map {
      async {
        val gameFileList = deepGameFile(it.absolutePath)
        val groupName = it.name.trim()
        GameGroupDto(
          groupName = groupName,
          fileList = gameFileList.toMutableList()
        )
      }
    }.awaitAll()
      .toMutableList()

    // 默认组游戏列表
    val defaultGameFileList = mutableListOf<File>()
    FileUtil.ls(path)
      .filter { it.isDirectory }
      .filter { it.isIgnoreDirectory().not() }
      .filter { groupFileList.contains(it).not() }
      .forEach {
        walkGameFolder(it) { walkFolder ->
          if (groupFileList.contains(walkFolder).not()) {
            defaultGameFileList += walkFolder
          }
        }
      }
    val defaultGroupDto = GameGroupDto(fileList = defaultGameFileList)

    groupList += defaultGroupDto

    groupList
  }

  override suspend fun getGsgmWrapperByFileAndGroupName(
    gameFile: File,
    groupName: String,
  ): GsgmWrapper = withContext(Dispatchers.IO) {
    val gsgmPath = gameFile.absolutePath + "/.gsgm"

    if (FileUtil.exists("$gsgmPath/${GsgmFileName.INFO}").not())
      throw IllegalStateException("${GsgmFileName.INFO} 不存在: $gsgmPath")
    if (FileUtil.exists("$gsgmPath/${GsgmFileName.SETTING}").not())
      throw IllegalStateException("${GsgmFileName.SETTING} 不存在: $gsgmPath")

    val gsgmHistory = if (FileUtil.exists("$gsgmPath/${GsgmFileName.HISTORY}")) {
      try {
        objectMapper.readValue<GsgmHistory>(FileUtil.readUtf8String("$gsgmPath/${GsgmFileName.HISTORY}"))
      } catch (e: Exception) {
        println(e)
        null
      }
    } else {
      null
    }

    val gsgmWrapper = GsgmWrapper().also {
      it.gameFile = gameFile
      it.gsgmInfo = objectMapper.readValue<GsgmInfo>(FileUtil.readUtf8String("$gsgmPath/${GsgmFileName.INFO}"))
      it.gsgmSetting =
        objectMapper.readValue<GsgmSetting>(FileUtil.readUtf8String("$gsgmPath/${GsgmFileName.SETTING}"))
      it.gsgmHistory = gsgmHistory
      it.groupName = groupName
    }

    gsgmWrapper
  }

  override suspend fun getGsgmWrapperByLutrisGame(lutrisGame: LutrisGame): GsgmWrapper = withContext(Dispatchers.IO) {
    val gsgmId = lutrisGame.slug!!.split("-").last().toLong()
    val runScriptPath = "${LutrisGlobalSettings.runScriptPath}/${lutrisGame.slug!!}.${LutrisExtName.SCRIPT_SUFFIX}"
    val lastplayed = lutrisGame.lastplayed
    val playtime = lutrisGame.playtime
    val gameFile = File(lutrisGame.directory!!)

    val yaml = FileUtil.readUtf8String(runScriptPath)
    val lutrisRunScript = YamlUtils.toBean<LutrisRunScript>(yaml)

    val localeCharSet = LocaleCharSet.values().first { lutrisRunScript.system?.locale == it.value }
    val platform = Platform.values().first { lutrisGame.platform == it.value }

    // gameFile
    // 这里不可以用 parentFile 获取 游戏目录

    // info
    val info = GsgmInfo(id = gsgmId)

    // setting
    val executeLocation = File(lutrisRunScript.game!!.exe!!).relationPath(gameFile)
    val setting = GsgmSetting(
      executeLocation = executeLocation,
      winePrefix = "${GsgmFileName.GSGM_DIR}/${File(lutrisRunScript.game?.prefix!!).name}",
      localeCharSet = localeCharSet,
      platform = platform,
    )

    // history
    val currentLastGameMoment = if (lastplayed != null) {
      DateTime(lastplayed * 1000)
    } else {
      null
    }

    val history = GsgmHistory(
      lastGameMoment = currentLastGameMoment,
      gameTime = playtime
    )

    GsgmWrapper(
      gsgmSetting = setting,
      gsgmHistory = history,
      gsgmInfo = info,
      gameFile = gameFile,
    )
  }

  override suspend fun checkGameGsgmDir(gameFile: File): CommonState<File> = withContext(Dispatchers.IO) {
    val exits = FileUtil.exists("${gameFile.absolutePath}/${GsgmFileName.GSGM_DIR}")
    val commonState = CommonState(gameFile)

    if (!exits) {
      commonState.level++
      commonState.messageList += colorize("${GsgmFileName.GSGM_DIR} 不存在", redText)
    }

    commonState
  }

  override suspend fun checkGameInfo(gameFile: File): CommonState<File> = withContext(Dispatchers.IO) {
    val commonState = CommonState(gameFile)

    // check file
    if (!FileUtil.exists("${gameFile.absolutePath}/${GsgmFileName.GSGM_DIR}/${GsgmFileName.INFO}")) {
      commonState.messageList += colorize("${GsgmFileName.INFO} 不存在", redText)
      commonState.level += 1
    }

    // check json parse
    try {
      val info = objectMapper.readValue<GsgmInfo>(
        FileUtil.readUtf8String("${gameFile.absolutePath}/${GsgmFileName.GSGM_DIR}/${GsgmFileName.INFO}")
      )

      if (info.id == null || info.id == 0L) {
        // info.id ?: run {
        commonState.level++
        commonState.messageList += colorize("${GsgmFileName.INFO}: id 字段为空", redText)
      }

    } catch (e: Exception) {
      commonState.level++
      commonState.messageList += colorize("${GsgmFileName.INFO} 格式异常: ${e.message}", redText)
    }

    commonState
  }

  override suspend fun checkGameSetting(gameFile: File): CommonState<File> = withContext(Dispatchers.IO) {
    val commonState = CommonState(gameFile)

    // check file
    if (!FileUtil.exists("${gameFile.absolutePath}/${GsgmFileName.GSGM_DIR}/${GsgmFileName.SETTING}")) {
      commonState.messageList += colorize("${GsgmFileName.SETTING} 不存在", redText)
      commonState.level += 1
    }

    // check json parse
    try {
      val setting = objectMapper.readValue<GsgmSetting>(
        FileUtil.readUtf8String("${gameFile.absolutePath}/${GsgmFileName.GSGM_DIR}/${GsgmFileName.SETTING}")
      )

      if (setting.executeLocation.isNullOrBlank()) {
        commonState.level++
        commonState.messageList += colorize("${GsgmFileName.SETTING}: executeLocation 字段为空", redText)
      }
      if (setting.winePrefix.isNullOrBlank()) {
        commonState.level++
        commonState.messageList += colorize("${GsgmFileName.SETTING}: winePrefix 字段为空", redText)
      }
      if (setting.platform == null) {
        commonState.level++
        commonState.messageList += colorize("${GsgmFileName.SETTING}: platform 字段为空", redText)
      }
      if (setting.localeCharSet == null) {
        commonState.level++
        commonState.messageList += colorize("${GsgmFileName.SETTING}: localeCharSet 字段为空", redText)
      }
    } catch (e: Exception) {
      commonState.level++
      commonState.messageList += colorize("${GsgmFileName.INFO} 格式异常: ${e.message}", redText)
    }

    commonState
  }

  override suspend fun checkGameHistory(gameFile: File): CommonState<File> = withContext(Dispatchers.IO) {
    val commonState = CommonState(gameFile)
    if (!FileUtil.exists("${gameFile.absolutePath}/${GsgmFileName.GSGM_DIR}/${GsgmFileName.HISTORY}")) {
      commonState.messageList += colorize("${GsgmFileName.HISTORY} 不存在", yellowText)
    }
    commonState
  }

  override suspend fun checkGameResource(gameFile: File): CommonState<File> = withContext(Dispatchers.IO) {
    val commonState = CommonState(gameFile)

    val picturePath = "${gameFile.absolutePath}/${GsgmFileName.GSGM_DIR}/$COVER_NAME"

    if (
      FileUtil.exists("$picturePath.png").not()
      && FileUtil.exists("$picturePath.jpg").not()
      && FileUtil.exists("$picturePath.jpeg").not()
      && FileUtil.exists("$picturePath.svg").not()
      && FileUtil.exists("$picturePath.gif").not()
    ) {
      commonState.level += 1
      commonState.messageList += colorize("图片资源 $COVER_NAME.xxx 未找到", redText)
    }

    commonState
  }

  override suspend fun installGsgmInfo(wrapper: GsgmWrapper): CommonState<Unit> = withContext(Dispatchers.IO) {
    val gameFile = wrapper.gameFile!!
    val info = wrapper.gsgmInfo!!.copy(description = null)
    val infoPath = "${gameFile.absolutePath}/${GsgmFileName.GSGM_DIR}/${GsgmFileName.INFO}"
    val infoJson = objectMapper.writeValueAsString(info).let { JSONUtil.formatJsonStr(it) }
    logger.info("info=$info")
    val state = CommonState<Unit>()

    try {
      FileUtil.writeUtf8String(infoJson, infoPath)
      val msg = colorize("${GsgmFileName.INFO} coverage successful")
      state.messageList += msg
    } catch (e: Exception) {
      val log = "info.json overwriting failed"
      logger.error(log, e)
      state.messageList += colorize(log, redText)
      state.level++
    }

    state
  }

  override suspend fun installGsgmSetting(wrapper: GsgmWrapper): CommonState<Unit> = withContext(Dispatchers.IO) {
    val gameFile = wrapper.gameFile!!
    val setting = wrapper.gsgmSetting!!
    val settingPath = "${gameFile.absolutePath}/${GsgmFileName.GSGM_DIR}/${GsgmFileName.SETTING}"
    val settingJson = objectMapper.writeValueAsString(setting).let { JSONUtil.formatJsonStr(it) }
    val state = CommonState<Unit>()

    logger.info("setting=$setting")

    try {
      FileUtil.writeUtf8String(settingJson, settingPath)
      val msg = colorize("${GsgmFileName.SETTING} coverage successful")
      state.messageList += msg
    } catch (e: Exception) {
      val log = "${GsgmFileName.SETTING} overwriting failed"
      logger.error(log, e)
      state.messageList += colorize(log, redText)
      state.level++
    }

    state
  }

  override suspend fun installGsgmHistory(wrapper: GsgmWrapper): CommonState<Unit> = withContext(Dispatchers.IO) {
    val gameFile = wrapper.gameFile!!
    val history = wrapper.gsgmHistory
    val historyPath = "${gameFile.absolutePath}/${GsgmFileName.GSGM_DIR}/${GsgmFileName.HISTORY}"
    val historyJson = objectMapper.writeValueAsString(history).let { JSONUtil.formatJsonStr(it) }
    val state = CommonState<Unit>()

    logger.info("history=$history")

    try {
      FileUtil.writeUtf8String(historyJson, historyPath)
      val msg = colorize("${GsgmFileName.HISTORY} coverage successful")
      state.messageList += msg
    } catch (e: Exception) {
      val log = "${GsgmFileName.HISTORY} overwriting failed"
      logger.error(log, e)
      state.messageList += colorize(log, redText)
      state.level++
    }

    state
  }

  override suspend fun assertAll(gameFile: File) = withContext(Dispatchers.IO) {

    val resultState = listOf(
      async { checkGameInfo(gameFile) },
      async { checkGameSetting(gameFile) },
      async { checkGameHistory(gameFile) },
      async { checkGameResource(gameFile) },
    ).awaitAll()
      .reduce { first, second -> first + second }

    if (resultState.level > 0) {
      resultState.messageList.forEach(::println)
      throw IllegalStateException("检查失败，游戏库格式错误: ${gameFile.absoluteFile}")
    }
  }

  // ------------------------------------------- private function

  /**
   * 这个方法只能放在第二层调用
   *
   * @param folder
   * @param block
   */
  private fun walkGameFolder(folder: File, block: (File) -> Unit) {
    if (folder.isGameDirectory()) {
      val subFiles = folder.listFiles() ?: emptyArray()
      if (subFiles.isNotEmpty()) {
        subFiles.filter { it.isDirectory }
          .forEach { walkGameFolder(it, block) }
      }
    } else {
      block(folder)
    }
  }

  /**
   * 这个方法只能放在第二层调用
   *
   * @param folder
   * @param block
   */
  fun walkGroupFolder(folder: File, block: (File) -> Unit) {

    if (folder.isGameDirectory()) {
      // 检查是否存在 .is-group 文件
      val isGroupFile = File(folder, GsgmFileName.IS_GROUP)
      if (isGroupFile.exists()) {
        // 如果存在，添加进 list
        block(folder)
      } else {
        // 如果不存在，则是普通文件夹
        folder.listFiles()
          ?.filter { it.isDirectory }
          ?.forEach { subFolder ->
            walkGroupFolder(subFolder, block)
          }
      }
    }

  }
}