package cn.xdwanj.gsgm.util.extensions

import cn.xdwanj.gsgm.data.dto.GameGroupDto
import cn.xdwanj.gsgm.data.wrapper.GsgmWrapper
import cn.xdwanj.gsgm.service.LibraryService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import org.dromara.hutool.extra.spring.SpringUtil

val libraryService: LibraryService = SpringUtil.getBean(LibraryService::class.java)

suspend fun List<GameGroupDto>.toGsgmWrapperList(): List<GsgmWrapper> = withContext(Dispatchers.IO) {
  this@toGsgmWrapperList.map { group ->
    group.fileList.map {
      async { libraryService.getGsgmWrapperByFileAndGroupName(it, group.groupName) }
    }
  }.flatten()
    .awaitAll()
}