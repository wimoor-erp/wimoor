#!/bin/bash

# ========================================
#   飞书 SSL 证书导入脚本 (Linux)
# ========================================

set -e

# ============================================
# 配置区域 - 可根据需要修改
# ============================================
CERT_HOST="open.feishu.cn"
CERT_PORT="443"
CERT_ALIAS="feishu"
KEYSTORE_PASS="changeit"

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 打印带颜色的消息
print_info() {
    echo -e "${GREEN}[信息]${NC} $1"
}

print_error() {
    echo -e "${RED}[错误]${NC} $1"
}

print_warn() {
    echo -e "${YELLOW}[警告]${NC} $1"
}

echo "========================================"
echo "   飞书 SSL 证书导入脚本 (Linux)"
echo "========================================"
echo

# ============================================
# 检查 root 权限
# ============================================
if [ "$(id -u)" -ne 0 ]; then
    print_error "请以 root 用户运行此脚本！"
    echo
    echo "操作方法："
    echo "  sudo bash $0"
    echo "  或者"
    echo "  sudo ./load_feishu_cert.sh"
    echo
    exit 1
fi

# ============================================
# 检查 JAVA_HOME 环境变量
# ============================================
if [ -z "$JAVA_HOME" ]; then
    # 尝试自动检测 JAVA_HOME
    print_warn "未设置 JAVA_HOME，尝试自动检测..."
    
    # 尝试从 java 命令获取路径
    if command -v java &> /dev/null; then
        JAVA_PATH=$(readlink -f $(which java))
        JAVA_HOME=$(echo "$JAVA_PATH" | sed 's|/bin/java||')
        # 处理 JRE 路径
        if echo "$JAVA_HOME" | grep -q "/jre"; then
            JAVA_HOME=$(echo "$JAVA_HOME" | sed 's|/jre||')
        fi
        print_info "自动检测到 JAVA_HOME: $JAVA_HOME"
    else
        print_error "未设置 JAVA_HOME 环境变量，且无法自动检测！"
        echo
        echo "请先设置 JAVA_HOME 环境变量："
        echo "  export JAVA_HOME=/usr/lib/jvm/java-17-openjdk"
        echo "  或添加到 /etc/profile 或 ~/.bashrc"
        echo
        exit 1
    fi
fi

# 验证 JAVA_HOME 目录存在
if [ ! -d "$JAVA_HOME" ]; then
    print_error "JAVA_HOME 目录不存在: $JAVA_HOME"
    exit 1
fi

# ============================================
# 检查 keytool 是否存在
# ============================================
KEYTOOL="$JAVA_HOME/bin/keytool"
if [ ! -f "$KEYTOOL" ]; then
    print_error "未找到 keytool 工具！"
    echo "路径: $KEYTOOL"
    echo
    echo "请检查 JAVA_HOME 是否指向正确的 JDK 目录"
    echo
    exit 1
fi

# 确保 keytool 有执行权限
chmod +x "$KEYTOOL"

# ============================================
# 确定 cacerts 文件路径
# ============================================
CACERTS="$JAVA_HOME/lib/security/cacerts"
if [ ! -f "$CACERTS" ]; then
    # JDK 9+ 使用 conf 目录
    CACERTS="$JAVA_HOME/conf/security/cacerts"
    if [ ! -f "$CACERTS" ]; then
        print_error "未找到 cacerts 文件！"
        echo "已尝试路径:"
        echo "  - $JAVA_HOME/lib/security/cacerts"
        echo "  - $JAVA_HOME/conf/security/cacerts"
        echo
        exit 1
    fi
fi

print_info "JAVA_HOME: $JAVA_HOME"
print_info "cacerts 路径: $CACERTS"
print_info "证书主机: $CERT_HOST:$CERT_PORT"
print_info "证书别名: $CERT_ALIAS"
echo

# ============================================
# 检查依赖工具
# ============================================
if ! command -v openssl &> /dev/null; then
    print_warn "未找到 openssl，将使用 keytool 获取证书"
    USE_KEYTOOL=true
else
    USE_KEYTOOL=false
fi

