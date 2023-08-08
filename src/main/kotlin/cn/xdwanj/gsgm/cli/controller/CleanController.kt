package cn.xdwanj.gsgm.cli.controller


interface CleanController {

  /**
   * clean command 服务
   *
   * @return
   */
  suspend fun cleanAction(): Int

}