package cn.xdwanj.gsgm.service.impl

import cn.xdwanj.gsgm.base.GsgmGlobalSettings.picTemp
import cn.xdwanj.gsgm.data.enum.LutrisImageStandard
import cn.xdwanj.gsgm.service.PictureService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.dromara.hutool.core.data.id.IdUtil
import org.dromara.hutool.core.io.file.FileUtil
import org.dromara.hutool.swing.img.ImgUtil
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.awt.Color
import java.io.File

@Service
class PictureServiceImpl : PictureService {

  private val logger = LoggerFactory.getLogger(PictureServiceImpl::class.java)

  override suspend fun suppressLutrisBanner(
    originFile: File,
    destFile: File,
  ): Boolean = withContext(Dispatchers.IO) {
    val newWidth = LutrisImageStandard.Banner.width
    val newHeight = LutrisImageStandard.Banner.height

    val tempFileName = "${IdUtil.fastSimpleUUID().slice(0..5)}${originFile.nameWithoutExtension}.png"
    val tempFile = File("$picTemp/$tempFileName")
    var isSuccess = false

    try {
      ImgUtil.scale(
        originFile,
        tempFile,
        newWidth,
        newHeight,
        Color(0, 0, 0, 0)
      )

      FileUtil.move(tempFile, destFile, true)
      isSuccess = true
    } catch (e: Exception) {
      logger.error("banner conversion failed: $originFile $destFile", e)
    }

    isSuccess
  }

  override suspend fun suppressLutrisCoverart(
    originFile: File,
    destFile: File,
  ): Boolean = withContext(Dispatchers.IO) {
    val newWidth = LutrisImageStandard.Coverart.width
    val newHeight = LutrisImageStandard.Coverart.height

    val tempFileName = "${IdUtil.fastSimpleUUID().substring(0..5)}_${originFile.nameWithoutExtension}.png"
    val tempFile = File("$picTemp/$tempFileName")
    var isSuccess = false

    try {
      ImgUtil.scale(
        originFile,
        tempFile,
        newWidth,
        newHeight,
        Color(0, 0, 0, 0)
      )

      FileUtil.move(tempFile, destFile, true)
      isSuccess = true
    } catch (e: Exception) {
      logger.error("coverart conversion failed: $originFile $destFile", e)

    }

    isSuccess
  }

  override suspend fun suppressLutrisIcon(
    originFile: File,
    destFile: File,
  ): Boolean = withContext(Dispatchers.IO) {
    val newWidth = LutrisImageStandard.Icon.width
    val newHeight = LutrisImageStandard.Icon.height
    var isSuccess = false

    try {
      ImgUtil.scale(
        originFile,
        destFile,
        newWidth,
        newHeight,
        Color(0, 0, 0, 0)
      )
      isSuccess = true
    } catch (e: Exception) {
      logger.error("icon conversion failed: $originFile $destFile", e)
    }

    isSuccess
  }
}