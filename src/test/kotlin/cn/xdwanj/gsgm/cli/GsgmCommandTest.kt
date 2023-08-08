package cn.xdwanj.gsgm.cli

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import picocli.CommandLine

@SpringBootTest
class GsgmCommandTest {
  @Autowired
  private lateinit var gsgmCommand: GsgmCommand

  @Autowired
  private lateinit var factory: CommandLine.IFactory

  @BeforeEach
  fun beforeEach() {
    println("----------------------------------------------------")
  }

  @AfterEach
  fun afterEach() {
    println("----------------------------------------------------")
  }

  @Test
  fun `gsgm -hV`() {
    CommandLine(gsgmCommand, factory).execute(
      "-hV"
    )
    println("----------------------------------------------------")
    CommandLine(gsgmCommand, factory).execute(
      "search", "--help"
    )
    println("----------------------------------------------------")
    CommandLine(gsgmCommand, factory).execute(
      "list", "--help"
    )
    println("----------------------------------------------------")
    CommandLine(gsgmCommand, factory).execute(
      "info", "--help"
    )
    println("----------------------------------------------------")
    CommandLine(gsgmCommand, factory).execute(
      "install", "--help"
    )
    println("----------------------------------------------------")
    CommandLine(gsgmCommand, factory).execute(
      "init", "--help"
    )
    println("----------------------------------------------------")
    CommandLine(gsgmCommand, factory).execute(
      "remove", "--help"
    )
    println("----------------------------------------------------")
    CommandLine(gsgmCommand, factory).execute(
      "check", "--help"
    )
    println("----------------------------------------------------")
    CommandLine(gsgmCommand, factory).execute(
      "sync", "--help"
    )
    println("----------------------------------------------------")
    CommandLine(gsgmCommand, factory).execute(
      "clean", "--help"
    )
  }

}

