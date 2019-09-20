var path = require('path')

// 日志根目录
var baseLogPath = path.resolve(__dirname, '../logs')
// 错误日志目录
var errorPath = '/error'
// 错误日志文件名
var errorFileName = 'error.log'
// 错误日志输出完整路径
var errorLogPath = baseLogPath + errorPath + '/' + errorFileName
// 响应日志目录
var responsePath = '/response'
// 响应日志文件名
var responseFileName = 'response.log'
// 响应日志输出完整路径
var responseLogPath = baseLogPath + responsePath + '/' + responseFileName
module.exports = {
  pm2: process.env.NODE_ENV === 'production', // 如果使用 pm2 -i 方式启动的 node 进程需要设置次为 true
  appenders: {
    errorLogger: { // 错误日志
      'type': 'file',
      'filename': errorLogPath,
      'maxLogSize': 1024 * 520,
      'backups': 8
    },

    resLogger: { // 响应日志
      'type': 'file',
      'filename': responseLogPath,
      'maxLogSize': 1024 * 1024 * 2,
      'backups': 8
    },

    guard: { // 非请求日志
      'type': 'file',
      'filename': `${baseLogPath}/guard/guard.log`,
      'maxLogSize': 1024 * 1024 * 2,
      'backups': 30
    },

    mongoose: {
      'type': 'file',
      'filename': `${baseLogPath}/mongoose/mongoose.log`,
      'maxLogSize': 1024 * 1024 * 2,
      'backups': 30
    },
	  guard_console: { type: 'console' }
  },
  categories: {
    default: {
      appenders: ['resLogger'],
      level: 'info'
    },
    error: {
      appenders: ['errorLogger', 'resLogger'],
      level: 'error'
    },
    guard: {
      appenders: ['guard','guard_console'],
      level: 'debug'
    },
    mongoose: {
      appenders: ['mongoose', 'guard_console'],
      level: 'debug'
    }
  }
}
