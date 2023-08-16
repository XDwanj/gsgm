package cn.xdwanj.gsgm.service

import java.io.File

interface PictureService {

  suspend fun suppressLutrisBanner(originFile: File, destFile: File): Boolean

  suspend fun suppressLutrisCoverart(originFile: File, destFile: File): Boolean

  suspend fun suppressLutrisIcon(originFile: File, destFile: File): Boolean
}