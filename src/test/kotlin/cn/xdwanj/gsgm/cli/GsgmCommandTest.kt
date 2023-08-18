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
  private lateinit var iFactory: CommandLine.IFactory

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
    CommandLine(gsgmCommand, iFactory).execute(
      "-hV"
    )
    println("----------------------------------------------------")
    CommandLine(gsgmCommand, iFactory).execute(
      "search", "--help"
    )
    println("----------------------------------------------------")
    CommandLine(gsgmCommand, iFactory).execute(
      "list", "--help"
    )
    println("----------------------------------------------------")
    CommandLine(gsgmCommand, iFactory).execute(
      "info", "--help"
    )
    println("----------------------------------------------------")
    CommandLine(gsgmCommand, iFactory).execute(
      "install", "--help"
    )
    println("----------------------------------------------------")
    CommandLine(gsgmCommand, iFactory).execute(
      "init", "--help"
    )
    println("----------------------------------------------------")
    CommandLine(gsgmCommand, iFactory).execute(
      "remove", "--help"
    )
    println("----------------------------------------------------")
    CommandLine(gsgmCommand, iFactory).execute(
      "check", "--help"
    )
    println("----------------------------------------------------")
    CommandLine(gsgmCommand, iFactory).execute(
      "sync", "--help"
    )
    println("----------------------------------------------------")
    CommandLine(gsgmCommand, iFactory).execute(
      "clean", "--help"
    )
  }

  @Test
  fun test() {
    CommandLine(gsgmCommand, iFactory).execute(
      "--spring.profiles.active=dev", "init", "-h"
    )
  }

}

