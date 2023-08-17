# Gsgm - GNU Single Game Manager

## 介绍

在 Linux 有如 Lutris 等非常方便的游戏管理平台，他们能够做到与 Steam、Epic 等平台轻松对接，还可以简易的管理 wine 和各种组件，也提供了脚本方式安装游戏，但是脚本却只能做到半自动更新。

Gsgm 不止能够完成全自动批量安装游戏的功能，还有诸如 Lutris 记录备份、Lutris 封面管理等

Gsgm 的特点和功能：

1. 根据规则批量导入游戏，只需要遵循规则，然后输入 `gsgm sync <游戏库位置>` 即可（详细请看下文
2. 同步 Lutris 的游戏记录和设置，即便 Lutris 的记录丢了，我们也可以通过游戏库中，每个游戏下的 `.gsgm` 文件恢复游戏库的 Lutris 数据，支持 Lutris 和 Gsgm 数据双向流动
3. 在 Lutris 上，封面的管理相当随意，只有联合 Steam 等平台的游戏封面才能很好的被管理。Gsgm 脱离 Lutris，可以主动生成 Lutris 的 3 种显示封面，并代为管理

*注意：使用 Gsgm 的时候，请关闭 Lutris，Gsgm 使用过程中可能会操作 Lutris 数据库，可能会导致运行中的 Lutris 发生错误*

## 游戏库规则

```
游戏库位置
├── 游戏A
│   ├── ...                 -------- 其他文件夹和文件
│   ├── .gsgm
│   │     ├── info.json     -------- 游戏的信息
│   │     ├── setting.json  -------- 游戏的运行设置
│   │     ├── history.json  -------- 游玩历史
│   │     ├── cover.png     -------- 游戏封面
│   │     └── winePrefix    -------- 默认的 wineprefix 位置
│   └── 游戏A.exe            -------- 游戏可执行文件
├── 游戏B
│   ├── ...                 -------- 其他文件夹和文件
│   ├── .gsgm
│   │     └── ...
│   └── 游戏B.exe            -------- 游戏可执行文件
├── #文件夹A                 -------- 文件夹的命名是单个 # 开头，例如 `#3D大作`、`# 2D游戏`，文件夹可以嵌套
│   ├── 游戏A
│   │   └── ...
│   └── #文件夹B             -------- 文件夹可以嵌套
│       └── ...
├── ##忽略文件夹A             -------- 忽略文件夹以两个 ## 开头，例如 `##游戏脚本`、`## 风灵月影工具包`
├── ##忽略文件夹B
└── ...
```

info.json

```json
{
  "id": 1684470185450471424, // `gsgm init 随机生成 gsgm id，每个游戏独一无二，不可重复！`
}
```

setting.json

```json
{
  "executeLocation": "PureStation.exe", // 游戏启动文件，路径相对于游戏根目录
  "winePrefix": ".gsgm/winePrefix", // wineprefix 路径，相对于游戏根目录，默认为 `.gsgm/winePrefix`
  "localeCharSet": "zh_CN.UTF-8", // 字符集编码，默认 zh_CN.UTF-8
  "platform": "Windows" // 游戏平台，默认 Windows
}
```

## 使用方式

```shell
# 通过设置别名的方式使用，本机需要安装 JDK8 及以上版本
alias gsgm='java -jar /xxxx/gsgm-xxxx.jar'
```

```shell
# 读取这个配置，支持 bash 和 zsh 的自动补全
source completion.sh
```

## 文档

具体命令的介绍，可以通过 `gsgm xxx -h` 获取

```shell
Usage: gsgm [-hV] [COMMAND]
Gsgm 管理工具
  -h, --help      Show this help message and exit.
  -V, --version   Print version information and exit.
Commands:
  search               查询
  list                 已安装列表
  info                 查询具体游戏
  init                 游戏初始化
  install              安装游戏到 Lutris 中
  uninstall            卸载游戏，但不删游戏文件
  check                检查游戏
  sync                 将 Lutris 数据同步到 Gsgm
  clean                清空 Lutris 游戏库
  generate-completion  Generate bash/zsh completion script for gsgm.
```

```shell
Usage: gsgm search [-hV] -k=<keyword> <libraryPath>...
查询
      <libraryPath>...      游戏库位置
  -k, --keyword=<keyword>   查询关键字
  -h, --help                Show this help message and exit.
  -V, --version             Print version information and exit.
```

```shell
Usage: gsgm list [-hV] [<keyword>]
已安装列表
      [<keyword>]   游戏库位置
  -h, --help        Show this help message and exit.
  -V, --version     Print version information and exit.
```

```shell
Usage: gsgm info [-hV] [-lp[=<libraryPath>...]]... <gsgmId>...
查询具体游戏
      <gsgmId>...   gsgm id: 一般保存到游戏的 .gsgm 文件夹中 info.json 文件中
      -lp, --library-path[=<libraryPath>...]
                    Gsgm 游戏库位置，为空则仅查 Lutris 数据库
  -h, --help        Show this help message and exit.
  -V, --version     Print version information and exit.
```

```shell
Usage: gsgm init [-ihV] [--is-library] (-lp | -wp | -mi) <gameOrLibraryPath>...
游戏初始化
      <gameOrLibraryPath>...
                            游戏路径
      --is-library          是否是 Gsgm 游戏库
  -i, --interactive-mode    交互模式初始化游戏数据
  -h, --help                Show this help message and exit.
  -V, --version             Print version information and exit.
扫描的游戏类型
      -lp, --linux-all      是否全是 Linux 游戏
      -wp, --windows-all    是否全是 Windows 游戏
      -mi, --mix, --mixed   混合游戏
```

```shell
Usage: gsgm install [-fhV] <libraryPath>...
安装游戏到 Lutris 中
      <libraryPath>...   Gsgm 游戏库位置
  -f, --force            强制覆盖同步
  -h, --help             Show this help message and exit.
  -V, --version          Print version information and exit.
```

```shell
Usage: gsgm uninstall [-hV] [-l[=<libraryPathList>...]]... [<gsgm id>...]
卸载游戏，但不删游戏文件
      [<gsgm id>...]   Gsgm 游戏id, 如果不指定，则卸载整个 Gsgm 游戏库
  -l, --library-path[=<libraryPathList>...]
                       Gsgm 游戏库位置
  -h, --help           Show this help message and exit.
  -V, --version        Print version information and exit.
```

```shell
Usage: gsgm check [-hV] [--is-library] <gamePath>...
检查游戏
      <gamePath>...   游戏路径
      --is-library    是否是 Gsgm 游戏库
  -h, --help          Show this help message and exit.
  -V, --version       Print version information and exit.
```

```shell
Usage: gsgm sync [-fhV] <libraryPath>...
将 Lutris 数据同步到 Gsgm
      <libraryPath>...   游戏库位置
  -f, --force            强制覆盖同步
  -h, --help             Show this help message and exit.
  -V, --version          Print version information and exit.
```

```shell
Usage: gsgm clean [-hV]
清空 Lutris 游戏库
  -h, --help      Show this help message and exit.
  -V, --version   Print version information and exit.
```

## 鸣谢列表

- [JColor](https://github.com/dialex/JColor)

