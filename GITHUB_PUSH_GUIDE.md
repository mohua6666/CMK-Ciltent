# MK PVP Client - GitHub 推送指南

## 作者: Mohua

---

## 方法一：使用 GitHub Personal Access Token（推荐）

### 第一步：创建 Personal Access Token

1. 访问 GitHub Settings: https://github.com/settings/tokens
2. 点击 "Generate new token (classic)"
3. 设置 Token 名称（例如：MKPVPClient-Push）
4. 勾选以下权限：
   - ✅ repo (Full control of private repositories)
5. 点击 "Generate token"
6. **重要：立即复制并保存 Token**，关闭页面后将无法再次查看

### 第二步：在 Termux 中执行推送

打开 Termux，执行以下命令：

```bash
# 进入项目目录
cd /workspace

# 配置 Git 用户信息
git config --global user.name "mohua6666"
git config --global user.email "你的GitHub邮箱"

# 添加远程仓库
git remote add origin https://github.com/mohua6666/CMK-Ciltent.git

# 设置凭证存储
git config --global credential.helper store

# 推送代码（会提示输入用户名和 Token）
git push -u origin master
```

当提示输入用户名时，输入：`mohua6666`
当提示输入密码时，**输入你创建的 Personal Access Token**（不是 GitHub 密码）

### 第三步：验证推送成功

推送成功后，访问你的仓库：
https://github.com/mohua6666/CMK-Ciltent

---

## 方法二：使用 SSH Key（高级）

### 第一步：生成 SSH Key

在 Termux 中执行：

```bash
# 生成 SSH Key
ssh-keygen -t ed25519 -C "你的GitHub邮箱"

# 启动 SSH 代理
eval "$(ssh-agent -s)"

# 添加 SSH Key 到代理
ssh-add ~/.ssh/id_ed25519

# 查看公钥
cat ~/.ssh/id_ed25519.pub
```

### 第二步：添加 SSH Key 到 GitHub

1. 访问 https://github.com/settings/keys
2. 点击 "New SSH key"
3. 粘贴刚才生成的公钥内容
4. 点击 "Add SSH key"

### 第三步：修改远程仓库地址

```bash
cd /workspace
git remote set-url origin git@github.com:mohua6666/CMK-Ciltent.git

# 推送代码
git push -u origin master
```

---

## 方法三：手动下载项目文件到电脑

如果你无法使用上述方法，可以：

### 第一步：打包项目

在 Termux 中执行：

```bash
cd /workspace
zip -r MKPVPClient.zip . -x "*.git*" -x ".uploads/*"
```

### 第二步：复制到可访问位置

```bash
# 复制到下载目录
cp MKPVPClient.zip /sdcard/Download/
```

### 第三步：在电脑上操作

1. 从手机下载 MKPVPClient.zip
2. 解压到任意位置
3. 在解压后的文件夹中打开终端（Windows 用 PowerShell，Mac 用 Terminal）
4. 执行：

```bash
git init
git add .
git commit -m "MKPVPClient by Mohua"
git remote add origin https://github.com/mohua6666/CMK-Ciltent.git
git push -u origin master
```

---

## 常见问题

### Q1: 推送时被拒绝 (403 Forbidden)
**原因**: Token 权限不足或已过期
**解决**: 确保 Token 具有 `repo` 权限，或创建新 Token

### Q2: 提示 "Repository not found"
**原因**: 仓库名称错误或仓库不存在
**解决**: 确认仓库地址为 `https://github.com/mohua6666/CMK-Ciltent.git`

### Q3: 需要用户名密码但不知道在哪里
**原因**: GitHub 已停止支持密码认证
**解决**: 使用 Personal Access Token 代替密码

### Q4: 如何查看是否已正确配置？
执行以下命令检查：

```bash
git config --list
git remote -v
```

---

## 项目信息

- **项目名称**: MK PVP Client
- **作者**: Mohua
- **仓库地址**: https://github.com/mohua6666/CMK-Ciltent
- **包含文件**: 74 个文件
- **总代码量**: 6938 行

---

## 下一步

推送成功后，你可以：

1. 在 GitHub 上查看代码
2. 在 Android Studio 中克隆项目
3. 构建 Debug APK
4. 安装到手机测试

祝你使用愉快！🎮
