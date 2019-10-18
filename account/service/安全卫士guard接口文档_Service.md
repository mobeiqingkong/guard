# Guard_Service接口说明文档

- 更新时间：2019/09/21

[TOC]

# 简介

欢迎使用Guard平台。

本文档主要描述Guard的Service层相关技术内容。

## 请求格式

- **请求格式为http://{ip}:{port}/{path}**

| 参数 | 说明                      |
| ---- | ------------------------- |
| ip   | Service程序运行的服务器IP |
| port | Service程序运行的端口     |
| path | Service调用路径           |

## 文档阅读说明

ip是指服务器地址，port是指端口号。

在文档中，ip是127.0.0.1，port是8011。

# 1) 用户管理接口

## 用户基本信息表

| 字段       | 类型     | 说明                                                         |
| ---------- | -------- | ------------------------------------------------------------ |
| deleted    | Boolean  | 用户删除标志，可选值true,false,如果该用户被删除，则将该标志位置为true，如果为false，则该用户未被删除 |
| _id        | ObjectId | 唯一的id，用于用户定位                                       |
| name       | String   | 用户姓名                                                     |
| username   | String   | 用户的账户名                                                 |
| password   | String   | 经过md5加密后的用户密码                                      |
| relative   | Arrray   | 用户关联的角色                                               |
| tel        | String   | 联系电话                                                     |
| enable     | Number   | 是否可用，如果该值为1，则该账号可用，否则不可用              |
| remark     | String   | 备注信息                                                     |
| createTime | String   | 该用户账号创建时间                                           |
| updateTime | String   | 该用户最后一次修改资料时间                                   |
| email      | String   | 用户邮箱地址                                                 |
| updateUser | String   | 修改该用户的用户账号姓名                                     |

## 1.登录

### 接口描述

用户请求登录

### 请求说明

- HTTP方法：**POST**

- path:``/guard/user/login``

- URL示例：`` http://localhost:8011/guard/user/login ``

- 请求参数说明：

  | 参数     | 类型   | 是否必选 | 说明         |
  | -------- | ------ | -------- | ------------ |
  | username | String | **是**   | 用户的账户名 |
  | password | String | **是**   | 用户的密码   |

- 请求示例：

  ```json
  {
  	"username":"admin",
  	"password":"123456"
  }
  ```

### 返回说明

- 返回参数说明：

  | 字段       | 类型     | 说明                                                         |
  | ---------- | -------- | ------------------------------------------------------------ |
  | deleted    | Boolean  | 用户删除标志，可选值true,false,如果该用户被删除，则将该标志位置为true，如果为false，则该用户未被删除 |
  | _id        | ObjectId | 唯一的id，用于用户定位                                       |
  | name       | String   | 用户姓名                                                     |
  | username   | String   | 用户的账户名                                                 |
  | password   | String   | 经过md5加密后的用户密码                                      |
  | relative   | Arrray   | 用户关联的角色                                               |
  | tel        | String   | 联系电话                                                     |
  | enable     | Number   | 是否可用，如果该值为1，则该账号可用，否则不可用              |
  | remark     | String   | 备注信息                                                     |
  | createTime | String   | 该用户账号创建时间                                           |
  | updateTime | String   | 该用户最后一次修改资料时间                                   |
  | email      | String   | 用户邮箱地址                                                 |
  | updateUser | String   | 修改该用户的用户账号姓名                                     |

- 返回成功示例

  ```json
  {
      "deleted": false,
      "relative": [
          "5bc4355ae0a7631a22a2fb6c",
          "5b95d405b106f54b08c0b3e4"
      ],
      "enable": true,
      "_id": "5b97271a1db24d5741c12477",
      "username": "admin",
      "password": "e10adc3949ba59abbe56e057f20f883e",
      "name": "小明",
      "tel": "15100000000",
      "email": "1@9090.com",
      "remark": "管理员",
      "createTime": "2019-03-11 10:20:38",
      "updateTime": "2019-09-05 10:32:10",
      "createUser": "管理员",
      "updateUser": "管理员",
      "__v": 0
  }
  ```



## 2.条件查询

### 接口描述

根据条件查询用户信息，返回符合条件的第一个查询结果

### 请求说明

- HTTP方法：**POST **

- path:``/guard/user/find``

- URL示例：`` http://localhost:8011/guard/user/find ``

- 请求参数说明：

  请求参数可根据用户信息表查询

### 返回说明

- 返回参数说明

  返回查询到的第一个用户信息

- 返回示例：

  ```json
{
      "deleted": false,
      "relative": [
          "5bc4355ae0a7631a22a2fb6c",
          "5b95d405b106f54b08c0b3e4"
      ],
      "enable": true,
      "_id": "5b97271a1db24d5741c12476",
      "username": "admin123456",
    "password": "e10adc3949ba59abbe56e057f20f883e",
      "name": "小红",
    "tel": "16100000000",
      "email": "16@9090.com",
      "remark": "小明的姐姐",
      "createTime": "2019-03-11 10:20:38",
      "updateTime": "2019-09-05 10:32:10",
      "createUser": "管理员",
      "updateUser": "管理员",
      "__v": 0
  }
  ```

