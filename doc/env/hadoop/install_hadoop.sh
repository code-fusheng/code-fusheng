#!bin/bash
echo "#hadoop 环境变量" >> /etc/profile
echo "export HADOOP_HOME=/usr/local/hadoop/hadoop-3.2.3" >> /etc/profile
echo "export PATH=\$PATH:\$HADOOP_HOME/bin:\$HADOOP_HOME/sbin" >> /etc/profile
echo "#启动用户权限问题" >> /etc/profile
echo "export HDFS_NAMENODE_USER=root" >> /etc/profile
echo "export HDFS_DATANODE_USER=root" >> /etc/profile
echo "export HDFS_SECONDARYNAMENODE_USER=root" >> /etc/profile
echo "export YARN_RESOURCEMANAGER_USER=root" >> /etc/profile
echo "export YARN_NODEMANAGER_USER=root" >> /etc/profile
source /etc/profile

### 创建 hadoop 目录并授权
echo "创建 hadoop 临时文件目录"
mkdir -p /home/hadoop/tmp
echo "创建 hadoop 主节点(namenode)文件目录"
mkdir -p /home/hadoop/hdfs/name
echo "创建 hadoop 数据节点(datanode)文件目录"
mkdir -p /home/hadoop/hdfs/data
echo "创建 hadoop 日志文件目录"
mkdir -p /home/hadoop/log
echo "设置 hadoop 文件目录所有者"
sudo chown -R root /home/hadoop

### 修改配置文件
### 1. 修改 hadoop-env.sh (jdk路径)
export JAVA_HOME\=/usr/local/java/jdk1.8.0_221
### 2. 修改 core-site.xml (hdfs 端口)
<configuration>
    <property>
        <name>fs.defaultFS</name>
        <value>hdfs://localhost:9001</value>
        <description>hdfs内部通讯访问地址</description>
    </property>
    <property>
        <name>hadoop.tmp.dir</name>
        <value>file:/home/hadoop/tmp</value>
        <description>hadoop数据存放</description>
    </property>
</configuration>
### 3. 修改 hdfs-site.xml
<configuration>
    <property>
        <name>dfs.namenode.name.dir</name>
        <value>file:/home/hadoop/hdfs/name</value>
    </property>
    <property>
        <name>dfs.datanode.data.dir</name>
        <value>file:/home/hadoop/hdfs/data</value>
    </property>
    <property>
        <name>dfs.replication</name>
        <value>1</value>
    </property>
</configuration>

### 格式化 hadoop 文件系统
./bin/hdfs namenode -format
