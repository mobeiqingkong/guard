# Service层接口说明文档

- 更新时间：2019/09/20

[TOC]

# 简介

欢迎使用CAS统一认证平台。

本文档主要描述CAS统一认证平台的Service层相关技术内容。

## 请求格式

- **请求格式为http://{ip}:{port}/{path}**

| 参数 | 说明                      |
| ---- | ------------------------- |
| ip   | Service程序运行的服务器IP |
| port | Service程序运行的端口     |
| path | Service调用路径           |

## 文档阅读说明

ip是指服务器地址，port是指端口号。

在文档中，ip是127.0.0.1，port是8051。

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

| 字段                 | 类型   | 说明                                              |
| -------------------- | ------ | ------------------------------------------------- |
| value                | String | id_token的值                                      |
| client_id            | String | 应用客户端id                                      |
| account_id           | String | 用户id                                            |
| expired_time         | String | 过期时间                                          |
| refresh_token        | String | 由统一认证中心颁发的刷新令牌，更新id_token(value) |
| refresh_expired_time | String | 更新token的refresh_token的过期时间                |

# id_token

## 1.查找id_token

### 接口描述

查找一条id_token数据

### 请求说明

- HTTP方法:**POST**

- Path：``auth/id_token/find``

- URL示例：``http://localhost:8051/auth/id_token/find``

- 请求参数说明：

  | 参数                 | 类型   | 是否必选 | 说明                       |
  | -------------------- | ------ | -------- | -------------------------- |
  | _id                  | String | 否       | 这条数据的_id值            |
  | client_id            | String | 否       | client的id值               |
  | account_id           | String | 否       | id_token对应的account的_id |
  | refresh_expired_time | String | 否       | 刷新过期的时间             |
  | refresh_token        | String | 否       | 用来刷新的标志token        |
  | expired_time         | String | 否       | 过期时间                   |
  | value                | String | 否       | id_token的值               |

- 请求示例：

  ```json
  //查找client_id和refresh_token都为3的数据
  {
  	 "client_id": "3",
       "refresh_token":"3"
  }
  ```

  

### 返回说明

- 返回成功说明：

  | 参数                 | 类型   | 说明                       |
  | -------------------- | ------ | -------------------------- |
  | _id                  | String | 这条数据的_id值            |
  | client_id            | String | client的id值               |
  | account_id           | String | id_token对应的account的_id |
  | refresh_expired_time | String | 刷新过期的时间             |
  | refresh_token        | String | 用来刷新的标志token        |
  | expired_time         | String | 过期时间                   |
  | value                | String | id_token的值               |
  | _v                   | Number | 系统自动生成不影响         |

- 返回示例：

  ```json
  {
      "_id": "5d774a70e490b49508be1ce9",
      "client_id": "3",
      "account_id": "5d25a9fb3d99198801f13028",
      "refresh_expired_time": "6",
      "refresh_token": "3",
      "expired_time": "2019-09-11 16:02:08",
      "value": "3",
      "__v": 0
  }
  ```

  

## 2.增加id_token

### 接口描述

增加一条id_token数据

### 请求说明

- HTTP方法:**POST**

- Path：``auth/id_token/add``

- URL示例：``http://localhost:8051/auth/id_token/add``

- 请求参数说明：

  | 参数                 | 类型   | 是否必选 | 说明                       |
  | -------------------- | ------ | -------- | -------------------------- |
  | client_id            | String | 是       | client的id值               |
  | account_id           | String | 否       | id_token对应的account的_id |
  | refresh_expired_time | String | 是       | 刷新过期的时间             |
  | refresh_token        | String | 是       | 用来刷新的标志token        |
  | expired_time         | String | 是       | 过期时间                   |
  | value                | String | 是       | id_token的值               |

- 请求示例：

  ```json
  {
      "client_id": "3",
      "account_id": "5d25a9fb3d99198801f13028",
      "refresh_expired_time": "6",
      "refresh_token": "3",
      "expired_time": "2019-09-11 16:02:08",
      "value": "3"
  }
  ```

  

### 返回说明

