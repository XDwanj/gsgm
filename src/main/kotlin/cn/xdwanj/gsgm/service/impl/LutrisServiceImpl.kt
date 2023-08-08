package cn.xdwanj.gsgm.service.impl

import cn.xdwanj.gsgm.base.GsgmFileName.COVER_NAME
import cn.xdwanj.gsgm.base.GsgmFileName.GSGM_DIR
import cn.xdwanj.gsgm.base.LutrisConstant
import cn.xdwanj.gsgm.base.LutrisConstant.BANNER_PREFIX
import cn.xdwanj.gsgm.base.LutrisConstant.COVERART_PREFIX
import cn.xdwanj.gsgm.base.LutrisConstant.ICON_PREFIX
import cn.xdwanj.gsgm.base.LutrisExtName.BANNER_SUFFIX
import cn.xdwanj.gsgm.base.LutrisExtName.COVERART_SUFFIX
import cn.xdwanj.gsgm.base.LutrisExtName.ICON_SUFFIX
import cn.xdwanj.gsgm.base.LutrisGlobalSettings.bannerPath
import cn.xdwanj.gsgm.base.LutrisGlobalSettings.coverartPath
import cn.xdwanj.gsgm.base.LutrisGlobalSettings.gameScriptPath
import cn.xdwanj.gsgm.base.LutrisGlobalSettings.iconPath
import cn.xdwanj.gsgm.config.FlexibleDataSource
import cn.xdwanj.gsgm.data.entity.LutrisGame
import cn.xdwanj.gsgm.data.entity.LutrisRelGameToCategories
import cn.xdwanj.gsgm.data.enum.LutrisImageStandard
import cn.xdwanj.gsgm.data.enum.LutrisImageStandard.Coverart
import cn.xdwanj.gsgm.data.mapper.LutrisGameMapper
import cn.xdwanj.gsgm.data.mapper.LutrisRelGameToCategoriesMapper
import cn.xdwanj.gsgm.data.script.LutrisInstallScript
import cn.xdwanj.gsgm.data.script.LutrisRunScript
import cn.xdwanj.gsgm.data.script.toYaml
import cn.xdwanj.gsgm.data.setting.GsgmWrapper
import cn.xdwanj.gsgm.service.LibraryService
import cn.xdwanj.gsgm.service.LutrisService
import cn.xdwanj.gsgm.util.extensions.queryChain
import cn.xdwanj.gsgm.util.extensions.updateChain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.dromara.hutool.core.io.IORuntimeException
import org.dromara.hutool.core.io.file.FileUtil
import org.dromara.hutool.swing.img.ImgUtil
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.awt.Color
import java.io.File
import java.math.BigDecimal

