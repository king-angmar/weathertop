![20191122160531.png](https://i.loli.net/2019/11/22/Bp7P48NhZT5FA63.png)

**Portainer** 是Docker的轻量级，跨平台和开源管理UI。Portainer提供了Docker的详细概述，并允许您通过基于Web的简单仪表板管理容器，图像，网络和卷。它最初是Docker UI的分支。 但是，开发人员现在已经重写了几乎所有的Docker UI原始代码。 他还彻底修改了UX，并在最新版本中添加了更多功能。 截至目前，它已经引起了用户的极大关注，并且现在已经有超过一百万的下载量！ 它将支持GNU / Linux，Microsoft Windows和Mac OS X。

# 1. 安装Portainer

Portainer的安装非常简单，将花费不到一分钟的时间。 Portainer完全支持Docker 1.10及更高版本。要安装Docker，请参考以下链接。
- [如何在CentOS上安装](https://www.ostechnix.com/install-docker-centos/)
- [如何在Ubuntu上安装](https://www.ostechnix.com/install-docker-ubuntu/)

- 查看镜像

安装Docker之后，运行以下命令以获取最新的Portainer映像。·
~~~
[root@localhost ~]# docker search portainer
~~~

![搜索结果列表](https://i.loli.net/2019/11/22/Wi12INpc35HODma.png)

- 拉取镜像

~~~
[root@localhost ~]# docker pull portainer/portainer
Using default tag: latest
latest: Pulling from portainer/portainer
d1e017099d17: Pull complete 
292b789be2e4: Pull complete 
Digest: sha256:63eb47d4b408c3f39e942368bcbf7e157a2b6e5dda94ffd403a14199e1137133
Status: Downloaded newer image for portainer/portainer:latest
docker.io/portainer/portainer:latest

~~~

- 查看镜像

~~~
[root@localhost ~]# docker images
REPOSITORY                                       TAG                 IMAGE ID            CREATED             SIZE
portainer/portainer                              latest              d1219c88aa21        2 weeks ago         80.8MB
~~~

从上面的输出中可以看到，Portainer的镜像约80 MB，这样我就不会占用更多的RAM和Hdd空间。

# 2. 启动

~~~

[root@localhost ~]# docker run -d -p 9000:9000 --restart=always -v /var/run/docker.sock:/var/run/docker.sock --name prtainer-test portainer/portainer
~~~

该语句用宿主机9000端口关联容器中的9000端口，并给容器起名为portainer-test。执行完该命令之后，使用该机器IP:PORT即可访问Portainer。

![启动结果](https://i.loli.net/2019/11/22/DgpzNHAUYR3PeFK.png)

# 3. 访问

http://IP:9000

首次登陆需要注册用户，给管理员用户admin设置密码。

![第一次登陆](https://i.loli.net/2019/11/22/kdo9utVTb4Z1n8X.png)

单机版这里选择local即可，选择完毕，点击Connect即可连接到本地docker

![选择本地](https://i.loli.net/2019/11/22/kcRgb2LWPYsqwVB.png)

注意：该页面上有提示需要挂载本地 /var/run/docker.socker与容器内的/var/run/docker.socker连接。因此，在启动时必须指定该挂载文件。

![查看容器](https://i.loli.net/2019/11/22/hcbmHL38xBIeqPj.png)

# 4. 创建容器

单击左侧的“App Templates”按钮。这将显示一些现成的可用模板，例如Docker映像注册表，Nginx，Httpd，MySQl，WordPress等。

![20191122162651.png](https://i.loli.net/2019/11/22/3JTCVe2c4ljsyQt.png)

选择要部署的容器，只需单击相应的模板。

这里让我们启动MySQL容器。为此，请单击MySQL模板。输入容器名称，选择网络类型（例如新娘模式）。单击“Show advanced options”并设置端口号。

![创建mysql容器](https://i.loli.net/2019/11/22/6lNoiR5XZwsjT3Y.png)

点击“Deploy the container”，过程会有点长，耐心等待。

![容器](https://i.loli.net/2019/11/22/Ms8BKJW4GU7hygl.png)

选择容器，点击进入。

![容器管理TAB](https://i.loli.net/2019/11/22/IfVYPdMO5SchwpA.png)

可以这里重新启动，停止，暂停和删除此部分中的容器。

点击“Stats”统计信息按钮以查看新启动的容器中发生的情况.

![查看统计信息](https://i.loli.net/2019/11/22/YnS2pgJhCO13fkF.png)

这是我的数据库容器的统计信息

![详细统计](https://i.loli.net/2019/11/22/6aVx2trPp7XLy8N.png)

同样，单击“Logs”按钮以查看容器的完整日志详细信息

![日志](https://i.loli.net/2019/11/22/qenWawgKATZmDJr.png)

![进入控制台](https://i.loli.net/2019/11/22/JeWoRhaUC7YPiNb.png)

选择命令行管理程序（BASH或SH），然后单击“Connect”按钮

![控制台命令行](https://i.loli.net/2019/11/22/griXyocDtaRYmKv.png)

# 5. Docker镜像

您可以查看已下载的Docker映像的列表。

![已下载的镜像](https://i.loli.net/2019/11/22/4MLr2p6EYX8dJKk.png)

要删除任何图像，只需选择它，然后单击“Remove”，所选图像将消失。

# 6. 网络（Networks）

网络部分允许您添加新网络，更改网络类型，分配/更改IP地址，删除现有网络。

![网络状况](https://i.loli.net/2019/11/22/tSvkmsJRz8MQILX.png)

# 7. 卷簇（Volumes）

在这里您可以查看现有的docker卷，创建新的docker卷，如果不再需要它们则将其删除。

![20191122170358.png](https://i.loli.net/2019/11/22/sHhOpcjgwS34QdK.png)

# 8. 事件

在这里，我们可以查看到目前为止我们正在做的事情，例如创建新实例，网络，卷等。

![20191122170453.png](https://i.loli.net/2019/11/22/ubLNyzhP19kA8Zt.png)