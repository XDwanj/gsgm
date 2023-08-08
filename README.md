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
  remove               删除游戏
  check                检查游戏
  sync                 同步数据
  clean                清空 Lutris 游戏库
  generate-completion  Generate bash/zsh completion script for gsgm.
```

## 鸣谢列表

- [JColor](https://github.com/dialex/JColor)

