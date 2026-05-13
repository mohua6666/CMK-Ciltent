# MK PVP Client - Android 打包指南

## 作者: Mohua

---

## 重要说明

在 Termux 中直接构建 Android APK 比较复杂，因为需要 Android SDK 和完整的工具链。

以下提供**三种方法**来打包你的应用：

---

## 📱 方法一：使用 Android Studio（推荐，最简单）

### 步骤概述

这是最简单、最可靠的方法。

### 详细步骤

#### 1. 从 GitHub 克隆项目

在你的电脑上（Windows/Mac/Linux）打开终端：

```bash
git clone https://github.com/mohua6666/CMK-Ciltent.git
cd CMK-Ciltent
```

#### 2. 安装 Android Studio

下载并安装：https://developer.android.com/studio

#### 3. 打开项目

1. 打开 Android Studio
2. 点击 "Open an Existing Project"
3. 选择刚才克隆的 `CMK-Ciltent` 文件夹
4. 等待 Gradle 同步（首次可能需要 5-10 分钟）

#### 4. 构建 Debug APK

点击菜单：
```
Build → Build Bundle(s)/APK(s) → Build APK(s)
```

构建完成后，点击通知中的 "locate" 或查看：
```
app/build/outputs/apk/debug/app-debug.apk
```

#### 5. 安装到手机

1. 将 `app-debug.apk` 传输到手机
2. 手机上打开文件管理器
3. 点击 APK 进行安装
4. 允许 "未知来源" 安装

---

## 🐧 方法二：在 Termux 中尝试构建（高级）

### 注意

这个方法比较复杂，且可能有兼容性问题。建议使用方法一。

### 步骤

#### 1. 更新 Termux 包

```bash
pkg update && pkg upgrade -y
```

#### 2. 安装依赖

```bash
pkg install -y git wget tar openjdk-17
```

#### 3. 下载 Android Command Line Tools

```bash
cd ~
mkdir -p android-sdk/cmdline-tools
cd android-sdk/cmdline-tools

# 下载 commandlinetools (需要查找最新版本)
# 访问 https://developer.android.com/studio#command-tools 获取最新链接
```

#### 4. 配置环境变量

在 `~/.bashrc` 或 `~/.zshrc` 中添加：

```bash
export ANDROID_HOME="$HOME/android-sdk"
export PATH="$PATH:$ANDROID_HOME/cmdline-tools/latest/bin"
export PATH="$PATH:$ANDROID_HOME/platform-tools"
```

应用更改：
```bash
source ~/.bashrc
```

#### 5. 安装 Android SDK 组件

```bash
sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"
```

#### 6. 进入项目目录

```bash
cd /workspace
```

#### 7. 修复 Gradle Wrapper

```bash
# 下载 gradle-wrapper.jar
mkdir -p gradle/wrapper
cd gradle/wrapper
wget https://raw.githubusercontent.com/gradle/gradle/v8.2.0/gradle/wrapper/gradle-wrapper.jar
cd ../..
```

#### 8. 尝试构建

```bash
chmod +x gradlew
./gradlew assembleDebug
```

---

## 💻 方法三：使用 GitHub Actions（自动化构建）

### 步骤

#### 1. 创建 GitHub Actions Workflow

在项目中创建文件 `.github/workflows/build.yml`：

```yaml
name: Build APK

on:
  push:
    branches: [ master ]
  pull_request:
    branches: [ master ]
  workflow_dispatch:

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'

      - name: Grant execute permission for gradlew
        run: chmod +x gradlew

      - name: Build Debug APK
        run: ./gradlew assembleDebug

      - name: Upload APK
        uses: actions/upload-artifact@v4
        with:
          name: app-debug
          path: app/build/outputs/apk/debug/app-debug.apk
```

#### 2. 提交并推送

```bash
git add .github/workflows/build.yml
git commit -m "Add GitHub Actions build workflow"
git push
```

#### 3. 下载构建好的 APK

1. 访问你的 GitHub 仓库
2. 点击 "Actions" 标签
3. 找到最新的 workflow run
4. 在 "Artifacts" 部分下载 `app-debug`

---

## 📋 当前环境状态检查

当前 Termux 环境中已安装：

✅ Java 25.0.2  
✅ Git 2.43.0  
✅ Gradle 8.14.4  

❌ Android SDK 未安装  
❌ Gradle wrapper jar 缺失  

---

## 🎯 推荐方案

**建议使用方法一（Android Studio）**，原因：

1. ✅ 最简单可靠
2. ✅ 图形化界面，易于调试
3. ✅ 官方支持，文档完善
4. ✅ 可以直接安装到真机测试

---

## 🔧 项目信息

```
项目: MK PVP Client
包名: com.mk.pvp.client
最低版本: Android 5.0 (API 21)
目标版本: Android 14 (API 34)
版本号: 1.0.0
作者: Mohua
```

---

## 📖 详细文档

完整的使用指南请查看项目中的 `RUN_GUIDE.md` 文件。

---

## 💡 下一步

1. 如果你有电脑，使用方法一
2. 如果想自动化，使用方法三
3. 只有在特殊情况下才尝试方法二

---

祝你打包顺利！🎮
