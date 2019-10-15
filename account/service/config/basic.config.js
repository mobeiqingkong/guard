const config = {
  // 程序运行端口
  service_port: 8011,
  // 域名
  service_dns: '127.0.0.1',
  // 静态资源路径
  resource_url: '/build',
  // 数据库地址
  mongodb_url: 'mongodb://127.0.0.1:27017/guard',
  mongodb_db: 'guard',
  key: '180330CDB6DBEAFCB55806DD41255B7C'
}

module.exports = config
