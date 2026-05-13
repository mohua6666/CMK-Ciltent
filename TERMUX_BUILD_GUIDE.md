# MK PVP Client - Termux 本地打包指南

## 作者: Mohua

---

## 重要说明

Termux 中配置 Android SDK 比较复杂，需要耐心完成。以下是详细步骤。

---

## 第一阶段：安装基础工具

### 1. 更新 Termux 包

打开 Termux，执行：

```bash
pkg update && pkg upgrade -y
```

### 2. 安装基础工具

```bash
pkg install -y git wget curl unzip openjdk-17
```

### 3. 配置 Java 环境

```bash
# 设置 JAVA_HOME
export JAVA_HOME="/data/data/com.termux/files/usr/lib/jvm/java-17-openjdk"
export PATH="$JAVA_HOME/bin:$PATH"

# 验证 Java
java -version
```

---

## 第二阶段：安装 Android SDK

### 方法 A：使用 Android SDK 命令行工具（推荐）

#### 1. 创建 SDK 目录

```bash
mkdir -p ~/android-sdk/cmdline-tools
cd ~/android-sdk/cmdline-tools
```

#### 2. 下载 Android Command Line Tools

```bash
# 下载最新版本的 cmdline-tools
wget https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip
unzip commandlinetools-linux-11076708_latest.zip
mv cmdline-tools latest
```

#### 3. 配置环境变量

编辑 `~/.bashrc` 文件：

```bash
nano ~/.bashrc
```

在文件末尾添加：

```bash
export ANDROID_HOME="$HOME/android-sdk"
export ANDROID_SDK_ROOT="$HOME/android-sdk"
export PATH="$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools:$ANDROID_HOME/build-tools/34.0.0"
export JAVA_HOME="/data/data/com.termux/files/usr/lib/jvm/java-17-openjdk"
```

保存并退出（Ctrl+X，然后按 Y 确认）

应用更改：

```bash
source ~/.bashrc
```

#### 4. 接受许可证并安装 SDK 组件

```bash
# 接受所有许可证
yes | sdkmanager --licenses

# 安装必要的组件
sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"
```

#### 5. 验证安装

```bash
sdkmanager --list_installed
```

---

## 第三阶段：克隆项目并构建

### 1. 克隆 GitHub 项目

```bash
cd ~
git clone https://github.com/mohua6666/CMK-Ciltent.git
cd CMK-Ciltent
```

### 2. 设置可执行权限

```bash
chmod +x gradlew
```

### 3. 修复 Gradle Wrapper

如果 gradlew 不工作，下载 gradle-wrapper.jar：

```bash
cd gradle/wrapper
wget https://services.gradle.org/distributions/gradle-8.5-bin.zip -O /tmp/gradle.zip
unzip -j /tmp/gradle.zip "gradle-8.5/lib/plugins/gradle-wrapper-*.jar" -d .
mv gradle-wrapper-*.jar gradle-wrapper.jar
cd ../..
```

### 4. 构建 Debug APK

```bash
./gradlew assembleDebug --no-daemon
```

### 5. 等待构建完成

首次构建可能需要 10-20 分钟，取决于网络速度。

构建成功后，APK 文件位置：

```
CMK-Ciltent/app/build/outputs/apk/debug/app-debug.apk
```

---

## 第四阶段：安装 APK

### 方法 1：复制到 Download 目录

```bash
cp app/build/outputs/apk/debug/app-debug.apk ~/storage/shared/Download/
```

然后在手机文件管理器中找到并安装。

### 方法 2：直接通过 Termux 安装

```bash
# 安装 APK
pkg install -y termux-api
termux-open app/build/outputs/apk/debug/app-debug.apk
```

---

## 常见问题

### Q1: sdkmanager 找不到命令？
确保已正确设置 PATH 环境变量，并重新加载：
```bash
source ~/.bashrc
```

### Q2: 下载超时？
可以尝试使用代理或更换网络环境。

### Q3: 构建失败？
检查 Java 版本是否为 17，Android SDK 是否正确安装。

### Q4: 存储权限问题？
在 Termux 中运行：
```bash
termux-setup-storage
```

---

## 简化版本（如果以上步骤太复杂）

如果完整配置太复杂，可以尝试简化方案：

### 只安装最小必要组件：

```bash
# 安装 JDK
pkg install -y openjdk-17

# 配置 Java
export JAVA_HOME="/data/data/com.termux/files/usr/lib/jvm/java-17-openjdk"

# 克隆项目
git clone https://github.com/mohua6666/CMK-Ciltent.git
cd CMK-Ciltent

# 尝试直接用 Gradle 构建（不使用 wrapper）
gradle assembleDebug
```

---

## 注意事项

⚠️ Termux 中运行 Android 构建非常消耗资源
⚠️ 可能需要较长时间完成
⚠️ 确保手机有足够的存储空间（至少 2GB）
⚠️ 建议连接稳定的 WiFi

---

## 下一步

完成以上步骤后，你的 APK 就可以安装到手机上了！

祝你打包顺利！🎮
