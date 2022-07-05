#!bin/bash
# 使用本脚本，请务必提前使用 scp 将jdk资源上传至服务器，并与本脚本文件在同一路径下！！！
currentPath=`pwd`
echo "当前路径:$currentPath"
# JDK目标安装路径
javaTargetPath="/usr/local/java"

# 检查是否存在目录，如果不存在就创建
if [ ! -d "$javaTargetPath" ]
then
	echo "安装路径不全,开始创建:$javaTargetPath"
	mkdir $javaTargetPath
	if [ -d "$javaTargetPath" ]
	then
		echo "安装路径创建成功"
	fi
fi

# 解压
if [ -d "jdk-8u221-linux-x64.tar.gz" ]
then
	echo "未找到资源 jdk-8u221-linux-x64.tar.gz"
	exit
else
  tar -zxvf jdk-8u221-linux-x64.tar.gz -C /usr/local/java
  if [ -d "/usr/local/java/jdk1.8.0_221" ]
  then
  	echo "解压成功"
  fi
fi

# 环境配置
echo "#java jdk环境变量" >> /etc/profile
echo "export JAVA_HOME=/usr/local/java/jdk1.8.0_221" >> /etc/profile
echo "export PATH=\$JAVA_HOME/bin:\$PATH" >> /etc/profile
echo "export CLASSPATH=.:\$JAVA_HOME/lib/dt.jar:\$JAVA_HOME/lib/tools.jar" >> /etc/profile
echo "刷新环境变量"
source /etc/profile
java -version
echo "jdk 安装完成"
