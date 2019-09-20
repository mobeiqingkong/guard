module.exports = {
  // 必须配置这两项
  product: 'iows',
  serv: 'service',
  // 通过日志启用配置，启用某些特定模块的日志记录
  enableModules: {
    mongoose: {
      enable: false,
      level: 'debug'
    },
    redis: {
      enable: true,
      level: 'debug'
    }
  },
  pm2: process.env.NODE_ENV === 'production', // 如果使用 pm2 -i 方式启动的 node 进程需要设置次为 true
  appenders: {
    servAppender: {
      type: 'dateFile',
      filename: 'serv.log',
      pattern: '-yyyy-MM-dd',
      daysToKeep: 15,
      maxLogSize: 20 * 1024,
      backups: 10,
      layout: {type: 'coloured'}
    },
    debugAppender: {
      type: 'dateFile',
      filename: 'debug.log',
      pattern: '-yyyy-MM-dd',
      daysToKeep: 15,
      maxLogSize: 20 * 1024,
      backups: 10,
      layout: {type: 'coloured'}
    },

	  console: { type: 'console' }
  },
  categories: {
    default: {
      appenders: ['servAppender','console'],
      level: 'info'
    },
    debug: {
      appenders: ['debugAppender','console'],
      level: 'debug'
    },
    serv: {
      appenders: ['servAppender','console'],
      level: 'info'
    }
  }
}
