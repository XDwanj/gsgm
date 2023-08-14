package cn.xdwanj.gsgm.data.setting

import cn.xdwanj.gsgm.service.LibraryService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.runBlocking
import org.dromara.hutool.core.date.DateTime
import org.dromara.hutool.core.io.file.FileUtil
import org.dromara.hutool.json.JSONUtil
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class GsgmInfoTest {

  @Autowired
  private lateinit var libraryService: LibraryService

  @Autowired
  private lateinit var objectMapper: ObjectMapper

  @Disabled
  @Test
  fun `add installAt column`(): Unit = runBlocking {
    val path = "/mnt/Frequent/HGame"
    val gameFileList = libraryService.deepGameFile(path)

    gameFileList.forEach {
      val infoPath = "${it.absolutePath}/.gsgm/info.json"
      val info = FileUtil.readUtf8String(infoPath).let { objectMapper.readValue<GsgmInfo>(it) }

      objectMapper.writeValueAsString(info.copy(initTime = DateTime.now()))
        .let { JSONUtil.formatJsonStr(it) }
        .let { FileUtil.writeUtf8String(it, infoPath) }

      println(info)
    }
  }
}