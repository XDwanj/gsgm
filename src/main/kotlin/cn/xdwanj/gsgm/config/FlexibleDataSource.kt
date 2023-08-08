package cn.xdwanj.gsgm.config

import cn.xdwanj.gsgm.base.LutrisGlobalSettings
import com.zaxxer.hikari.HikariDataSource
import org.springframework.jdbc.datasource.AbstractDataSource
import org.springframework.stereotype.Component
import java.sql.Connection
import javax.sql.DataSource

@Component
class FlexibleDataSource : AbstractDataSource() {

  private val dataSourceQueue = mutableListOf(
    HikariDataSource().apply {
      driverClassName = "org.sqlite.JDBC"
      jdbcUrl = "jdbc:sqlite:${LutrisGlobalSettings.pgaDbPath}"
      // jdbcUrl = "jdbc:sqlite:/home/xdwanj/Downloads/Aria2Download/completed/test/pga.db"
    }
  )

  override fun getConnection(): Connection {
    return dataSourceQueue.last().connection
  }

  override fun getConnection(username: String?, password: String?): Connection {
    return dataSourceQueue.last().connection
  }

  @Synchronized
  fun pushDB(customDbPath: String) {
    dataSourceQueue += HikariDataSource().apply {
      driverClassName = "org.sqlite.JDBC"
      jdbcUrl = "jdbc:sqlite:$customDbPath"
    }
  }

  @Synchronized
  fun popDB(): DataSource {
    return dataSourceQueue.removeLast()
  }
}