- 返回成功说明：

  | 参数                 | 类型   | 说明                       |
  | -------------------- | ------ | -------------------------- |
  | _id                  | String | 这条数据的_id值            |
  | client_id            | String | client的id值               |
  | account_id           | String | id_token对应的account的_id |
  | refresh_expired_time | String | 刷新过期的时间             |
  | refresh_token        | String | 用来刷新的标志token        |
  | expired_time         | String | 过期时间                   |
  | value                | String | id_token的值               |
  | _v                   | Number | 系统自动生成不影响         |

- 返回示例：

  ```json
  {
      "_id": "5d789f19eb49ce45a4f1d1ba",
      "client_id": "3",
      "account_id": "5d25a9fb3d99198801f13028",
      "refresh_expired_time": "6",
      "refresh_token": "3",
      "expired_time": "2019-09-11 16:02:08",
      "value": "3",
      "__v": 0
  }
  ```

  

## 3.修改id_token

### 接口描述

修改一条id_token数据

### 请求说明

- HTTP方法:**PUT**

- Path：``auth/id_token/update``

- URL示例：``http://localhost:8051/auth/id_token/update``

- 请求参数说明：

  | 对象名 | 参数                 | 类型   | 是否必选 | 说明                       |
  | ------ | -------------------- | ------ | -------- | -------------------------- |
  | filter |                      | Object | 是       | 需要修改的数据的筛选条件   |
  | params |                      | Object | 是       | 需要修改数据的值           |
  |        | client_id            | String | 否       | client的id值               |
  |        | account_id           | String | 否       | id_token对应的account的_id |
  |        | refresh_expired_time | String | 否       | 刷新过期的时间             |
  |        | refresh_token        | String | 否       | 用来刷新的标志token        |
  |        | expired_time         | String | 否       | 过期时间                   |
  |        | value                | String | 否       | id_token的值               |

- 请求示例：

  ```json
  //将client_id为123的这条数据的value值修改为qwe
  {
      "filter":{
          "client_id":"123"
      },
      "params":{
          "value":"qwe"
      }
  }
  ```

  

### 返回说明

- 返回参数说明：

  | 参数      | 类型   | 说明                 |
  | --------- | ------ | -------------------- |
  | n         | Number | 符合查询结果的条目数 |
  | nModified | Number | 修改的条目数         |
  | ok        | Number | 修改成功的数量       |

- 返回示例：

  ```json
  //成功修改一条数据
  {
      "n": 1,
      "nModified": 1,
      "ok": 1
  }
  ```

  

## 4.删除id_token

### 接口描述

删除一条id_token数据

### 请求说明

- HTTP方法:**DELETE**
- Path：``auth/id_token/delete/:_id``
- URL示例：``http://localhost:8051/auth/id_token/delete/5d789171c37bcc2ab4475203``

### 返回说明

- 返回参数说明：

  | 参数         | 类型   | 说明                 |
  | ------------ | ------ | -------------------- |
  | n            | Number | 符合查询结果的条目数 |
  | ok           | Number | 删除成功的数量       |
  | deletedCount | Number | 删除的条目数         |

- 返回示例：

  ```json
  //成功删除一条数据
  {
      "n": 1,
      "ok": 1,
      "deletedCount": 1
  }
  ```





# client_token

## 1.查找client_token

### 接口描述

查找一条client_token数据

### 请求说明

- HTTP方法:**POST**

- Path：``auth/client_token/find``

- URL示例：``http://localhost:8051/auth/client_token/find``

- 请求参数说明：

  | 参数         | 类型   | 是否必选 | 说明            |
  | ------------ | ------ | -------- | --------------- |
  | _id          | String | 否       | 这条数据的_id值 |
  | name         | String | 否       | client的名字    |
  | client_id    | String | 否       | client的id值    |
  | secret       | String | 否       | client的加密值  |
  | token        | String | 否       | client的token   |
  | isInternal   | String | 否       | 是否为内部系统  |
  | created_time | String | 否       | 创建时间        |

- 请求示例：

  ```json
  //查找client_id为3的数据
  {
  	 "client_id": "3"
  }
  ```
  


### 返回说明

- 返回成功说明：

  | 参数         | 类型   | 说明               |
  | ------------ | ------ | ------------------ |
  | _id          | String | 这条数据的_id值    |
  | name         | String | client的名字       |
  | client_id    | String | client的id值       |
  | secret       | String | client的加密值     |
  | token        | String | client的token      |
  | isInternal   | String | 是否为内部系统     |
  | created_time | String | 创建时间           |
  | __v          | Number | 系统自动生成不影响 |

