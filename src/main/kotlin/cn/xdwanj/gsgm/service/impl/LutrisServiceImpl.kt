package cn.xdwanj.gsgm.service.impl

import cn.xdwanj.gsgm.base.GsgmFileName.COVER_NAME
import cn.xdwanj.gsgm.base.GsgmFileName.GSGM_DIR
import cn.xdwanj.gsgm.base.LutrisConstant
import cn.xdwanj.gsgm.base.LutrisConstant.BANNER_PREFIX
import cn.xdwanj.gsgm.base.LutrisConstant.COVERART_PREFIX
import cn.xdwanj.gsgm.base.LutrisConstant.ICON_PREFIX
import cn.xdwanj.gsgm.base.LutrisConstant.SLUG_PREFIX
import cn.xdwanj.gsgm.base.LutrisExtName.BANNER_SUFFIX
import cn.xdwanj.gsgm.base.LutrisExtName.COVERART_SUFFIX
import cn.xdwanj.gsgm.base.LutrisExtName.ICON_SUFFIX
import cn.xdwanj.gsgm.base.LutrisExtName.SCRIPT_SUFFIX
import cn.xdwanj.gsgm.base.LutrisGlobalSettings.bannerPath
import cn.xdwanj.gsgm.base.LutrisGlobalSettings.coverartPath
import cn.xdwanj.gsgm.base.LutrisGlobalSettings.iconPath
import cn.xdwanj.gsgm.base.LutrisGlobalSettings.runScriptPath
import cn.xdwanj.gsgm.config.FlexibleDataSource
import cn.xdwanj.gsgm.data.dto.CommonState
import cn.xdwanj.gsgm.data.entity.LutrisGame
import cn.xdwanj.gsgm.data.entity.LutrisRelGameToCategories
import cn.xdwanj.gsgm.data.enum.LutrisImageStandard
import cn.xdwanj.gsgm.data.mapper.LutrisGameMapper
import cn.xdwanj.gsgm.data.mapper.LutrisRelGameToCategoriesMapper
import cn.xdwanj.gsgm.data.script.LutrisInstallScript
import cn.xdwanj.gsgm.data.script.LutrisRunScript
import cn.xdwanj.gsgm.data.script.toYaml
import cn.xdwanj.gsgm.data.setting.GsgmWrapper
import cn.xdwanj.gsgm.service.LutrisService
import cn.xdwanj.gsgm.util.extensions.queryChain
import cn.xdwanj.gsgm.util.extensions.updateChain
import cn.xdwanj.kcolor.Ansi.colorize
import cn.xdwanj.kcolor.AttrTemplate.redText
import cn.xdwanj.kcolor.AttrTemplate.yellowText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import org.dromara.hutool.core.io.IORuntimeException
import org.dromara.hutool.core.io.file.FileUtil
import org.dromara.hutool.swing.img.ImgUtil
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.awt.Color
import java.io.File
import java.math.BigDecimal

