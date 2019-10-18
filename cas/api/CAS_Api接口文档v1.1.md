# Api层接口说明文档

- 更新时间：2019/09/20

[TOC]

# 简介

欢迎使用CAS统一认证平台。

本文档主要描述CAS统一认证平台的Api层相关技术内容。

## 请求格式

- **请求格式为http://{ip}:{port}/{path}**

| 参数 | 说明                  |
| ---- | --------------------- |
| ip   | Api程序运行的服务器IP |
| port | Api程序运行的端口     |
| path | Api调用路径           |

## 请求说明

- **除了验证获取client_token接口以外，请求所有接口时都需要在Header里添加authentication，该值为client_token，如果没有，则返回认证失败错误401 **

## 文档阅读说明

ip是指服务器地址，port是指端口号。

在文档中，ip是127.0.0.1，port是8050。

# clients基本参数表

| 字段         | 类型    | 说明                                                        |
| ------------ | ------- | ----------------------------------------------------------- |
| client_id    | String  | 应用客户端id                                                |
| name         | String  | 系统名称                                                    |
| secret       | String  | 密钥                                                        |
| token        | String  | 令牌，用于验证client是否合法，内部系统颁发的token属于永久性 |
| isInternal   | Boolean | 是否为内部系统 client                                       |
| created_time | String  | 创建时间                                                    |

# id_tokens基本参数表


| 字段         | 类型    | 说明                  |
| ------------ | ------- | --------------------- |
| value    | String  | id_token的值          |
| client_id         | String  | 应用客户端id              |
| account_id       | String  | 用户id                  |
| expired_time        | String  | 过期时间                 |
| refresh_token   | String | 由统一认证中心颁发的刷新令牌，更新id_token(value) |
| refresh_expired_time | String  | 更新token的过期时间，默认是6小时    |


# 1.颁发id_token接口

## 接口描述

用户请求进行id_token颁发。需要结合Account的登录接口进行账户验证。如果用户名，密码正确但是client_id没有对应的id_token(value)，则增加一条id_token数据，默认id_token有效时间为1小时，refresh_token为6小时。

## 请求说明

- HTTP方法:**POST**
- Path：``/auth/id_token``
- URL示例：``http://127.0.0.1:8050/auth/id_token``
- 请求参数说明

| 参数        | 类型   | 是否必选 | 说明                                 |
| ----------- | ------ | -------- | ------------------------------------ |
| accountname | String | 是       | 账户名/手机号/邮箱均可，但是不能为空 |
| password    | String | 是       | 用户密码，必填                       |

## 返回说明

### 返回参数说明

| 参数          | 类型   | 说明                                                         |
| ------------- | ------ | ------------------------------------------------------------ |
| status        | Number | 状态码，默认返回200为正常                                    |
| code          | Number | 返回代码                                                     |
| message       | String | 返回说明                                                     |
| id_token      | String | 颁发token成功返回                                            |
| refresh_token | String | 颁发token成功返回，如果是新增，默认首次生成refresh_token和id_token相同 |
| addId_Token   | Object | 登录用户账户名，密码正确，但是由token查询到的id_Token不存在返回，返回创建的id_Token |

### 返回代码说明

| 返回代码 | 说明                                                         |
| -------- | ------------------------------------------------------------ |
| 100001   | 返回成功，正常返回id_token和refresh_token                    |
| 100002   | 登录用户账户名，密码正确，但是由token查询到的id_Token不存在，创建新的id_Token |
| 100003   | 由传入的token查询到的client的token不存在                     |
| 100004   | 由传入的token查询到的client的token存在，但是已超过有效期（默认为创建后365天之内有效） |
| 100005   | 登录的账户名/手机号/邮箱存在，但是密码不正确                 |
| 100006   | 账户名/手机号/邮箱其中之一没有填或密码未填，或者手机号，邮箱格式不符合 |
| 100007   | id_Token已过期，有效期在expired_time之前                     |

# 2.验证id_token接口

## 接口描述

验证id_token是否存在

## 请求说明

- HTTP方法:**GET**

- Path：``auth/id_token/{value}``

  | 参数  | 类型   | 是否必选 | 说明         |
  | ----- | ------ | -------- | ------------ |
  | value | String | 是       | id_token的值 |

- URL示例：``http://localhost:8050/auth/id_token/10001``

## 返回说明

- 返回状态说明：

| status | code   | message          | 备注     |
| ------ | ------ | ---------------- | -------- |
| 200    | 100001 |                  | 验证成功 |
| 200    | 100002 | id_token不存在   | 验证失败 |
| 200    | 100008 | id_token已经过期 | 验证失败 |

# 3.更新id_token接口

## 接口描述

刷新id_token接口，调用该接口可以为该refresh_token对应的id_token生成新的id_token并更新id_token

## 请求说明

- HTTP方法:**GET**

- Path：``auth/id_refresh_token/{refresh_token}``

  | 参数          | 类型   | 是否必选 | 说明                                      |
  | ------------- | ------ | -------- | ----------------------------------------- |
  | refresh_token | String | 是       | 需要刷新id_token的对应的refresh_token的值 |

- URL示例：``http://localhost:8050/auth/id_refresh_token/10001``

## 返回说明

- 返回参数说明：

| 参数     | 类型   | 说明             |
| -------- | ------ | ---------------- |
| id_token | String | 刷新后的id_token |

- 返回状态说明：

| status | code   | message             | 备注     |
| ------ | ------ | ------------------- | -------- |
| 200    | 100001 |                     | 返回成功 |
| 200    | 100002 | refresh_token不存在 | 验证失败 |



# 4.认证颁发client_token接口

## 接口描述

验证client_token是否存在，请求参数为client_id和sign，如果验证正确但是client的token不存在，则增加该条数据的token值。

## 请求说明

- HTTP方法:**POST**
- Path：``auth/client_token``
- URL示例：``http://localhost:8050/auth/client_token``
- 请求参数说明:

| 参数      | 类型   | 是否必选 | 说明                    |
| --------- | ------ | -------- | ----------------------- |
| client_id | String | 是       | client的client_id值     |
| sign      | String | 是       | secret经过md5加密后的值 |

## 返回说明

- 返回参数说明：

| 参数         | 类型   | 说明                 |
| ------------ | ------ | -------------------- |
| client_token | String | client_id对应的token |

- 返回状态说明：

| status | code   | message           | 备注     |
| ------ | ------ | ----------------- | -------- |
| 200    | 200001 | client_id格式错误 | 验证失败 |
| 200    | 200002 | client_id不存在   | 验证失败 |
