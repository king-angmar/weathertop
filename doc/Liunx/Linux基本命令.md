## 7.1. 总体说明

### 7.1.1. 查看版本当前操作系统内核信息

~~~
[root@localhost ~]# uname -a
Linux localhost.localdomain 3.10.0-1062.1.1.el7.x86_64 #1 SMP Fri Sep 13 22:55:44 UTC 2019 x86_64 x86_64 x86_64 GNU/Linux
~~~

### 7.1.2. 查看当前操作系统版本信息

~~~
[root@localhost ~]# cat /proc/version
Linux version 3.10.0-1062.1.1.el7.x86_64 (mockbuild@kbuilder.bsys.centos.org) (gcc version 4.8.5 20150623 (Red Hat 4.8.5-39) (GCC) ) #1 SMP Fri Sep 13 22:55:44 UTC 2019
~~~

### 7.1.3. 查看版本当前操作系统发行版信息

~~~
[root@localhost ~]# cat /etc/redhat-release
CentOS Linux release 7.7.1908 (Core)
~~~

### 7.1.4. 查看cpu相关信息，包括型号、主频、内核信息等

~~~

[root@localhost ~]# cat /proc/cpuinfo
processor	: 0
vendor_id	: GenuineIntel
cpu family	: 6
model		: 94
model name	: Intel(R) Core(TM) i5-6300HQ CPU @ 2.30GHz
stepping	: 3
microcode	: 0xc6
cpu MHz		: 2303.999
cache size	: 6144 KB
physical id	: 0
siblings	: 1
core id		: 0
cpu cores	: 1
apicid		: 0
initial apicid	: 0
fpu		: yes
fpu_exception	: yes
cpuid level	: 22
wp		: yes
flags		: fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush mmx fxsr sse sse2 ss syscall nx pdpe1gb rdtscp lm constant_tsc arch_perfmon nopl xtopology tsc_reliable nonstop_tsc eagerfpu pni pclmulqdq ssse3 fma cx16 pcid sse4_1 sse4_2 x2apic movbe popcnt tsc_deadline_timer xsave avx f16c rdrand hypervisor lahf_lm abm 3dnowprefetch invpcid_single ssbd ibrs ibpb stibp fsgsbase tsc_adjust bmi1 hle avx2 smep bmi2 invpcid rtm rdseed adx smap xsaveopt arat spec_ctrl intel_stibp flush_l1d arch_capabilities
bogomips	: 4607.99
clflush size	: 64
cache_alignment	: 64
address sizes	: 42 bits physical, 48 bits virtual
power management:

processor	: 1
vendor_id	: GenuineIntel
cpu family	: 6
model		: 94
model name	: Intel(R) Core(TM) i5-6300HQ CPU @ 2.30GHz
stepping	: 3
microcode	: 0xc6
cpu MHz		: 2303.999
cache size	: 6144 KB
physical id	: 2
siblings	: 1
core id		: 0
cpu cores	: 1
apicid		: 2
initial apicid	: 2
fpu		: yes
fpu_exception	: yes
cpuid level	: 22
wp		: yes
flags		: fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush mmx fxsr sse sse2 ss syscall nx pdpe1gb rdtscp lm constant_tsc arch_perfmon nopl xtopology tsc_reliable nonstop_tsc eagerfpu pni pclmulqdq ssse3 fma cx16 pcid sse4_1 sse4_2 x2apic movbe popcnt tsc_deadline_timer xsave avx f16c rdrand hypervisor lahf_lm abm 3dnowprefetch invpcid_single ssbd ibrs ibpb stibp fsgsbase tsc_adjust bmi1 hle avx2 smep bmi2 invpcid rtm rdseed adx smap xsaveopt arat spec_ctrl intel_stibp flush_l1d arch_capabilities
bogomips	: 4607.99
clflush size	: 64
cache_alignment	: 64
address sizes	: 42 bits physical, 48 bits virtual
power management:

~~~

## 7.2. 系统配置

### 7.2.1. 内存
~~~
[root@localhost ~]# free -ml
              total        used        free      shared  buff/cache   available
Mem:           3770         124        3547          11          98        3473
Low:           3770         223        3547
High:             0           0           0
Swap:          2047           0        2047

~~~

### 7.2.2. 磁盘

~~~

