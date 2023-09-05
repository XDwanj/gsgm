# Gsgm - GNU Single Game Manager

## 介绍

目前 Gsgm 要联合 Lutris 一起使用，与 Lutris 一起能够很方便的管理自定义游戏库。

Gsgm 不止能够完成全自动批量安装游戏的功能，还有诸如 Lutris 记录备份、Lutris 封面管理等，还有游戏库组管理(需要 `lutris-git v0.5.13+`)

Gsgm 的特点和功能：

1. 根据规则批量导入游戏，只需要遵循规则，然后输入 `gsgm install <游戏库位置>` 即可（详细请看下文
2. 同步 Lutris 的游戏记录和设置，即便 Lutris 的记录丢了，我们也可以通过游戏库中，每个游戏下的 `.gsgm` 文件恢复游戏库的 Lutris 数据，支持 Lutris 和 Gsgm 数据双向流动
3. 在 Lutris 上，封面的管理相当随意，只有联合 Steam 等平台的游戏封面才能很好的被管理。Gsgm 脱离 Lutris，可以主动生成 Lutris 的 3 种显示封面，并代为管理

*注意：使用 Gsgm 的时候，请关闭 Lutris!!!，Gsgm 使用过程中可能会操作 Lutris 数据库，可能会导致运行中的 Lutris 发生错误*

## 游戏库规则

*请仔细阅读，这个结构至关重要！！！*

```
游戏库位置
├── 游戏A
│   ├── ...                 -------- 其他文件夹和文件
│   ├── .gsgm
│   │     ├── info.json     -------- 游戏的信息
│   │     ├── setting.json  -------- 游戏的运行设置
│   │     ├── history.json  -------- 游玩历史
│   │     ├── cover.xxx     -------- 游戏封面，可以是 [png | jpg | jpeg]
│   │     └── winePrefix    -------- 默认的 wineprefix 位置
│   └── bin
│        └── 游戏A.exe       -------- 游戏可执行文件，可以识别游戏根目录和二级目录的可执行文件
├── 游戏B
│   ├── ...                 -------- 其他文件夹和文件
│   ├── .gsgm
│   │     └── ...
│   └── 游戏B.exe            -------- 游戏可执行文件，wei chu
├── @文件夹A                 -------- 文件夹的命名是单个 @ 开头，例如 `@3D大作`、`@ 2D游戏`，文件夹可以嵌套
│   ├── 游戏A
│   │   └── ...
│   └── @文件夹B             -------- 文件夹可以嵌套
│       └── ...
├── @文件夹C                 -------- 文件夹的命名是单个 @ 开头，例如 `@3D大作`、`@ 2D游戏`，文件夹可以嵌套
│   ├── .is-group           -------- 分组标识，有这个文件的文件夹，会把下面的所有游戏分为一组，组名则是标识文件所在目录名
│   ├── 游戏C
│   │   └── ...
│   ├── 游戏D
│   │   └── ...
│   └── @文件夹B             -------- 文件夹可以嵌套
│       └── ...
├── @@忽略文件夹A             -------- 忽略文件夹以两个 @@ 开头，例如 `@@游戏脚本`、`@@ 风灵月影工具包`
├── @@忽略文件夹B
└── ...
```

info.json

