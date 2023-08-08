package cn.xdwanj.gsgm.service.impl

import cn.xdwanj.gsgm.service.GsgmService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class GsgmServiceImpl : GsgmService {

  private val logger = LoggerFactory.getLogger(GsgmService::class.java)

  // override suspend fun checkAction(
  //   isLibrary: Boolean,
  //   gamePathList: List<File>
  // ): Int = coroutineScope {
  //   val checkResult = if (isLibrary) {
  //     gamePathList
  //       .map { async(Dispatchers.IO) { libraryService.deepGameFile(it.absolutePath) } }
  //       .awaitAll()
  //       .flatten()
  //       .map {
  //         async(Dispatchers.IO) { checkAllState(it) }
  //       }
  //       .awaitAll()
  //   } else {
  //     gamePathList.map {
  //       async { checkAllState(it) }
  //     }.awaitAll()
  //   }
  //
  //   // print
  //   checkResult.sortedBy { it.level }
  //     .forEach { state ->
  //       println(Ansi.colorize(state.gameFile.toString(), greenText))
  //       state.messageList.forEach {
  //         println("    $it")
  //       }
  //     }
  //
  //   0
  // }

  // private suspend fun checkAllState(
  //   gameFile: File
  // ): CheckState = withContext(Dispatchers.Default) {
  //   val checkState = CheckState(gameFile)
  //
  //   listOf(
  //     async { libraryService.checkGameGsgm(gameFile) },
  //     async { libraryService.checkGameInfo(gameFile) },
  //     async { libraryService.checkGameSetting(gameFile) },
  //     async { libraryService.checkGameHistory(gameFile) },
  //     async { libraryService.checkGameResource(gameFile) },
  //   ).awaitAll()
  //     .forEach { checkState += it }
  //
  //   checkState
  // }

  // override suspend fun cleanAction(
  //   lutrisSettingGroup: LutrisSettingGroup?,
  // ): Int = coroutineScope {
  //   val pgaDbPath = lutrisSettingGroup?.pgaDbPath ?: File(LutrisGlobalSettings.pgaDbPath)
  //   val coverPath = lutrisSettingGroup?.coverPath ?: File(LutrisGlobalSettings.coverartPath)
  //   val bannerPath = lutrisSettingGroup?.bannerPath ?: File(LutrisGlobalSettings.bannerPath)
  //   val iconPath = lutrisSettingGroup?.iconPath ?: File(LutrisGlobalSettings.iconPath)
  //   var commandCode = -1
  //
  //   if (lutrisService.cleanLutrisDB(pgaDbPath)) {
  //     commandCode = 0
  //     println(Ansi.colorize("pga clean is success", greenText))
  //   } else {
  //     println(Ansi.colorize("pga clean is error", redText))
  //   }
  //
  //   if (lutrisService.cleanLutrisCover(coverPath)) {
  //     commandCode = 0
  //     println(Ansi.colorize("cover clean is success", greenText))
  //   } else {
  //     println(Ansi.colorize("cover clean is error", redText))
  //   }
  //
  //
  //   if (lutrisService.cleanLutrisBanner(bannerPath)) {
  //     commandCode = 0
  //     println(Ansi.colorize("banner clean is success", greenText))
  //   } else {
  //     println(Ansi.colorize("banner clean is error", redText))
  //   }
  //
  //
  //   if (lutrisService.cleanLutrisIcon(iconPath)) {
  //     commandCode = 0
  //     println(Ansi.colorize("icon clean is success", greenText))
  //   } else {
  //     println(Ansi.colorize("icon clean is error", redText))
  //   }
  //
  //   commandCode
  // }

  // override suspend fun infoAction(
  //   lutrisSettingGroup: LutrisSettingGroup?,
  //   libraryPath: File?,
  //   gsgmIdList: List<Long>
  // ): Int = coroutineScope {
  //
  //   val pgaDbPath = lutrisSettingGroup?.pgaDbPath
  //   if (pgaDbPath != null) {
  //     flexibleDataSource.switchEnable(pgaDbPath.absolutePath)
  //   }
  //
  //   val gsgmGameList = libraryPath?.let {
  //     libraryService.deepGameFile(libraryPath.absolutePath)
  //       .map { libraryService.getGameWrapperByFile(it) }
  //   } ?: emptyList()
  //
  //   gsgmIdList.map { gsgmId ->
  //     val slug = LutrisConstant.SLUG_PREFIX + gsgmId.toString()
  //     // lutris
  //     val lutrisGame: LutrisGame? = try {
  //       lutrisGameMapper.queryChain()
  //         .eq(LutrisGame::slug, slug)
  //         .one()
  //     } catch (e: Exception) {
  //       logger.error("", e)
  //       null
  //     }
  //
  //     // gsgm
  //     val gsgmGame: GameWrapper? = if (gsgmGameList.isNotEmpty()) {
  //       gsgmGameList.firstOrNull {
  //         it.gsgmInfo?.id == gsgmId
  //       }
  //     } else {
  //       null
  //     }
  //
  //     // print
  //     // printlnDetail(gsgmId, gsgmGame, lutrisGame)
  //   }
  //
  //
  //   println(Ansi.colorize("$"))
  //
  //
  //
  //
  //
  //
  // }

  // override suspend fun initAction(
  //   activeInteractiveMode: Boolean,
  //   isLibrary: Boolean,
  //   pathList: List<File>
  // ): Int {
  //
  //   // get gameFileList
  //   val gameFileList: List<File> = if (isLibrary) {
  //     pathList.map {
  //       libraryService.deepGameFile(it.absolutePath)
  //     }.flatten()
  //   } else {
  //     pathList
  //   }
  //
  //   val isThePlatformSelectedForAllGames =
  //     inputReadLn("是否选定所有游戏的平台(y/n): ", Ansi.colorize("请输入(y/n)", redText),
  //       condition = {
  //         val text = it.lowercase()
  //         text == "y" || text == "yes" || text == "n" || text == "no"
  //       },
  //       converter = {
  //         val text = it.lowercase()
  //         when (text) {
  //           "y" -> true
  //           "yes" -> true
  //           "n" -> false
  //           "no" -> false
  //           else -> false
  //         }
  //       }
  //     )
  //
  //   val platformInputTip =
  //     """
  //       游戏平台(序号):
  //         ${Platform.Windows.code}. ${Platform.Windows.value}
  //         ${Platform.Linux.code}. ${Platform.Linux.value}
  //       请输入序号:
  //     """.trimIndent()
  //   val platform = inputReadLn(platformInputTip, Ansi.colorize("序号输入有误，请重新输入!", redText),
  //     condition = {
  //       val text = it.lowercase()
  //       Platform.Windows.code.toString() == text || Platform.Linux.code.toString() == text
  //     }, converter = {
  //       when (it.lowercase()) {
  //         Platform.Windows.code.toString() -> Platform.Windows
  //         Platform.Linux.code.toString() -> Platform.Linux
  //         else -> Platform.Windows
  //       }
  //     })
  //
  //   // 非交互模式
  //   if (activeInteractiveMode.not()) {
  //     if (!isThePlatformSelectedForAllGames)  {
  //       val platformInputTip =
  //         """
  //       游戏平台(序号):
  //         ${Platform.Windows.code}. ${Platform.Windows.value}
  //         ${Platform.Linux.code}. ${Platform.Linux.value}
  //       请输入序号:
  //     """.trimIndent()
  //       val platform = inputReadLn(platformInputTip, Ansi.colorize("序号输入有误，请重新输入!", redText),
  //         condition = {
  //           val text = it.lowercase()
  //           Platform.Windows.code.toString() == text || Platform.Linux.code.toString() == text
  //         }, converter = {
  //           when (it.lowercase()) {
  //             Platform.Windows.code.toString() -> Platform.Windows
  //             Platform.Linux.code.toString() -> Platform.Linux
  //             else -> Platform.Windows
  //           }
  //         })
  //     }
  //     val info = Defaults.defaultGsgmInfo
  //     val setting = Defaults.defaultGsgmSetting.copy(platform = platform)
  //
  //     gameFileList.forEach {
  //       println()
  //       initGsgm(it, info, setting)
  //     }
  //   }
  //
  // }
  //
  // private fun initGsgm(
  //   gameFile: File,
  //   gsgmInfo: GsgmInfo,
  //   gsgmSetting: GsgmSetting
  // ): String {
  //   val gsgmDirPath = "${gameFile.absolutePath}/${GsgmFileName.GSGM_DIR}"
  //   val infoPath = "$gsgmDirPath/${GsgmFileName.INFO}"
  //   val settingPath = "$gsgmDirPath/${GsgmFileName.SETTING}"
  //
  //   FileUtil.mkdir(gsgmDirPath)
  //
  //   val infoJson = objectMapper.writeValueAsString(gsgmInfo).let { JSONUtil.formatJsonStr(it) }
  //   val settingJson = objectMapper.writeValueAsString(gsgmSetting).let { JSONUtil.formatJsonStr(it) }
  //
  //   FileUtil.writeUtf8String(infoJson, infoPath)
  //   FileUtil.writeUtf8String(settingJson, settingPath)
  //
  //   return infoJson + "\n" + settingJson
  // }

}