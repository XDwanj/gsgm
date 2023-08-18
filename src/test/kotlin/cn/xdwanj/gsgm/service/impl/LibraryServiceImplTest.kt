package cn.xdwanj.gsgm.service.impl

import cn.xdwanj.gsgm.service.LibraryService
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class LibraryServiceImplTest {

  @Autowired
  private lateinit var libraryService: LibraryService

  @Test
  fun deepGroupFile(): Unit = runBlocking {
    val path = "/mnt/Frequent/HGame"
    val list = libraryService.deepGroupFile(path)
    list.forEach {
      println("---------------")
      println("it.groupName = ${it.groupName}")
      it.fileList.forEach(::println)
    }
  }
}