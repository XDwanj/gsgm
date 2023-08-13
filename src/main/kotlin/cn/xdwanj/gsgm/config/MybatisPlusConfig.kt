package cn.xdwanj.gsgm.config

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor
import org.mybatis.spring.annotation.MapperScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.EnableTransactionManagement

/**
 * Mybatis Plus
 *
 */
@EnableTransactionManagement
@MapperScan("cn.xdwanj.gsgm.data")
@Configuration
class MybatisPlusConfig {

  @Bean
  fun mybatisPlusInterceptor(): MybatisPlusInterceptor = MybatisPlusInterceptor().apply {
    addInnerInterceptor(PaginationInnerInterceptor())
  }
}