```json5
{
  "id": 1684470185450471424, // `gsgm init 随机生成 gsgm id，每个游戏独一无二，不可重复！
}
```

setting.json

```json5
{
  "executeLocation": "PureStation.exe", // 游戏启动文件，路径相对于游戏根目录
  "winePrefix": ".gsgm/winePrefix", // wineprefix 路径，相对于游戏根目录，默认为 `.gsgm/winePrefix`
  "localeCharSet": "zh_CN.UTF-8", // 字符集编码，默认 zh_CN.UTF-8
  "platform": "Windows" // 游戏平台，默认 Windows，可选值：[Windows | Linux]
}
```

history.json

```json5
{
    "lastGameMoment": 1692364512968, // 上次游玩时间的时间戳(毫秒值)
    "gameTime": 1 // 总游玩时间，小数形式，单位是小时
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

# completion.sh 可以自己生成
java -jar /xxxx/gsgm-xxxx.jar generate-completion > completion.sh
```

## 文档

具体命令的介绍，可以通过 `gsgm xxx -h` 获取

```
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

```
Usage: gsgm search [-hV] -k=<keyword> <libraryPath>...
查询
      <libraryPath>...      游戏库位置
  -k, --keyword=<keyword>   查询关键字
  -h, --help                Show this help message and exit.
  -V, --version             Print version information and exit.
```

```
Usage: gsgm list [-hV] [<keyword>]
已安装列表
      [<keyword>]   游戏库位置
  -h, --help        Show this help message and exit.
  -V, --version     Print version information and exit.
```

```
Usage: gsgm info [-hV] [-lp[=<libraryPath>...]]... <gsgmId>...
查询具体游戏
      <gsgmId>...   gsgm id: 一般保存到游戏的 .gsgm 文件夹中 info.json 文件中
      -lp, --library-path[=<libraryPath>...]
                    Gsgm 游戏库位置，为空则仅查 Lutris 数据库
  -h, --help        Show this help message and exit.
  -V, --version     Print version information and exit.
```

```
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

```
Usage: gsgm install [-fhV] <libraryPath>...
安装游戏到 Lutris 中
      <libraryPath>...   Gsgm 游戏库位置
  -f, --force            强制覆盖同步
  -h, --help             Show this help message and exit.
  -V, --version          Print version information and exit.
```

```
Usage: gsgm uninstall [-hV] [-l[=<libraryPathList>...]]... [<gsgm id>...]
卸载游戏，但不删游戏文件
      [<gsgm id>...]   Gsgm 游戏id, 如果不指定，则卸载整个 Gsgm 游戏库
  -l, --library-path[=<libraryPathList>...]
                       Gsgm 游戏库位置
  -h, --help           Show this help message and exit.
  -V, --version        Print version information and exit.
```

```
Usage: gsgm check [-hV] [--is-library] <gamePath>...
检查游戏
      <gamePath>...   游戏路径
      --is-library    是否是 Gsgm 游戏库
  -h, --help          Show this help message and exit.
  -V, --version       Print version information and exit.
```

```
Usage: gsgm sync [-fhV] <libraryPath>...
将 Lutris 数据同步到 Gsgm
      <libraryPath>...   游戏库位置
  -f, --force            强制覆盖同步
  -h, --help             Show this help message and exit.
  -V, --version          Print version information and exit.
```

```
Usage: gsgm clean [-hV]
清空 Lutris 游戏库
  -h, --help      Show this help message and exit.
  -V, --version   Print version information and exit.
```

## 常见操作举例

清空Gsgm的游戏库

> 这里将清除所有通过 Gsgm 安装的游戏数据

```shell
gsgm clean
```

### 初始化游戏库

> 交互模式的作用在于手动填写游戏平台和选择可执行文件

```shell
# 指定初始化的是游戏库文件夹，且使用交互模式，库内的游戏既有 linux 也有 Windows 平台
gsgm init -i -mi --is-library /path/of/library

# 初始化的是单个游戏，且使用交互模式，所有的游戏均为 Windows 平台，一次性初始化两个游戏
gsgm init -i -wp /path/of/game1 /path/of/game2

# 非交互模式初始化游戏，指定游戏为 Windows 平台，初始化完成后需要自己填写可执行文件位置
gsgm init -wp /path/of/game
```

### 检查游戏结构

```shell
# 检查游戏库的结构是否合法
gsgm check --is-library /path/of/library

# 检查这两个游戏的结构是否合法
gsgm check /path/of/game1 /path/of/game2
```

### 安装游戏库

```shell
# 同步选择的游戏库数据到 Lutris 中
gsgm install /path/of/library

# 强制同步
gsgm install -f /path/of/library
```

### 同步数据

```shell
# 将当前 Lutris 中的数据，同步到指定游戏库
gsgm sync /path/of/library

# 强制同步
gsgm sync -f /path/of/library
```

### 罗列已安装游戏

```shell
# 罗列当前 Lutris 安装的 Gsgm 游戏
gsgm list

# 基于游戏名关键字，罗列 Lutris 当前安装的 Gsgm 游戏
gsgm list <keyword>
```

### 查看具体信息

```shell
# 指定一个游戏库位置，通过 gsgmid，查询 游戏信息，gsgmid，一般通过 list 命令获得
gsgm -lp /path/of/library <gsgmid>

# 效果与上面一致，但是指定两个游戏库位置
gsgm -lp /path/of/library1 /path/of/library1 <gsgmid>
```

## Beta功能

### 分组功能

> 截止 2023-08-18，分组功能还需要 lutris 版本为 `v0.5.13+ `(或者 lutris-git)

由于库的设计理念，不允许将 `.is-group` 文件放在库根目录(放了也是忽略)，在游戏库结构中已经提到了分组结构，这里进行进一步的解释

- 未被分组的游戏会被分类为 `$default`

- 普通分组命名则是 `@xxxx`

已经被分组的游戏，将不会进入 `$default` 组中，也就是说 `$default` 组中的游戏是没有被明确分组的游戏

**效果图**

![image-20230818215356813](README.assets/image-20230818215356813.png)

## 鸣谢列表

- [JColor](https://github.com/dialex/JColor)

