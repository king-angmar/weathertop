
<!-- TOC -->

- [Nacos](#nacos)
    - [简介](#简介)
    - [安装](#安装)
    - [SpringBoot集成Nacos](#springboot集成nacos)
        - [配置中心集成](#配置中心集成)
        - [注册中心集成](#注册中心集成)
- [1. 基于Nacos配置中心](#1-基于nacos配置中心)
- [2. 基于Nacos注册中心](#2-基于nacos注册中心)
- [3. 网关Gateway](#3-网关gateway)
- [4. 熔断Hystream](#4-熔断hystream)

<!-- /TOC -->

# Nacos

## 简介

## 安装

## SpringBoot集成Nacos

### 配置中心集成

### 注册中心集成



# 1. 基于Nacos配置中心

Nacos 支持基于 DNS 和基于 RPC 的服务发现（可以作为springcloud的注册中心）、动态配置服务（可以做配置中心）、动态 DNS 服务。

`官方介绍`

~~~
Nacos 致力于帮助您发现、配置和管理微服务。Nacos 提供了一组简单易用的特性集，帮助您实现动态服务发现、服务配置管理、服务及流量管理。
Nacos 帮助您更敏捷和容易地构建、交付和管理微服务平台。 Nacos 是构建以“服务”为中心的现代应用架构(例如微服务范式、云原生范式)的服务基础设施。
~~~

- pom依赖包

~~~
<dependency>
    <groupId>com.alibaba.boot</groupId>
    <artifactId>nacos-config-spring-boot-starter</artifactId>
    <version>0.1.2</version>
</dependency>
~~~

- application.yml

~~~
nacos:
  config:
    server-addr: 192.168.147.132:8848
~~~

# 2. 基于Nacos注册中心

# 3. 网关Gateway

# 4. 熔断Hystream
