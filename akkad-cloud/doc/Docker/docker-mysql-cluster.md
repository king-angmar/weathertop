
~~~

[root@localhost ~]# docker search percona-xtradb
NAME                                          DESCRIPTION                                     STARS               OFFICIAL            AUTOMATED
percona/percona-xtradb-cluster                Percona XtraDB Cluster docker image | https:…   97                                      

[root@localhost ~]# docker pull percona/percona-xtradb-cluster
Using default tag: latest
latest: Pulling from percona/percona-xtradb-cluster
d8d02d457314: Pull complete 
438d35a56c8b: Pull complete 
f1f1bf9a6403: Pull complete 
3f1d05f72c7e: Pull complete 
9ab505692dcc: Pull complete 
4c31ad540342: Pull complete 
e933f27fa417: Pull complete 
2b6312e57b5e: Pull complete 
8e51b638f213: Pull complete 
d86d0bad2c77: Pull complete 
3430a371893a: Pull complete 
Digest: sha256:7d8eb4d2031c32c6e96451655f359d8e5e8e047dc95bada9a28c41c158876c26
Status: Downloaded newer image for percona/percona-xtradb-cluster:latest
docker.io/percona/percona-xtradb-cluster:latest

[root@localhost ~]# docker tag percona/percona-xtradb-cluster pxc
[root@localhost ~]# docker images
REPOSITORY                       TAG                 IMAGE ID            CREATED             SIZE
portainer/portainer              latest              d1219c88aa21        2 weeks ago         80.8MB
mysql                            latest              c8ee894bd2bd        5 weeks ago         456MB
pxc                              latest              aef0b3032083        2 months ago        827MB
percona/percona-xtradb-cluster   latest              aef0b3032083        2 months ago        827MB

//创建一个其他的网段(docker默认使用172.17.0.1网段)，所以会有报错
[root@localhost ~]# docker network create --subnet=172.17.0.0/16 net1
Error response from daemon: Pool overlaps with other one on this address space
[root@localhost ~]# docker network ls
NETWORK ID          NAME                DRIVER              SCOPE
ac8dc88bd3e3        bridge              bridge              local
919042d89d4d        host                host                local
aa0b33fe32f2        none                null                local
[root@localhost ~]# docker network create --subnet=172.18.0.0/16 net1
78415fbe0891f34618955ed91f269ce854c5c4cb20547dfe6711a6820c20bbbb
[root@localhost ~]# docker network ls
NETWORK ID          NAME                DRIVER              SCOPE
ac8dc88bd3e3        bridge              bridge              local
919042d89d4d        host                host                local
78415fbe0891        net1                bridge              local
aa0b33fe32f2        none                null                local
[root@localhost ~]# docker volume create v1
v1
[root@localhost ~]# docker volume create v2
v2
[root@localhost ~]# docker volume create v3
v3
[root@localhost ~]# docker volume create v4
v4
[root@localhost ~]# docker volume create v5
v5
[root@localhost ~]# docker inspect v1
[
    {
        "CreatedAt": "2019-11-22T18:47:54+08:00",
        "Driver": "local",
        "Labels": {},
        "Mountpoint": "/var/lib/docker/volumes/v1/_data",
        "Name": "v1",
        "Options": {},
        "Scope": "local"
    }
]

//  安装Haproxy进行高可用与负载均衡
[root@localhost ~]# docker pull haproxy
Using default tag: latest
latest: Pulling from library/haproxy
8d691f585fa8: Pull complete 
f55ccc95b726: Pull complete 
774183a58600: Pull complete 
Digest: sha256:2eb6f97387fd3b7726481c16986aaf4cd48db61bd85a3aa4af83b3f6b5ee773d
Status: Downloaded newer image for haproxy:latest
docker.io/library/haproxy:latest
[root@localhost ~]# 


//创建5节点的PXC集群
//  创建第1个MySQL节点

[root@localhost ~]# docker run -d -p 13306:3306 -e MYSQL_ROOT_PASSWORD=abc123456 -e CLUSTER_NAME=PXC -e XTRABACKUP_PASSWORD=abc123456 -v v1:/var/lib/mysql -v backup:/data --privileged --name=node1 --net=net1 --ip 172.18.0.2 pxc
5bf72db2df020088211520e8877e51beb3a668f69dbbde2c35fc090f2a3dd838


// 之前看到的材料说，每次创建MYSQL节点最好间隔至少2分钟，否则立马开启第二个节点实例，容器创建中的节点会瞬间停止，我并未去验证这个，大家在处理过程中知晓这个即可。

#创建第2个MySQL节点
docker run -d -p 13307:3306 -e MYSQL_ROOT_PASSWORD=abc123456 -e CLUSTER_NAME=PXC -e XTRABACKUP_PASSWORD=abc123456 -e CLUSTER_JOIN=node1 -v v2:/var/lib/mysql -v backup:/data --privileged --name=node2 --net=net1 --ip 172.18.0.3 pxc


~~~