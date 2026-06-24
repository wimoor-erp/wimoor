@echo off
setlocal enabledelayedexpansion

echo ========================================
echo   飞书 SSL 证书导入脚本 (Windows)
echo ========================================
echo.

:: ============================================
:: 配置区域 - 可根据需要修改
:: ============================================
set "CERT_HOST=open.feishu.cn"
set "CERT_PORT=443"
set "CERT_ALIAS=feishu"
set "KEYSTORE_PASS=changeit"

:: ============================================
:: 检查管理员权限
:: ============================================
net session >nul 2>&1
if %errorlevel% neq 0 (
    echo [错误] 请以管理员身份运行此脚本！
    echo.
    echo 操作方法：
    echo   1. 右键点击此脚本
    echo   2. 选择"以管理员身份运行"
    echo.
    pause
    exit /b 1
)

:: ============================================
:: 检查 JAVA_HOME 环境变量
:: ============================================
if "%JAVA_HOME%"=="" (
    echo [错误] 未设置 JAVA_HOME 环境变量！
    echo.
    echo 请先设置 JAVA_HOME 环境变量指向 JDK 安装目录
    echo 示例: set JAVA_HOME=C:\Program Files\Java\jdk-17
    echo.
    pause
    exit /b 1
)

:: 检查 keytool 是否存在
set "KEYTOOL=%JAVA_HOME%\bin\keytool.exe"
if not exist "%KEYTOOL%" (
    echo [错误] 未找到 keytool 工具！
    echo 路径: %KEYTOOL%
    echo.
    echo 请检查 JAVA_HOME 是否指向正确的 JDK 目录
    echo.
    pause
    exit /b 1
)

:: ============================================
:: 确定 cacerts 文件路径
:: ============================================
set "CACERTS=%JAVA_HOME%\lib\security\cacerts"
if not exist "%CACERTS%" (
    :: JDK 9+ 使用 conf 目录
    set "CACERTS=%JAVA_HOME%\conf\security\cacerts"
    if not exist "%CACERTS%" (
        echo [错误] 未找到 cacerts 文件！
        echo 已尝试路径:
        echo   - %JAVA_HOME%\lib\security\cacerts
        echo   - %JAVA_HOME%\conf\security\cacerts
        echo.
        pause
        exit /b 1
    )
)

echo [信息] JAVA_HOME: %JAVA_HOME%
echo [信息] cacerts 路径: %CACERTS%
echo [信息] 证书主机: %CERT_HOST%:%CERT_PORT%
echo [信息] 证书别名: %CERT_ALIAS%
echo.

:: ============================================
:: 检查证书是否已存在
:: ============================================
echo [步骤 1/3] 检查证书是否已存在...
"%KEYTOOL%" -list -alias %CERT_ALIAS% -keystore "%CACERTS%" -storepass %KEYSTORE_PASS% >nul 2>&1
if %errorlevel% equ 0 (
    echo [信息] 证书已存在，将先删除旧证书...
    "%KEYTOOL%" -delete -alias %CERT_ALIAS% -keystore "%CACERTS%" -storepass %KEYSTORE_PASS% >nul 2>&1
    if %errorlevel% neq 0 (
        echo [错误] 删除旧证书失败！
        pause
        exit /b 1
    )
    echo [成功] 旧证书已删除
)

:: ============================================
:: 获取服务器证书
:: ============================================
echo [步骤 2/3] 正在从 %CERT_HOST%:%CERT_PORT% 获取证书...

:: 创建临时文件
set "TEMP_CERT=%TEMP%\feishu_cert_%RANDOM%.cer"

:: 使用 keytool 获取证书
"%KEYTOOL%" -printcert -sslserver %CERT_HOST%:%CERT_PORT% -rfc > "%TEMP_CERT%" 2>nul
if %errorlevel% neq 0 (
    echo [错误] 获取证书失败！
    echo.
    echo 可能的原因:
    echo   - 网络连接问题
    echo   - 防火墙阻止连接
    echo   - DNS 解析失败
    echo.
    echo 请检查网络连接后重试
    if exist "%TEMP_CERT%" del "%TEMP_CERT%"
    pause
    exit /b 1
)

:: 提取第一个证书（叶子证书）
set "TEMP_LEAF=%TEMP%\feishu_leaf_%RANDOM%.cer"
powershell -Command "& { $content = Get-Content '%TEMP_CERT%' -Raw; $endIndex = $content.IndexOf('-----END CERTIFICATE-----') + '-----END CERTIFICATE-----'.Length; $firstCert = $content.Substring(0, $endIndex); Set-Content -Path '%TEMP_LEAF%' -Value $firstCert -NoNewline -Encoding ASCII }"

if not exist "%TEMP_LEAF%" (
    echo [错误] 提取叶子证书失败！
    if exist "%TEMP_CERT%" del "%TEMP_CERT%"
    pause
    exit /b 1
)

echo [成功] 证书获取成功

:: ============================================
:: 导入证书到 cacerts
:: ============================================
echo [步骤 3/3] 正在导入证书到 JVM 信任库...

"%KEYTOOL%" -import -alias %CERT_ALIAS% -keystore "%CACERTS%" -file "%TEMP_LEAF%" -storepass %KEYSTORE_PASS% -noprompt -trustcacerts
if %errorlevel% neq 0 (
    echo [错误] 导入证书失败！
    if exist "%TEMP_CERT%" del "%TEMP_CERT%"
    if exist "%TEMP_LEAF%" del "%TEMP_LEAF%"
    pause
    exit /b 1
)

:: 清理临时文件
if exist "%TEMP_CERT%" del "%TEMP_CERT%"
if exist "%TEMP_LEAF%" del "%TEMP_LEAF%"

echo.
echo ========================================
echo   [成功] 证书导入完成！
echo ========================================
echo.
echo 证书信息:
echo   别名: %CERT_ALIAS%
echo   主机: %CERT_HOST%:%CERT_PORT%
echo   信任库: %CACERTS%
echo.
echo 请重启应用程序使证书生效
echo.

:: 显示导入的证书
"%KEYTOOL%" -list -alias %CERT_ALIAS% -keystore "%CACERTS%" -storepass %KEYSTORE_PASS%

echo.
pause