- 返回示例：

  ```json
  {
      "_id": "5d72233234ff68421811dd70",
      "name": "手机",
      "client_id": "12333",
      "secret": "123456",
      "token": "10000",
      "isInternal": true,
      "created_time": "2099",
      "__v": 0
  }
  ```

  

## 2.增加client_token

### 接口描述

增加一条client_token数据

### 请求说明

- HTTP方法:**POST**

- Path：``auth/client_token/add``

- URL示例：``http://localhost:8051/auth/client_token/add``

- 请求参数说明：

  | 参数       | 类型   | 是否必选 | 说明               |
  | ---------- | ------ | -------- | ------------------ |
  | name       | String | 是       | client的名字       |
  | client_id  | String | 是       | client的id值       |
  | secret     | String | 是       | 验证client的加密值 |
  | token      | String | 是       | client的token      |
  | isInternal | String | 是       | 是否为内部系统     |
  
- 请求示例：

  ```json
  {
  	"name": "123123",
      "client_id": "123313",
      "secret": "123456",
      "token": "10000",
      "isInternal": true
  }
  ```
  


### 返回说明

- 返回成功说明：

  | 参数         | 类型   | 说明               |
  | ------------ | ------ | ------------------ |
  | _id          | String | 这条数据的_id值    |
  | name         | String | client的名字       |
  | client_id    | String | client的id值       |
  | secret       | String | client的加密值     |
  | token        | String | client的token      |
  | isInternal   | String | 是否为内部系统     |
  | created_time | String | 创建时间           |
  | __v          | Number | 系统自动生成不影响 |

- 返回示例：

  ```json
  {
      "_id": "5d78a623a2dffa55f48b106f",
      "name": "123123",
      "client_id": "123313",
      "secret": "123456",
      "token": "10000",
      "isInternal": true,
      "created_time": "2019-09-11 15:45:39",
      "__v": 0
  }
  ```




## 3.修改client_token

### 接口描述

修改一条client_token数据

### 请求说明

- HTTP方法:**PUT**

- Path：``auth/client_token/update``

- URL示例：``http://localhost:8051/auth/client_token/update``

- 请求参数说明：

  | 对象名 | 参数         | 类型   | 是否必选 | 说明                     |
  | ------ | ------------ | ------ | -------- | ------------------------ |
  | filter |              | Object | 是       | 需要修改的数据的筛选条件 |
  | params |              | Object | 是       | 需要修改数据的值         |
  |        | client_id    | String | 否       | client的id值             |
  |        | name         | String | 否       | client的name             |
  |        | secret       | String | 否       | 验证client的值           |
  |        | token        | String | 否       | client的token            |
  |        | isInternal   | String | 否       | 过期时间                 |
  |        | created_time | String | 否       | 创建时间                 |

- 请求示例：

  ```json
  //将name为123123的这条数据的name值修改为123123123
  {
  	"filter":{
  		"name":"123123"
  	},
  	"params":{
  		"name": "123123123"
  	}
  }
  ```


### 返回说明

- 返回参数说明：

  | 参数      | 类型   | 说明                 |
  | --------- | ------ | -------------------- |
  | n         | Number | 符合查询结果的条目数 |
  | nModified | Number | 修改的条目数         |
  | ok        | Number | 修改成功的数量       |

- 返回示例：

  ```json
  //成功修改一条数据
  {
      "n": 1,
      "nModified": 1,
      "ok": 1
  }
  ```




## 4.删除client_token

### 接口描述

删除一条client_token数据

### 请求说明

- HTTP方法:**DELETE**
- Path：``auth/client_token/delete/:_id``
- URL示例：``http://localhost:8051/auth/client_token/delete/5d789171c37bcc2ab4475203``

### 返回说明

- 返回参数说明：

  | 参数         | 类型   | 说明                 |
  | ------------ | ------ | -------------------- |
  | n            | Number | 符合查询结果的条目数 |
  | ok           | Number | 删除成功的数量       |
  | deletedCount | Number | 删除的条目数         |

- 返回示例：

  ```json
  //成功删除一条数据
  {
      "n": 1,
      "ok": 1,
      "deletedCount": 1
  }
  ```
  