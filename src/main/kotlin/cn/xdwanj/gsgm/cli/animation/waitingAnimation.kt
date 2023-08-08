package cn.xdwanj.gsgm.cli.animation

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive

private suspend fun waitingAnimation() = coroutineScope {
  var index = 0
  val animationFrames = arrayOf(
    "[     ]",
    "[=    ]",
    "[==   ]",
    "[===  ]",
    "[ === ]",
    "[  ===]",
    "[   ==]",
    "[    =]",
  )

  while (isActive) {
    print("\r${animationFrames[index++ % animationFrames.size]}")
    Thread.sleep(500)
  }
}

