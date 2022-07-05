#!bin/bash
currentPath=`pwd`
echo "当前路径:$currentPath"
# 安装目录
zkTargetPath="/usr/local/zookeeper"
# 检查是否存在目录，如果不存在就创建
if [ ! -d "$zkTargetPath" ]
then
	echo "安装路径不全,开始创建:$zkTargetPath"
	mkdir $zkTargetPath
	if [ -d "$zkTargetPath" ]
	then
		echo "安装路径创建成功"
	fi
fi
# 解压
tar -zxvf zookeeper-3.7.0-bin.tar.gz
mv apache-zookeeper-3.7.0-bin zookeeper
mv zookeeper /usr/local/
# 复制 zoo_sample.cfg  => zoo.cfg
cd /usr/local/zookeeper/conf
cp zoo_sample.cfg zoo.cfg
cd /usr/local/zookeeper
sh ./bin/zkServer.sh start
sh ./bin/zkServer.sh status

# 环境配置
echo "#zk 环境变量" >> /etc/profile
echo "export ZK_HOME=/usr/local/zookeeper" >> /etc/profile
echo "export PATH=\$ZK_HOME/bin:\$PATH" >> /etc/profile
echo "刷新环境变量"
source /etc/profile
echo "zk 安装完成"