@Service
class LutrisServiceImpl(
  private val libraryService: LibraryService,
  private val lutrisGameMapper: LutrisGameMapper,
  private val flexibleDataSource: FlexibleDataSource,
  private val lutrisRelGameToCategoriesMapper: LutrisRelGameToCategoriesMapper,
) : LutrisService {
  private val logger = LoggerFactory.getLogger(LutrisServiceImpl::class.java)

  override suspend fun installGameIcon(wrapper: GsgmWrapper): Boolean = withContext(Dispatchers.IO) {
    val gsgmId = wrapper.gsgmInfo!!.id!!
    val destFile = File("$iconPath/${ICON_PREFIX}$gsgmId.${ICON_SUFFIX}")
    val gameFile = wrapper.gameFile!!

    // picture path
    val pictureFile = FileUtil.ls("${gameFile.absoluteFile}/$GSGM_DIR").first {
      COVER_NAME == it.nameWithoutExtension.lowercase()
    }

    // 不考虑变换，直接转128x128，多出来的部分填充透明，这样图片反而能够居中
    val newWidth = LutrisImageStandard.Icon.width
    val newHeight = LutrisImageStandard.Icon.height

    runCatching {
      ImgUtil.scale(pictureFile, destFile, newWidth, newHeight, Color(0, 0, 0, 0))
    }.onFailure {
      logger.error("icon转换失败: originFile=$pictureFile, destFile=$destFile")
      throw it
    }.isSuccess
  }

  override suspend fun installGameCoverart(wrapper: GsgmWrapper): Boolean = withContext(Dispatchers.IO) {
    val gsgmId = wrapper.gsgmInfo!!.id!!
    val destFile = File("$coverartPath/$COVERART_PREFIX$gsgmId.$COVERART_SUFFIX")
    val gameFile = wrapper.gameFile!!

    // picture path
    val pictureFile = FileUtil.ls("${gameFile.absoluteFile}/$GSGM_DIR").first {
      COVER_NAME == it.nameWithoutExtension.lowercase()
    }

    // 264x352 ok
    val originImg = ImgUtil.read(pictureFile)

    if (originImg.width < Coverart.width) logger.warn("图片宽度小于${Coverart.width}px: file=$gameFile, width=${originImg.width}")
    if (originImg.height < Coverart.height) logger.warn("图片高度小于${Coverart.height}px: file=$gameFile, width=${originImg.width}")

    // todo: 压缩图片
    runCatching {
      FileUtil.copy(pictureFile, destFile, true)
    }.onFailure {
      logger.error("coverart 转换失败: originFile=$pictureFile, destFile=$destFile")
      throw it
    }.isSuccess
  }

  override suspend fun installGameBanner(wrapper: GsgmWrapper): Boolean = withContext(Dispatchers.IO) {
    val gsgmId = wrapper.gsgmInfo!!.id!!
    val gameFile = wrapper.gameFile!!
    val destFile = File("$bannerPath/$BANNER_PREFIX$gsgmId.$BANNER_SUFFIX")

    // picture path
    val pictureFile = FileUtil.ls("${gameFile.absoluteFile}/$GSGM_DIR").first {
      COVER_NAME == it.nameWithoutExtension.lowercase()
    }

    // todo: 压缩图片
    runCatching {
      FileUtil.copy(pictureFile, destFile, true)
    }.onFailure {
      logger.error("banner 转换失败: originFile=$pictureFile, destFile=$destFile")
      throw it
    }.isSuccess
  }

  override suspend fun getLutrisInstallScript(gsgmWrapper: GsgmWrapper): LutrisInstallScript =
    withContext(Dispatchers.IO) {
      val gameFile = gsgmWrapper.gameFile!!
      val gameInfo = gsgmWrapper.gsgmInfo!!
      val gameSetting = gsgmWrapper.gsgmSetting!!

      LutrisInstallScript(
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

  override suspend fun getLutrisRunScript(gsgmWrapper: GsgmWrapper): LutrisRunScript =
    withContext(Dispatchers.IO) {
      val gameInfo = gsgmWrapper.gsgmInfo!!
      val gameSetting = gsgmWrapper.gsgmSetting!!

      LutrisRunScript(
        slug = "${LutrisConstant.SLUG_PREFIX}${gameInfo.id}",
        game_slug = "${LutrisConstant.SLUG_PREFIX}${gameInfo.id}",
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

  override suspend fun insertLutrisDB(gsgmWrapper: GsgmWrapper): LutrisGame = withContext(Dispatchers.IO) {
    if (gsgmWrapper.gsgmInfo == null) throw IllegalArgumentException("gameWrapper.gameInfo must not null")
    if (gsgmWrapper.gsgmSetting == null) throw IllegalArgumentException("gameWrapper.gameSetting must not null")

    val lutrisGame = getLutrisGameByGsgmWrapper(gsgmWrapper)

    lutrisGameMapper.insert(lutrisGame)

    lutrisGame
  }

  override fun getLutrisGameByGsgmWrapper(
    gsgmWrapper: GsgmWrapper,
  ): LutrisGame {
    val gsgmId = gsgmWrapper.gsgmInfo!!.id!!
    return LutrisGame(
      id = null,
      name = gsgmWrapper.gameFile!!.name,
      slug = LutrisConstant.SLUG_PREFIX + gsgmId,
      installerSlug = LutrisConstant.SLUG_PREFIX + gsgmId,
      platform = gsgmWrapper.gsgmSetting!!.platform!!.value,
      runner = gsgmWrapper.gsgmSetting!!.platform!!.runner,
      installed = 1,
      installedAt = System.currentTimeMillis() / 1000,
      configpath = LutrisConstant.SLUG_PREFIX + gsgmId,
      lastplayed = gsgmWrapper.gsgmHistory?.lastGameMoment?.let { it.time / 1000 },
      playtime = gsgmWrapper.gsgmHistory?.gameTime ?: BigDecimal.ZERO,
      hasCustomBanner = 0,
      hasCustomIcon = 0,
      hasCustomCoverartBig = 0,
      hidden = 0,
    )
  }

  override suspend fun installLutrisGame(gsgmWrapper: GsgmWrapper): LutrisGame = withContext(Dispatchers.IO) {

    val scriptPath = gameScriptPath.also { FileUtil.mkdir(it) }
    val coverartPath = coverartPath.also { FileUtil.mkdir(it) }
    val bannerPath = bannerPath.also { FileUtil.mkdir(it) }
    val iconPath = iconPath.also { FileUtil.mkdir(it) }

    // install db
    val lutrisGame = upsertLutrisDB(gsgmWrapper)
    logger.info("lutrisGame = $lutrisGame")

    // script
    val lutrisRunScript = getLutrisRunScript(gsgmWrapper)
    val scriptDest = "$scriptPath/${lutrisGame.configpath}.yml"
    logger.info("scriptDest = $scriptDest")
    FileUtil.writeUtf8String(lutrisRunScript.toYaml(), scriptDest)

    // coverart
    val coverartDest = "$coverartPath/${lutrisGame.slug}.$COVERART_SUFFIX"
    logger.info("coverartDest = $coverartDest")
    installGameCoverart(gsgmWrapper)

    // banner
    val bannerDest = "$bannerPath/${lutrisGame.slug}.$BANNER_SUFFIX"
    logger.info("bannerDest = $bannerDest")
    installGameBanner(gsgmWrapper)

    // icon
    val iconDest = "$iconPath/lutris_${lutrisGame.slug}.$ICON_SUFFIX"
    logger.info("iconDest = $iconDest")
    installGameIcon(gsgmWrapper)

    logger.info("install game [${gsgmWrapper.gameFile?.name}] is ok")

    lutrisGame
  }

  override suspend fun updateInstallLutrisGame(wrapper: GsgmWrapper): LutrisRunScript {
    val gsgmId = wrapper.gsgmInfo!!.id!!

    val installedCoverartPath = "$coverartPath/$COVERART_PREFIX$gsgmId.$COVERART_SUFFIX"
    val installedBannerPath = "$coverartPath/$BANNER_PREFIX$gsgmId.$BANNER_SUFFIX"
    val installedIconPath = "$coverartPath/$ICON_PREFIX$gsgmId.$ICON_SUFFIX"
    val scriptDirPath = gameScriptPath.also { FileUtil.mkdir(it) }

    // if
    if (!FileUtil.exists(installedCoverartPath)) {
      installGameCoverart(wrapper)
    }
    if (!FileUtil.exists(installedBannerPath)) {
      installGameBanner(wrapper)
    }
    if (!FileUtil.exists(installedIconPath)) {
      installGameIcon(wrapper)
    }

    // db
    val lutrisGame = upsertLutrisDB(wrapper)

    // script
    val lutrisRunScript = getLutrisRunScript(wrapper)
    val scriptDest = "$scriptDirPath/${lutrisGame.configpath}.yml"
    logger.info("scriptDest = $scriptDest")
    FileUtil.writeUtf8String(lutrisRunScript.toYaml(), scriptDest)

    return lutrisRunScript
  }

  override suspend fun insertLutrisDB(gameFile: File): LutrisGame = withContext(Dispatchers.IO) {
    if (FileUtil.exists("${gameFile.absolutePath}/.gsgm")
        .not()
    ) throw IllegalArgumentException("gameFile 并非游戏文件夹: $gameFile")

    val gameWrapper = libraryService.getGsgmWrapperByFile(gameFile)
    this@LutrisServiceImpl.upsertLutrisDB(gameWrapper)
  }

  override suspend fun upsertLutrisDB(gsgmWrapper: GsgmWrapper): LutrisGame = withContext(Dispatchers.IO) {
    val slug = LutrisConstant.SLUG_PREFIX + gsgmWrapper.gsgmInfo!!.id!!
    val lutrisGame = getLutrisGameByGsgmWrapper(gsgmWrapper)
    val exists = lutrisGameMapper.queryChain()
      .eq(LutrisGame::slug, slug)
      .exists()

    if (exists) {
      // exists
      lutrisGameMapper.updateChain()
        .eq(LutrisGame::slug, slug)
        .setEntity(lutrisGame.copy(id = null, slug = null))
    } else {
      // no exists
      lutrisGameMapper.insert(lutrisGame)
    }

    lutrisGame
  }

  override suspend fun cleanLutrisDB(): Boolean = withContext(Dispatchers.IO) {

    val idList = try {
      lutrisGameMapper.queryChain()
        .likeRight(LutrisGame::slug, LutrisConstant.SLUG_PREFIX)
        .select(LutrisGame::id)
        .list()
        .map { it.id!! }
    } catch (e: Exception) {
      logger.error("", e)
      emptyList()
    }

    // remove rel table
    try {
      // lutrisGameMapper.updateChain()
      //   .`in`(LutrisRelGameToCategories::gameId, idList)
      //   .remove()
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
        .filter { it.name.startsWith(LutrisConstant.ICON_PREFIX, false) }
        .forEach { FileUtil.del(it) }

      return@withContext true
    } catch (e: IORuntimeException) {
      logger.error("", e)

      return@withContext false
    }
  }

  override suspend fun cleanLutrisRunScript(): Boolean {
    return try {
      FileUtil.ls(gameScriptPath)
        .filter { it.name.startsWith(LutrisConstant.SCRIPT_PREFIX, false) }
        .forEach { FileUtil.del(it) }

      true
    } catch (e: Exception) {
      logger.error("", e)
      false
    }
  }
}