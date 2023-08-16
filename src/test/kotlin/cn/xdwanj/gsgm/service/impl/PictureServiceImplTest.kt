package cn.xdwanj.gsgm.service.impl

import cn.xdwanj.gsgm.service.PictureService
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.io.File

@SpringBootTest
class PictureServiceImplTest {

  @Autowired
  private lateinit var pictureService: PictureService

  @Test
  fun suppressLutrisBanner(): Unit = runBlocking {
    val origin = File("/home/xdwanj/Downloads/Aria2Download/completed/test/res/cover.png")
    val dest = File("/home/xdwanj/Downloads/Aria2Download/completed/test/res/copy.png")
    pictureService.suppressLutrisBanner(origin, dest)
  }

  @Test
  fun suppressLutrisCoverart() {
  }

  @Test
  fun suppressLutrisIcon() {
  }
}