[root@localhost ~]# df -h
文件系统                 容量  已用  可用 已用% 挂载点
devtmpfs                 1.9G     0  1.9G    0% /dev
tmpfs                    1.9G     0  1.9G    0% /dev/shm
tmpfs                    1.9G   12M  1.9G    1% /run
tmpfs                    1.9G     0  1.9G    0% /sys/fs/cgroup
/dev/mapper/centos-root   18G  8.9G  8.6G   51% /
/dev/sda1                497M  165M  332M   34% /boot
tmpfs                    378M     0  378M    0% /run/user/0

~~~

## 7.3. Linux优化

### 7.3.1. 运行级别Runlevel

- 查看运行级别的方法依旧使用
~~~
[root@localhost ~]# runlevel
N 3


[root@localhost ~]# ls -lh /usr/lib/systemd/system/runlevel*.target
lrwxrwxrwx. 1 root root 15 9月  30 18:00 /usr/lib/systemd/system/runlevel0.target -> poweroff.target
lrwxrwxrwx. 1 root root 13 9月  30 18:00 /usr/lib/systemd/system/runlevel1.target -> rescue.target
lrwxrwxrwx. 1 root root 17 9月  30 18:00 /usr/lib/systemd/system/runlevel2.target -> multi-user.target
lrwxrwxrwx. 1 root root 17 9月  30 18:00 /usr/lib/systemd/system/runlevel3.target -> multi-user.target
lrwxrwxrwx. 1 root root 17 9月  30 18:00 /usr/lib/systemd/system/runlevel4.target -> multi-user.target
lrwxrwxrwx. 1 root root 16 9月  30 18:00 /usr/lib/systemd/system/runlevel5.target -> graphical.target
lrwxrwxrwx. 1 root root 13 9月  30 18:00 /usr/lib/systemd/system/runlevel6.target -> reboot.target

~~~

- 设置运行级别

~~~
[root@localhost ~]# systemctl get-default multi-user.target
Invalid number of arguments.

~~~

- 查看运行级别

~~~
[root@localhost ~]# systemctl get-default
multi-user.target
~~~

### 7.3.2. systemd一统天下

查看系统启动文件的目录

~~~
[root@localhost ~]# ls /usr/lib/systemd/system
auditd.service                          initrd.target.wants                 rescue.target                                  systemd-halt.service
autovt@.service                         initrd-udevadm-cleanup-db.service   rescue.target.wants                            systemd-hibernate-resume@.service
basic.target                            iprdump.service                     rhel-autorelabel-mark.service                  systemd-hibernate.service
basic.target.wants                      iprinit.service                     rhel-autorelabel.service                       systemd-hostnamed.service
blk-availability.service                iprupdate.service 
~~~

## 7.4. 开机时间优化

### 7.4.1. 查看开机时间

~~~
[root@localhost ~]# systemd-analyze time
Startup finished in 1.375s (kernel) + 1.928s (initrd) + 3.233s (userspace) = 6.537s

~~~

### 7.4.2. 查看具体的使用时间

~~~
[root@localhost ~]# systemd-analyze blame
          1.439s dev-mapper-centos\x2droot.device
           653ms boot.mount
           604ms lvm2-pvscan@8:2.service
           391ms tuned.service
           362ms systemd-tmpfiles-clean.service
           360ms network.service
           236ms NetworkManager-wait-online.service
           119ms systemd-udev-trigger.service
           108ms systemd-vconsole-setup.service
~~~

### 7.4.3. 启动项优化

~~~

[root@localhost ~]# systemctl list-unit-files|egrep "^ab|^aud|^kdump|vm|^md|^mic|^post|lvm"  |awk '{print $1}'|sed -r 's#(.*)#systemctl disable &#g'|bash
Removed symlink /etc/systemd/system/multi-user.target.wants/auditd.service.
Removed symlink /etc/systemd/system/multi-user.target.wants/kdump.service.
Removed symlink /etc/systemd/system/sysinit.target.wants/lvm2-monitor.service.
Failed to execute operation: Unit name lvm2-pvscan@.service is missing the instance name.
Removed symlink /etc/systemd/system/basic.target.wants/microcode.service.
Removed symlink /etc/systemd/system/multi-user.target.wants/postfix.service.
Removed symlink /etc/systemd/system/sysinit.target.wants/lvm2-lvmetad.socket.
Removed symlink /etc/systemd/system/sysinit.target.wants/lvm2-lvmpolld.socket.

~~~

### 7.4.4. Selinux的优化

~~~
[root@localhost ~]# sed -i 's#SELINUX=enforcing#SELINUX=disabled#g' /etc/selinux/config
~~~

### 7.4.5. ssh的优化

/etc/ssh/sshd_config文件中修改<font color=red>GSSAPIAuthentication no、UseDNS no</font>，然后重启sshd服务即可。