@Service
class LutrisServiceImpl(
  private val lutrisGameMapper: LutrisGameMapper,
  private val flexibleDataSource: FlexibleDataSource,
  private val lutrisRelGameToCategoriesMapper: LutrisRelGameToCategoriesMapper,
) : LutrisService {
  private val logger = LoggerFactory.getLogger(LutrisServiceImpl::class.java)

  override suspend fun installGameIcon(
    wrapper: GsgmWrapper,
  ): CommonState<Unit> = withContext(Dispatchers.IO) {
    val gsgmId = wrapper.gsgmInfo!!.id!!
    val destFile = File("$iconPath/${ICON_PREFIX}$gsgmId.${ICON_SUFFIX}")
    val gameFile = wrapper.gameFile!!

    // picture path
    val pictureFile = FileUtil.ls("${gameFile.absoluteFile}/$GSGM_DIR")
      .firstOrNull { COVER_NAME == it.nameWithoutExtension.lowercase() }
      ?: throw IllegalStateException("must have picture: $gameFile")

    // 不考虑变换，直接转128x128，多出来的部分填充透明，这样图片反而能够居中
    val newWidth = LutrisImageStandard.Icon.width
    val newHeight = LutrisImageStandard.Icon.height

    val commonState = CommonState<Unit>()

    try {
      ImgUtil.scale(pictureFile, destFile, newWidth, newHeight, Color(0, 0, 0, 0))
      val msg = "icon conversion installed successfully: ${destFile.absolutePath}"
      commonState.messageList += msg
    } catch (e: Exception) {
      val log = "icon conversion failed: $pictureFile"
      logger.error(log, e)
      commonState.messageList += colorize(log, redText)
      commonState.level++
    }

    commonState
  }

  override suspend fun installGameCoverart(wrapper: GsgmWrapper): CommonState<Unit> = withContext(Dispatchers.IO) {
    val gsgmId = wrapper.gsgmInfo!!.id!!
    val destFile = File("$coverartPath/$COVERART_PREFIX$gsgmId.$COVERART_SUFFIX")
    val gameFile = wrapper.gameFile!!

    // picture path
    val pictureFile = FileUtil.ls("${gameFile.absoluteFile}/$GSGM_DIR")
      .first { COVER_NAME == it.nameWithoutExtension.lowercase() }
      ?: throw IllegalStateException("must have picture: $gameFile")

    // 264x352 ok
    // val originImg = ImgUtil.read(pictureFile)

    val commonState = CommonState<Unit>()
    try {
      // todo: 压缩图片
      FileUtil.copy(pictureFile, destFile, true)
      val msg = "cover conversion installed successfully: ${destFile.absolutePath}"
      commonState.messageList += msg
    } catch (e: Exception) {
      val log = "cover conversion failed: $pictureFile"
      logger.error(log, e)
      commonState.messageList += colorize(log, redText)
      commonState.level++
    }

    commonState
  }

  override suspend fun installGameBanner(wrapper: GsgmWrapper): CommonState<Unit> = withContext(Dispatchers.IO) {
    val gsgmId = wrapper.gsgmInfo!!.id!!
    val gameFile = wrapper.gameFile!!
    val destFile = File("$bannerPath/$BANNER_PREFIX$gsgmId.$BANNER_SUFFIX")

    // picture path
    val pictureFile = FileUtil.ls("${gameFile.absoluteFile}/$GSGM_DIR").first {
      COVER_NAME == it.nameWithoutExtension.lowercase()
    }

    // todo: 压缩图片
    val commonState = CommonState<Unit>()
    try {
      FileUtil.copy(pictureFile, destFile, true)
      val msg = "banner conversion installed successfully: ${destFile.absolutePath}"
      commonState.messageList += msg
    } catch (e: Exception) {
      val log = "banner conversion failed: $pictureFile"
      logger.error(log, e)
      commonState.messageList += colorize(log, redText)
      commonState.level++
    }
    commonState
  }

  override fun getLutrisInstallScript(gsgmWrapper: GsgmWrapper): LutrisInstallScript {
    val gameFile = gsgmWrapper.gameFile!!
    val gameInfo = gsgmWrapper.gsgmInfo!!
    val gameSetting = gsgmWrapper.gsgmSetting!!

    return LutrisInstallScript(
      name = gameFile.name,
      slug = "gsgm-${gameInfo.id}",
      version = "v1.0.0",
      runner = "wine",
      script = LutrisInstallScript.GameScript(
        game = LutrisInstallScript.GameDetails(
          exe = "${gameFile.absolutePath}/${gameSetting.executeLocation!!}",
          prefix = "${gameFile.absolutePath}/${gameSetting.winePrefix!!}",
        )
      ),
    )
  }

  override fun getLutrisRunScript(gsgmWrapper: GsgmWrapper): LutrisRunScript {
    val gameInfo = gsgmWrapper.gsgmInfo!!
    val gameSetting = gsgmWrapper.gsgmSetting!!

    return LutrisRunScript(
      slug = "$SLUG_PREFIX${gameInfo.id}",
      game_slug = "$SLUG_PREFIX${gameInfo.id}",
      game = LutrisRunScript.GameDetails(
        exe = "${gsgmWrapper.gameFile!!.absolutePath}/${gameSetting.executeLocation}",
        prefix = "${gsgmWrapper.gameFile!!.absolutePath}/${gameSetting.winePrefix}",
      ),
      system = LutrisRunScript.SystemDetails(
        locale = gameSetting.localeCharSet!!.value
      ),
      wine = null,
    )
  }

  override suspend fun insertLutrisDb(
    gsgmWrapper: GsgmWrapper,
  ): CommonState<Unit> = withContext(Dispatchers.IO) {
    if (gsgmWrapper.gsgmInfo == null) throw IllegalArgumentException("gameWrapper.gameInfo must not null")
    if (gsgmWrapper.gsgmSetting == null) throw IllegalArgumentException("gameWrapper.gameSetting must not null")

    val lutrisGame = getLutrisGameByGsgmWrapper(gsgmWrapper)

    lutrisGameMapper.insert(lutrisGame)

    val log = colorize("lutris DB successful installation")
    CommonState(messageList = mutableListOf(log))
  }

  override fun getLutrisGameByGsgmWrapper(
    gsgmWrapper: GsgmWrapper,
  ): LutrisGame {
    val gsgmId = gsgmWrapper.gsgmInfo!!.id!!
    val installedAt = gsgmWrapper.gsgmInfo?.initTime?.time
      ?.let { it / 1000 }
      ?: (System.currentTimeMillis() / 1000)
    val directory = gsgmWrapper.gameFile!!.absolutePath
    return LutrisGame(
      id = null,
      name = gsgmWrapper.gameFile!!.name,
      slug = SLUG_PREFIX + gsgmId,
      installerSlug = SLUG_PREFIX + gsgmId,
      platform = gsgmWrapper.gsgmSetting!!.platform!!.value,
      runner = gsgmWrapper.gsgmSetting!!.platform!!.runner,
      installed = 1,
      installedAt = installedAt,
      configpath = SLUG_PREFIX + gsgmId,
      lastplayed = gsgmWrapper.gsgmHistory?.lastGameMoment?.let { it.time / 1000 },
      playtime = gsgmWrapper.gsgmHistory?.gameTime ?: BigDecimal.ZERO,
      directory = directory,
      hasCustomBanner = 0,
      hasCustomIcon = 0,
      hasCustomCoverartBig = 0,
      hidden = 0,
    )
  }

  @Transactional
  override suspend fun installLutrisGame(
    gsgmWrapper: GsgmWrapper,
  ): CommonState<Unit> = withContext(Dispatchers.IO) {

    val state = CommonState<Unit>()

    listOf(
      async { insertLutrisDb(gsgmWrapper) },
      async { installRunScript(gsgmWrapper) },
      async { installGameCoverart(gsgmWrapper) },
      async { installGameBanner(gsgmWrapper) },
      async { installGameIcon(gsgmWrapper) },
    ).awaitAll()
      .forEach { state += it }

    return@withContext state
  }

  @Transactional
  override suspend fun upsertLutrisGame(
    gsgmWrapper: GsgmWrapper,
  ): CommonState<Unit> = withContext(Dispatchers.IO) {
    val state = CommonState<Unit>()

    listOf(
      async { upsertLutrisDB(gsgmWrapper) },
      async { upsertRunScript(gsgmWrapper) },
      async { upsertGameCoverart(gsgmWrapper) },
      async { upsertGameBanner(gsgmWrapper) },
      async { upsertGameIcon(gsgmWrapper) },
    ).awaitAll()
      .forEach { state += it }

    state
  }

  @Transactional
  override suspend fun upsertLutrisDB(
    gsgmWrapper: GsgmWrapper,
  ): CommonState<Unit> = withContext(Dispatchers.IO) {
    val slug = SLUG_PREFIX + gsgmWrapper.gsgmInfo!!.id!!
    val lutrisGame = getLutrisGameByGsgmWrapper(gsgmWrapper)
    val exists = lutrisGameMapper.queryChain()
      .eq(LutrisGame::slug, slug)
      .exists()

    val state = CommonState<Unit>()

    if (exists) {
      // is existing
      try {
        val log = colorize("the game is already installed, overwriting the database", yellowText)
        lutrisGameMapper.updateChain()
          .eq(LutrisGame::slug, slug)
          .update(lutrisGame.copy(id = null, slug = null))
        state.messageList += log
      } catch (e: Exception) {
        val log = "database overwrite failed"
        logger.error(log, e)
        state.messageList += colorize(log, redText)
      }
    } else {
      // no exist
      try {
        val log = colorize("the current game is not installed newly inserted")
        lutrisGameMapper.insert(lutrisGame)
        state.messageList += log
      } catch (e: Exception) {
        val log = "Database insert failed: $lutrisGame"
        logger.error(log, e)
        state.messageList += colorize(log, redText)
      }
    }

    state
  }

  override suspend fun cleanLutrisDB(): Boolean = withContext(Dispatchers.IO) {

    val idList = try {
      lutrisGameMapper.queryChain()
        .likeRight(LutrisGame::slug, SLUG_PREFIX)
        .select(LutrisGame::id)
        .list()
        .map { it.id!! }
    } catch (e: Exception) {
      logger.error("", e)
      emptyList()
    }

    // remove rel table
    try {
      lutrisRelGameToCategoriesMapper.updateChain()
        .`in`(LutrisRelGameToCategories::gameId, idList)
        .remove()
    } catch (e: Exception) {
      logger.error("", e)
      return@withContext false
    }

    // remove table
    try {
      lutrisGameMapper.deleteBatchIds(idList)
    } catch (e: Exception) {
      logger.error("", e)
      return@withContext false
    }

    flexibleDataSource.popDB()

    true
  }

  override suspend fun cleanLutrisCover(): Boolean = withContext(Dispatchers.IO) {
    try {
      FileUtil.ls(coverartPath)
        .filter { it.name.startsWith(COVERART_PREFIX, false) }
        .forEach { FileUtil.del(it) }

      return@withContext true
    } catch (e: IORuntimeException) {
      logger.error("", e)

      return@withContext false
    }
  }

  override suspend fun cleanLutrisBanner(): Boolean = withContext(Dispatchers.IO) {
    try {
      FileUtil.ls(bannerPath)
        .filter { it.name.startsWith(BANNER_PREFIX, false) }
        .forEach { FileUtil.del(it) }

      return@withContext true
    } catch (e: IORuntimeException) {
      logger.error("", e)

      return@withContext false
    }
  }

  override suspend fun cleanLutrisIcon(): Boolean = withContext(Dispatchers.IO) {
    try {
      FileUtil.ls(iconPath)
        .filter { it.name.startsWith(ICON_PREFIX, false) }
        .forEach { FileUtil.del(it) }

      return@withContext true
    } catch (e: IORuntimeException) {
      logger.error("", e)

      return@withContext false
    }
  }

  override suspend fun cleanLutrisRunScript(): Boolean {
    return try {
      FileUtil.ls(runScriptPath)
        .filter { it.name.startsWith(LutrisConstant.SCRIPT_PREFIX, false) }
        .forEach { FileUtil.del(it) }

      true
    } catch (e: Exception) {
      logger.error("", e)
      false
    }
  }

  override suspend fun removeLutrisGameBySlug(slug: String): CommonState<Unit> = withContext(Dispatchers.IO) {
    val state = CommonState<Unit>()

    listOf(
      async { removeLutrisCoverartBySlug(slug) },
      async { removeLutrisBannerBySlug(slug) },
      async { removeLutrisIconBySlug(slug) },
      async { removeLutrisRunScriptBySlug(slug) },
      async { removeLutrisDbBySlug(slug) },
    ).awaitAll()
      .forEach { state += it }

    state
  }

  override suspend fun removeLutrisCoverartBySlug(slug: String): CommonState<Unit> = withContext(Dispatchers.IO) {
    val currentCoverartPath = "$coverartPath/$slug.$COVERART_SUFFIX"

    if (!FileUtil.exists(currentCoverartPath)) {
      val log = colorize("lutris coverart resource does not exist: $currentCoverartPath", yellowText)
      return@withContext CommonState<Unit>(messageList = mutableListOf(log))
    }

    return@withContext try {
      FileUtil.del(currentCoverartPath)
      val log = colorize("lutris coverart resource deleted successfully: $currentCoverartPath")
      CommonState<Unit>(messageList = mutableListOf(log))
    } catch (e: Exception) {
      val log = "lutris coverart resource deletion failed: $currentCoverartPath"
      logger.error(log, e)
      CommonState<Unit>(level = 1, messageList = mutableListOf(colorize(log, redText)))
    }
  }

  override suspend fun removeLutrisBannerBySlug(slug: String): CommonState<Unit> = withContext(Dispatchers.IO) {
    val currentBannerPath = "$bannerPath/$slug.$BANNER_SUFFIX"

    if (!FileUtil.exists(currentBannerPath)) {
      val log = colorize("lutris banner resource does not exist: $currentBannerPath", yellowText)
      return@withContext CommonState<Unit>(messageList = mutableListOf(log))
    }

    return@withContext try {
      FileUtil.del(currentBannerPath)
      val log = colorize("lutris banner resource deleted successfully: $currentBannerPath")
      CommonState<Unit>(messageList = mutableListOf(log))
    } catch (e: Exception) {
      val log = "lutris banner resource deletion failed: $currentBannerPath"
      logger.error(log, e)
      CommonState<Unit>(level = 1, messageList = mutableListOf(colorize(log, redText)))
    }
  }

  override suspend fun removeLutrisIconBySlug(slug: String): CommonState<Unit> = withContext(Dispatchers.IO) {
    val currentIconPath = "$iconPath/lutris_$slug.$ICON_SUFFIX"

    if (!FileUtil.exists(currentIconPath)) {
      val log = colorize("lutris icon resource does not exist: $currentIconPath", yellowText)
      return@withContext CommonState<Unit>(messageList = mutableListOf(log))
    }

    return@withContext try {
      FileUtil.del(currentIconPath)
      val log = colorize("lutris icon resource deleted successfully: $currentIconPath")
      CommonState<Unit>(messageList = mutableListOf(log))
    } catch (e: Exception) {
      val log = "lutris icon resource deletion failed: $currentIconPath"
      logger.error(log, e)
      CommonState<Unit>(level = 1, messageList = mutableListOf(colorize(log, redText)))
    }
  }

  override suspend fun removeLutrisDbBySlug(slug: String): CommonState<Unit> = withContext(Dispatchers.IO) {
    val exist = lutrisGameMapper.queryChain()
      .eq(LutrisGame::slug, slug)
      .exists()

    if (!exist) {
      val log = colorize("database record does not exist: $slug", yellowText)
      return@withContext CommonState<Unit>(messageList = mutableListOf(log))
    }

    return@withContext try {
      val id = lutrisGameMapper.queryChain()
        .eq(LutrisGame::slug, slug)
        .one()
        .id!!
      lutrisRelGameToCategoriesMapper.updateChain()
        .eq(LutrisRelGameToCategories::gameId, id)
        .remove()
      lutrisGameMapper.updateChain()
        .eq(LutrisGame::id, id)
        .remove()
      val log = colorize("database record deleted successfully: $slug")
      CommonState<Unit>(messageList = mutableListOf(log))
    } catch (e: Exception) {
      val log = "database record deletion failed: $slug"
      logger.error(log, e)
      CommonState<Unit>(level = 1, messageList = mutableListOf(colorize(log, redText)))
    }
  }

  override suspend fun removeLutrisRunScriptBySlug(slug: String): CommonState<Unit> = withContext(Dispatchers.IO) {
    val currentScriptPath = "$runScriptPath/$slug.$SCRIPT_SUFFIX"
    if (!FileUtil.exists(currentScriptPath)) {
      val log = colorize("run script does not exist: $currentScriptPath", yellowText)
      return@withContext CommonState<Unit>(messageList = mutableListOf(log))
    }

    return@withContext try {
      FileUtil.del(currentScriptPath)
      CommonState<Unit>(messageList = mutableListOf(colorize("run script removed: $currentScriptPath")))
    } catch (e: Exception) {
      val log = "failed to run the script to delete: $currentScriptPath"
      logger.error(log, e)
      CommonState<Unit>(level = 1, messageList = mutableListOf(colorize(log, redText)))
    }
  }

  override suspend fun upsertGameIcon(wrapper: GsgmWrapper): CommonState<Unit> = withContext(Dispatchers.IO) {
    val currentIconPath = "$iconPath/lutris_$SLUG_PREFIX${wrapper.gsgmInfo!!.id!!}.$ICON_SUFFIX"

    val state = CommonState<Unit>()
    if (FileUtil.exists(currentIconPath)) {
      state.messageList += colorize("icon resource already exists: $currentIconPath", yellowText)
    } else {
      state += installGameIcon(wrapper)
    }

    state
  }

  override suspend fun upsertGameCoverart(wrapper: GsgmWrapper): CommonState<Unit> = withContext(Dispatchers.IO) {
    val currentCoverPath = "$coverartPath/$SLUG_PREFIX${wrapper.gsgmInfo!!.id!!}.$COVERART_SUFFIX"

    val state = CommonState<Unit>()
    if (FileUtil.exists(currentCoverPath)) {
      state.messageList += colorize("coverart resource already exists: $currentCoverPath", yellowText)
    } else {
      state += installGameIcon(wrapper)
    }

    state
  }

  override suspend fun upsertGameBanner(wrapper: GsgmWrapper): CommonState<Unit> = withContext(Dispatchers.IO) {
    val currentCoverPath = "$bannerPath/$SLUG_PREFIX${wrapper.gsgmInfo!!.id!!}.$BANNER_SUFFIX"

    val state = CommonState<Unit>()
    if (FileUtil.exists(currentCoverPath)) {
      state.messageList += colorize("banner resource already exists: $currentCoverPath", yellowText)
    } else {
      state += installGameIcon(wrapper)
    }
    state
  }

  override suspend fun installRunScript(gsgmWrapper: GsgmWrapper): CommonState<Unit> = withContext(Dispatchers.IO) {
    val scriptDest = "$runScriptPath/$SLUG_PREFIX${gsgmWrapper.gsgmInfo!!.id!!}.$SCRIPT_SUFFIX"
    val lutrisRunScript = getLutrisRunScript(gsgmWrapper)

    val state = CommonState<Unit>()
    try {
      val msg = colorize("the running script has been written successfully: $scriptDest")
      FileUtil.writeUtf8String(lutrisRunScript.toYaml(), scriptDest)
      state.messageList += msg
    } catch (e: Exception) {
      val log = "script writing failed: $scriptDest"
      logger.error(log, e)
      state.messageList += colorize(log, redText)
      state.level++
    }
    state
  }

  override suspend fun upsertRunScript(gsgmWrapper: GsgmWrapper): CommonState<Unit> = withContext(Dispatchers.IO) {
    val scriptDest = "$runScriptPath/$SLUG_PREFIX${gsgmWrapper.gsgmInfo!!.id!!}.$SCRIPT_SUFFIX"

    val state = CommonState<Unit>()
    if (FileUtil.exists(scriptDest)) {
      state.messageList += colorize("script already exists: $scriptDest", yellowText)
    } else {
      state += installRunScript(gsgmWrapper)
    }

    state
  }
}