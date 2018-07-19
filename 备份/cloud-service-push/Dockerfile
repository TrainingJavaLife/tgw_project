# 基于哪个镜像
FROM java:8
VOLUME /root/platform/cloud-service-push
# 开放8761端⼝
#EXPOSE 8762
# 配置容器启动后执⾏的命令
ENTRYPOINT ["java","-server","-Xmx500m","-Xms300m","-jar","/root/platform/cloud-service-push/app.jar","--logging.config=/root/platform/cloud-service-push/config/logback-spring.xml","--spring.config.location=/root/platform/cloud-service-push/config/bootstrap.yml"]

