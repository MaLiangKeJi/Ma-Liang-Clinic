# 基础镜像
FROM openjdk:8-jdk-alpine
WORKDIR /app

RUN apk update
RUN apk add --no-cache vim curl wget bash

RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.ustc.edu.cn/g' /etc/apk/repositories && apk update && apk add busybox-extras

# 设置时区
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' >/etc/timezone
ADD auth/target/classes/wx /app/wx
# 拷贝jar 把可执行jar包复制到基础镜像的根目录下
ADD auth/target/auth-1.0.jar app.jar
# 设置暴露的端口号
EXPOSE 8080
# 在镜像运行为容器后执行的命令
ENTRYPOINT ["java","-jar","app.jar","-XX:+UnlockExperimentalVMOptions","XX:+UseCGroupMemoryLimitForHeap", " --spring.config.location=/app/bootstrap.properties"]