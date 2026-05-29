#!/bin/bash
# Wimoor 服务启动脚本

export JAVA_HOME=/opt/homebrew/Cellar/openjdk@17/17.0.19/libexec/openjdk.jdk/Contents/Home
export JAVA_OPTS="--add-opens=java.base/java.lang=ALL-UNNAMED \
  --add-opens=java.base/java.util=ALL-UNNAMED \
  --add-opens=java.base/java.lang.reflect=ALL-UNNAMED \
  -Xms256m -Xmx512m"

export NACOS_SERVER_ADDR=127.0.0.1:8848
export NACOS_NAMESPACE=public
export NACOS_IP=127.0.0.1

LOG_DIR=/tmp/wimoor-logs
mkdir -p $LOG_DIR

start_service() {
    local name=$1
    local jar=$2
    local port=$3
    
    echo "Starting $name on port $port..."
    $JAVA_HOME/bin/java $JAVA_OPTS \
        -DNACOS_SERVER_ADDR=$NACOS_SERVER_ADDR \
        -DNACOS_NAMESPACE=$NACOS_NAMESPACE \
        -DNACOS_IP=$NACOS_IP \
        -Dspring.profiles.active=dev \
        -jar "$jar" \
        > $LOG_DIR/$name.log 2>&1 &
    echo "$!" > $LOG_DIR/$name.pid
    echo "  $name started (PID: $!), log: $LOG_DIR/$name.log"
}

case "$1" in
    gateway)
        start_service "gateway" "/Users/liuxingwang/go/src/wimoor/wimoor-gateway/target/wimoor-gateway.jar" 8099
        ;;
    admin)
        start_service "admin" "/Users/liuxingwang/go/src/wimoor/wimoor-admin/admin-boot/target/admin-boot.jar" 8100
        ;;
    all)
        start_service "gateway" "/Users/liuxingwang/go/src/wimoor/wimoor-gateway/target/wimoor-gateway.jar" 8099
        sleep 5
        start_service "admin" "/Users/liuxingwang/go/src/wimoor/wimoor-admin/admin-boot/target/admin-boot.jar" 8100
        ;;
    stop)
        for pid_file in $LOG_DIR/*.pid; do
            if [ -f "$pid_file" ]; then
                pid=$(cat "$pid_file")
                name=$(basename "$pid_file" .pid)
                echo "Stopping $name (PID: $pid)..."
                kill $pid 2>/dev/null
                rm "$pid_file"
            fi
        done
        echo "All services stopped"
        ;;
    status)
        for pid_file in $LOG_DIR/*.pid; do
            if [ -f "$pid_file" ]; then
                pid=$(cat "$pid_file")
                name=$(basename "$pid_file" .pid)
                if kill -0 $pid 2>/dev/null; then
                    echo "$name: Running (PID: $pid)"
                else
                    echo "$name: Stopped"
                fi
            fi
        done
        ;;
    *)
        echo "Usage: $0 {gateway|admin|all|stop|status}"
        exit 1
        ;;
esac
