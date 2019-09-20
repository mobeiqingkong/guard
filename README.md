##### 环境依赖

node、redis、mongodb

##### 运行步骤

1.api 业务层  service 数据层

2.分别进入api和service文件夹，运行命令npm install  

3.将common-lib复制到api\node_modules

4.启动项目

​    api: npm run api

​    service: npm run service

##### 目录结构描述

├── api                  // api层

│   ├── config           //配置文件

│   ├── src              // 项目运行

│   ├── .babelrc               

│   ├── package.json              

│   └── Readme.md       

├── common-lib           //依赖包     

├── service              // service层

│   ├── config           //配置文件

│   ├── src              // 项目运行

│   ├── .babelrc               

│   ├── package.json              

│   └── Readme.md          

├── .gitignore           //git忽视文件 

└── Readme.md            // 阅读文件
