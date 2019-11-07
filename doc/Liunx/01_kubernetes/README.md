# 1. 前言
<!-- TOC -->

- [1. 前言](#1-前言)
- [2. 环境要求](#2-环境要求)
    - [2.1. 操作系统描述](#21-操作系统描述)
    - [2.2. 关闭防火墙](#22-关闭防火墙)
    - [2.3. 配置阿里源](#23-配置阿里源)
- [安装Docker](#安装docker)
- [安装kubernetes](#安装kubernetes)

<!-- /TOC -->
大家在学习k8s过程中，都需要对k8s环境搭建。网上找了很多BLOG，期间的痛点，大家也都深有感触，如何在不翻墙的情况下，提供一套k8s集群搭建的方案及脚本。

# 2. 环境要求

## 2.1. 操作系统描述

Centos7系统

## 2.2. 关闭防火墙

## 2.3. 配置阿里源

~~~

cat >> /etc/yum.repos.d/kubernetes.repo << EOF
[kubernetes]
name=Kubernetes
baseurl=https://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64/
enabled=1
gpgcheck=0
~~~

![20191105092154.png](https://i.loli.net/2019/11/05/SgCYzPbLrpOvio9.png)

# 安装Docker

- 安装必要的一些系统工具

~~~
yum install -y yum-utils device-mapper-persistent-data lvm2
~~~

- 添加软件源信息

~~~
 yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
~~~

- 更新并安装 Docker-CE

~~~

~~~

# 安装kubernetes

~~~
yum install -y docker kubelet-1.11.0-0 kubeadm-1.11.0-0 kubectl-1.11.0-0  kubernetes-cni
~~~

![20191105092654.png](https://i.loli.net/2019/11/05/WTxqwORLNKusIdp.png)

在运行kubeadm init 之前可以先执行 kubeadm config images pull 来测试与 gcr.io 的连接，kubeadm config images pull尝试是否可以拉取镜像，由于国内访问"k8s.gcr.io", "gcr.io", "quay.io" 有困难，这里采用自建docker register的方式.

~~~
[root@localhost app]# kubeadm config images pull
W1105 15:15:13.509767    2974 version.go:101] could not fetch a Kubernetes version from the internet: unable to get URL "https://dl.k8s.io/release/stable-1.txt": Get https://dl.k8s.io/release/stable-1.txt: net/http: request canceled while waiting for connection (Client.Timeout exceeded while awaiting headers)
W1105 15:15:13.509894    2974 version.go:102] falling back to the local client version: v1.16.2
failed to pull image "k8s.gcr.io/kube-apiserver:v1.16.2": output: Error response from daemon: Get https://k8s.gcr.io/v2/: net/http: request canceled while waiting for connection (Client.Timeout exceeded while awaiting headers)
, error: exit status 1

~~~

所以只能通过私有仓库拉取k8s.gcr.io等镜像

构建私有镜像：

~~~
docker pull registry
~~~

关闭系统的Swap方法如下:

  swapoff -a
修改 /etc/fstab 文件，注释掉 SWAP 的自动挂载，使用free -m确认swap已经关闭。 swappiness参数调整，修改/etc/sysctl.d/k8s.conf添加下面一行：

  vm.swappiness=0
执行sysctl -p /etc/sysctl.d/k8s.conf使修改生效。

~~~~

docker pull mirrorgooglecontainers/kube-apiserver:v1.15.0
docker pull mirrorgooglecontainers/kube-controller-manager:v1.15.0
docker pull mirrorgooglecontainers/kube-scheduler:v1.15.0
docker pull mirrorgooglecontainers/kube-proxy:v1.15.0
docker pull mirrorgooglecontainers/pause:3.1
docker pull mirrorgooglecontainers/etcd:3.3.10
docker pull coredns/coredns:1.3.1

~~~

docker pull mirrorgooglecontainers/kube-apiserver:v1.16.2
docker pull mirrorgooglecontainers/kube-controller-manager:v1.16.2
docker pull mirrorgooglecontainers/kube-scheduler:v1.16.2
docker pull mirrorgooglecontainers/kube-proxy:v1.16.2
docker pull mirrorgooglecontainers/etcd:3.3.15-0
docker pull mirrorgooglecontainers/coredns:1.6.2


~~~
docker tag mirrorgooglecontainers/kube-apiserver:v1.15.0   k8s.gcr.io/kube-apiserver:v1.15.0
docker tag mirrorgooglecontainers/kube-controller-manager:v1.15.0   k8s.gcr.io/kube-controller-manager:v1.15.0
docker tag mirrorgooglecontainers/kube-scheduler:v1.15.0   k8s.gcr.io/kube-scheduler:v1.15.0
docker tag mirrorgooglecontainers/kube-proxy:v1.15.0   k8s.gcr.io/kube-proxy:v1.15.0
docker tag mirrorgooglecontainers/pause:3.1   k8s.gcr.io/pause:3.1
docker tag mirrorgooglecontainers/etcd:3.3.10   k8s.gcr.io/etcd:3.3.10
docker tag coredns/coredns:1.3.1   k8s.gcr.io/coredns:1.3.1
~~~