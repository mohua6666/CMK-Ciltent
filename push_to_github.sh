#!/bin/bash

# MK PVP Client - GitHub 推送脚本
# 作者: Mohua

echo "================================"
echo "  MK PVP Client - GitHub 推送"
echo "================================"
echo ""

# 检查是否已安装 git
if ! command -v git &> /dev/null; then
    echo "错误: 未安装 git"
    echo "请先安装 git: apt install git"
    exit 1
fi

# 配置 Git 用户信息
echo "步骤1: 配置 Git 用户信息"
read -p "请输入 GitHub 用户名: " github_user
read -p "请输入 GitHub 邮箱: " github_email

git config --global user.name "$github_user"
git config --global user.email "$github_email"

echo ""
echo "Git 用户信息已配置:"
echo "  用户名: $(git config --global user.name)"
echo "  邮箱: $(git config --global user.email)"
echo ""

# 添加远程仓库
echo "步骤2: 添加远程仓库"
echo "仓库地址: https://github.com/mohua6666/CMK-Ciltent.git"
git remote add origin https://github.com/mohua6666/CMK-Ciltent.git
echo ""

# 推送到 GitHub
echo "步骤3: 推送到 GitHub"
echo "正在推送..."
echo ""

# 使用 GitHub Personal Access Token
echo "需要 GitHub Personal Access Token"
echo "如果没有，请访问: https://github.com/settings/tokens"
read -sp "请输入 Token: " github_token
echo ""

# 设置凭证存储
git config --global credential.helper store
echo "https://${github_token}@github.com" > ~/.git-credentials

# 推送代码
git push -u origin master

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ 推送成功！"
    echo "访问你的仓库: https://github.com/mohua6666/CMK-Ciltent"
else
    echo ""
    echo "❌ 推送失败，请检查 Token 权限"
    echo "确保 Token 具有 repo 权限"
fi

echo ""
echo "================================"
