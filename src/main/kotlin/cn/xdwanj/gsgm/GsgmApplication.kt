package cn.xdwanj.gsgm

import cn.xdwanj.gsgm.cli.GsgmCommand
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.ExitCodeGenerator
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import picocli.CommandLine
import picocli.CommandLine.IFactory
import java.io.Serializable

@SpringBootApplication
class GsgmApplication(
  private val gsgmCommand: GsgmCommand,
  private val factory: IFactory,
) : CommandLineRunner, ExitCodeGenerator, Serializable {
  private var exitCode = 0

  override fun run(vararg args: String?) {

    exitCode = CommandLine(gsgmCommand, factory)
      .execute(*args)

    // exitCode = CommandLine(gsgmCommand, factory)
    //   .execute(
    //     "init", "--is-library", "-i", "--windows-all",
    //     "/home/xdwanj/Downloads/Aria2Download/completed/test/res/library"
    //   )
  }

  override fun getExitCode(): Int {
    return exitCode
  }
}

fun main(args: Array<String>) {
  runApplication<GsgmApplication>(*args)
}