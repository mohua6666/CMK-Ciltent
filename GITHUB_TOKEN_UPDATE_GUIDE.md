# GitHub Token 更新指南

## 问题说明

刚才推送失败是因为你的 Personal Access Token 缺少 `workflow` 权限。

---

## 解决方案

需要更新 Token 或创建新的 Token。

---

## 步骤 1：访问 Token 设置页面

打开链接：
**https://github.com/settings/tokens**

---

## 步骤 2：更新现有 Token 或创建新的

### 选项 A：更新现有 Token（推荐）

1. 找到你刚才创建的 "MKPVPClient-Push" Token
2. 点击 "Edit"
3. 找到 "Scopes" 部分
4. ✅ 勾选 `workflow` （在 repo 下面，或者单独的选项）
5. 确保以下权限都已勾选：
   - ✅ `repo` (完整权限)
   - ✅ `workflow` (新增！)
6. 点击 "Update token" 保存

### 选项 B：创建新的 Token

1. 点击 "Generate new token" → "Generate new token (classic)"
2. 输入名称：`MKPVPClient-Full`
3. 勾选以下权限：
   - ✅ `repo` (全部勾选)
   - ✅ `workflow` (重要！)
   - ✅ `write:packages` (可选)
   - ✅ `user` (可选)
4. 点击 "Generate token"
5. **立即复制保存新的 Token**！

---

## 步骤 3：使用新 Token 重新推送

**方案 A：在网页上直接上传文件（最简单）**

1. 访问你的 GitHub 仓库：
   https://github.com/mohua6666/CMK-Ciltent

2. 点击 "Add file" → "Upload files"

3. 从你的设备上传以下文件：
   - `.github/workflows/build.yml`
   - `gradle/wrapper/gradle-wrapper.jar`
   - `ANDROID_BUILD_GUIDE.md`
   - （注意：需要先在设备上找到这些文件，或者我可以通过对话发给你）

4. 填写提交信息：`Add GitHub Actions auto-build workflow`

5. 点击 "Commit changes"

**方案 B：使用新的 Token 重新配置**

如果你选择创建新的 Token，请将新的 Token 发给我，格式：
```
ghp_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
```

---

## 步骤 4：触发自动构建

推送成功后：

1. 访问你的 GitHub 仓库
2. 点击 "Actions" 标签
3. 应该能看到 "Build APK" 工作流正在运行
4. 等待约 3-5 分钟
5. 构建完成后，点击运行记录
6. 在页面底部 "Artifacts" 部分下载 `app-debug`

---

## 📋 权限检查清单

你的 Token 必须具有：
- ✅ `repo` 权限
- ✅ `workflow` 权限（这次必须加！）

---

## 快速开始建议

**最简单的方法**：我把需要的文件内容直接通过对话发给你，你可以：

1. 在 GitHub 网页上直接创建这些文件
2. 或者把项目从 GitHub 下载到电脑，修改后重新上传

你想选择哪种方式？