## 3.新增用户

### 接口描述

新增用户

### 请求说明

- HTTP方法：**POST**

- path:``/guard/user/add``

- URL示例：`` http://localhost:8011/guard/user/add ``

- 请求参数说明：

  | 字段       | 类型     | 是否必选 | 说明                                                         |
  | ---------- | -------- | -------- | ------------------------------------------------------------ |
  | deleted    | Boolean  | 否       | 用户删除标志，可选值true,false,如果该用户被删除，则将该标志位置为true，如果为false，则该用户未被删除 |
  | _id        | ObjectId | 否       | 唯一的id，用于用户定位                                       |
  | name       | String   | 是       | 用户姓名                                                     |
  | username   | String   | 是       | 用户的账户名                                                 |
  | password   | String   | 是       | 经过md5加密后的用户密码                                      |
  | role       | ObjectId | 否       | 用户角色信息，这里为详细信息                                 |
  | email      | String   | 是       | 邮箱地址                                                     |
  | tel        | String   | 是       | 联系电话                                                     |
  | enable     | Number   | 否       | 是否可用，如果该值为1，则该账号可用，否则不可用              |
  | department | String   | 否       | 用户所属部门                                                 |
  | person     | String   | 否       | 关联人员                                                     |
  | remark     | String   | 否       | 备注信息                                                     |
  | createTime | String   | 否       | 该用户账号创建时间                                           |
  | updateTime | String   | 否       | 该用户最后一次修改资料时间                                   |
  | updateUser | String   | 否       | 修改该用户的用户账号姓名                                     |

- 请求示例：

  ```json
  {
  	"username":"admin123",
  	"tel":"12300000000",
  	"email":"1@qq.com",
  	"name":"猫小白",
  	"password":"123123"
  }
  ```

### 返回说明

- 返回参数说明：

  | 字段       | 类型     | 说明                                                         |
  | ---------- | -------- | ------------------------------------------------------------ |
  | deleted    | Boolean  | 用户删除标志，可选值true,false,如果该用户被删除，则将该标志位置为true，如果为false，则该用户未被删除 |
  | _id        | ObjectId | 唯一的id，用于用户定位                                       |
  | name       | String   | 用户姓名                                                     |
  | username   | String   | 用户的账户名                                                 |
  | password   | String   | 经过md5加密后的用户密码                                      |
  | role       | ObjectId | 用户角色                                                     |
  | department | String   | 用户所属部门                                                 |
  | person     | String   | 关联人员                                                     |
  | tel        | String   | 联系电话                                                     |
  | enable     | Number   | 是否可用，如果该值为1，则该账号可用，否则不可用              |
  | remark     | String   | 备注信息                                                     |
  | createTime | String   | 该用户账号创建时间                                           |
  | updateTime | String   | 该用户最后一次修改资料时间                                   |
  | email      | String   | 邮箱地址                                                     |
  | updateUser | String   | 修改该用户的用户账号姓名                                     |

- 返回成功示例

  ```json
  {
      "deleted": false,
      "relative": [],
      "enable": true,
      "_id": "5d85f8e0b4ff98314c0ea586",
      "username": "admin123",
      "tel": "12300000000",
      "email": "1@qq.com",
      "name": "猫小白",
      "password": "123123",
      "__v": 0
  }
  ```



## 4.修改用户

### 接口描述

修改用户

### 请求说明

- HTTP方法：**PUT**

- path:``/guard/user/update ``

- URL示例：`` http://localhost:8011/guard/user/update ``

- 请求参数说明：

  | 参数   | 字段               | 是否必选 | 说明             |
  | ------ | ------------------ | -------- | ---------------- |
  | filter | 可选用户基本信息表 | **是**   | 过滤用户信息     |
  | params | 可选用户基本信息表 | 是       | 修改后的用户信息 |
  
- 请求示例：

  ```json
  {
  	"filter":{
  		"name":"小明"
  	},
  	"params":{
  		"name":"猫小黑"
  	}
  }
  ```

### 返回说明

- 返回参数说明：

  | 字段      | 类型   | 说明           |
  | --------- | ------ | -------------- |
  | n         | Number | 匹配的数据数量 |
  | nModified | Number | 修改的数量     |
  | ok        | Number | 修改成功的数量 |

- 返回成功示例

  ```json
  {
      "n": 1,
      "nModified": 1,
      "ok": 1
  }
  
  ```



## 5.删除用户

### 接口描述

删除指定用户信息

### 请求说明

- HTTP方法：**DELETE**

- path:``/guard/user/delete/{id}``

  | 参数 | 类型   | 是否必选 | 说明           |
  | ---- | ------ | -------- | -------------- |
  | id   | String | **是**   | 所查询的用户id |

- URL示例：`` http://localhost:8011/guard/user/delete/5d85f8e0b4ff98314c0ea586``