# ============================================
# 检查证书是否已存在
# ============================================
print_info "[步骤 1/3] 检查证书是否已存在..."
if "$KEYTOOL" -list -alias "$CERT_ALIAS" -keystore "$CACERTS" -storepass "$KEYSTORE_PASS" &> /dev/null; then
    print_warn "证书已存在，将先删除旧证书..."
    "$KEYTOOL" -delete -alias "$CERT_ALIAS" -keystore "$CACERTS" -storepass "$KEYSTORE_PASS" &> /dev/null
    if [ $? -ne 0 ]; then
        print_error "删除旧证书失败！"
        exit 1
    fi
    print_info "旧证书已删除"
fi

# ============================================
# 获取服务器证书
# ============================================
print_info "[步骤 2/3] 正在从 $CERT_HOST:$CERT_PORT 获取证书..."

# 创建临时文件
TEMP_CERT=$(mktemp /tmp/feishu_cert_XXXXXX.cer)
TEMP_LEAF=$(mktemp /tmp/feishu_leaf_XXXXXX.cer)

# 确保临时文件在退出时清理
trap "rm -f '$TEMP_CERT' '$TEMP_LEAF'" EXIT

if [ "$USE_KEYTOOL" = true ]; then
    # 使用 keytool 获取证书
    "$KEYTOOL" -printcert -sslserver "$CERT_HOST:$CERT_PORT" -rfc > "$TEMP_CERT" 2>/dev/null
    if [ $? -ne 0 ]; then
        print_error "获取证书失败！"
        echo
        echo "可能的原因:"
        echo "  - 网络连接问题"
        echo "  - 防火墙阻止连接"
        echo "  - DNS 解析失败"
        echo
        echo "请检查网络连接后重试"
        exit 1
    fi
    
    # 提取第一个证书（叶子证书）
    awk '/-----BEGIN CERTIFICATE-----/{found=1} found{print} /-----END CERTIFICATE-----/{if(found) exit}' "$TEMP_CERT" > "$TEMP_LEAF"
else
    # 使用 openssl 获取证书
    echo | openssl s_client -connect "$CERT_HOST:$CERT_PORT" -servername "$CERT_HOST" 2>/dev/null | \
        openssl x509 -outform PEM > "$TEMP_LEAF" 2>/dev/null
    if [ $? -ne 0 ] || [ ! -s "$TEMP_LEAF" ]; then
        print_error "使用 openssl 获取证书失败，尝试使用 keytool..."
        "$KEYTOOL" -printcert -sslserver "$CERT_HOST:$CERT_PORT" -rfc > "$TEMP_CERT" 2>/dev/null
        awk '/-----BEGIN CERTIFICATE-----/{found=1} found{print} /-----END CERTIFICATE-----/{if(found) exit}' "$TEMP_CERT" > "$TEMP_LEAF"
    fi
fi

# 验证证书文件
if [ ! -s "$TEMP_LEAF" ]; then
    print_error "获取证书失败！"
    exit 1
fi

# 显示证书信息
print_info "证书获取成功"
echo "证书信息:"
openssl x509 -in "$TEMP_LEAF" -noout -subject -issuer -dates 2>/dev/null || true
echo

# ============================================
# 导入证书到 cacerts
# ============================================
print_info "[步骤 3/3] 正在导入证书到 JVM 信任库..."

"$KEYTOOL" -import -alias "$CERT_ALIAS" -keystore "$CACERTS" -file "$TEMP_LEAF" -storepass "$KEYSTORE_PASS" -noprompt -trustcacerts
if [ $? -ne 0 ]; then
    print_error "导入证书失败！"
    exit 1
fi

# 清理临时文件
rm -f "$TEMP_CERT" "$TEMP_LEAF"

echo
echo "========================================"
echo -e "   ${GREEN}[成功] 证书导入完成！${NC}"
echo "========================================"
echo
echo "证书信息:"
echo "  别名: $CERT_ALIAS"
echo "  主机: $CERT_HOST:$CERT_PORT"
echo "  信任库: $CACERTS"
echo
echo "请重启应用程序使证书生效"
echo

# 显示导入的证书
"$KEYTOOL" -list -alias "$CERT_ALIAS" -keystore "$CACERTS" -storepass "$KEYSTORE_PASS"

echo
