package cn.xdwanj.gsgm.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Json 转换工厂
 *
 */
@Configuration
class JsonConversionFactory {

  @Bean
  fun objectMapper() = ObjectMapper()
}