### 返回说明

- 返回参数说明：

  | 字段         | 说明           |
  | ------------ | -------------- |
  | n            | 匹配的数据数量 |
  | ok           | 操作结果       |
  | deletedCount | 删除成功数     |
  
- 返回示例：

  ```json
  {
      "n": 1,
      "ok": 1,
      "deletedCount": 1
  }
  ```



# 2) 角色管理接口

角色基本信息表

| 字段         | 类型     | 说明                                                         |
| ------------ | -------- | ------------------------------------------------------------ |
| deleted      | Boolean  | 角色删除标志，可选值true,false,如果该角色被删除，则将该标志位置为true，如果为false，则该角色未被删除 |
| _id          | ObjectId | 唯一的id，用于角色定位                                       |
| name         | String   | 角色姓名                                                     |
| relationship | Arrray   | 角色关联的称谓                                               |
| tel          | String   | 联系电话                                                     |
| email        | String   | 用户邮箱地址                                                 |

## 1.条件查询

### 接口描述

根据条件查询角色信息，返回符合条件的第一个查询结果

### 请求说明

- HTTP方法：**POST **

- path:``/guard/role/find``

- URL示例：`` http://localhost:8011/guard/role/find ``

- 请求参数说明：

  请求参数可根据角色信息表查询

### 返回说明

- 返回参数说明

  返回查询到的第一个角色信息

- 返回示例：

  ```json
  {
      "deleted": false,
      "_id": "5bc4355ae0a7631a22a2fb6c",
      "name": "小黑",
      "tel": "15100000000",
      "email": "1@9090.com",
      "relationship": "父亲"
  }
  ```

## 2.新增角色

### 接口描述

新增角色

### 请求说明

- HTTP方法：**POST**

- path:``/guard/role/add``

- URL示例：`` http://localhost:8011/guard/role/add ``

- 请求参数说明：

  | 字段         | 类型   | 是否必选 | 说明     |
  | ------------ | ------ | -------- | -------- |
  | name         | String | 是       | 角色姓名 |
  | relationship | String | 是       | 角色关系 |
  | email        | String | 是       | 邮箱地址 |
  | tel          | String | 是       | 联系电话 |

- 请求示例：

  ```json
  {
      "name": "小俄",
      "tel": "15100100000",
      "email": "11@9090.com",
      "relationship": "父亲"
  }
  ```

### 返回说明

- 返回参数说明：

  | 字段         | 类型     | 说明           |
  | ------------ | -------- | -------------- |
  | name         | String   | 角色姓名       |
  | relationship | String   | 角色关系       |
  | email        | String   | 邮箱地址       |
  | tel          | String   | 联系电话       |
  | deleted      | Boolean  | 角色是否被删除 |
  | _id          | ObjectId | 角色的id       |

- 返回成功示例

  ```json
  {
      "deleted": false,
      "_id": "5d85fe6eb4ff98314c0ea588",
      "name": "小俄",
      "tel": "15100100000",
      "email": "11@9090.com",
      "relationship": "父亲",
      "__v": 0
  }
  ```



## 3.修改角色

### 接口描述

修改角色信息

### 请求说明

- HTTP方法：**PUT**

- path:``/guard/role/update ``

- URL示例：`` http://localhost:8011/guard/role/update ``

- 请求参数说明：

  | 参数   | 字段               | 是否必选 | 说明             |
  | ------ | ------------------ | -------- | ---------------- |
  | filter | 可选角色基本信息表 | **是**   | 过滤角色信息     |
  | params | 可选角色基本信息表 | 是       | 修改后的角色信息 |

- 请求示例：

  ```json
  {
      "filter":{
      	"tel" : "13100000000"
      },
      "params":{
      	"tel" : "13111111110"
      }
  }
  ```

### 返回说明

- 返回参数说明：

  | 字段      | 类型   | 说明           |
  | --------- | ------ | -------------- |
  | n         | Number | 匹配的数据数量 |
  | nModified | Number | 修改的数量     |
  | ok        | Number | 修改成功的数量 |

- 返回成功示例

  ```json
  {
      "n": 1,
      "nModified": 1,
      "ok": 1
  }
  
  ```



## 4.删除角色

### 接口描述

删除角色信息

### 请求说明

- HTTP方法：**DELETE**

- path:``/guard/role/delete/{id}``

  | 参数 | 类型   | 是否必选 | 说明           |
  | ---- | ------ | -------- | -------------- |
  | id   | String | **是**   | 所删除的角色id |

- URL示例：`` http://localhost:8011/guard/role/delete/5d85fe6eb4ff98314c0ea588``

### 返回说明

- 返回参数说明：

  | 字段         | 说明           |
  | ------------ | -------------- |
  | n            | 匹配的数据数量 |
  | ok           | 操作结果       |
  | deletedCount | 删除成功数     |

- 返回示例：

  ```json
  {
      "n": 1,
      "ok": 1,
      "deletedCount": 1
  }
  ```

