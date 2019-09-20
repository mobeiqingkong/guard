#!/usr/bin/env node

/**
 * Module dependencies.
 */

var app = require('../src/app')
var debug = require('debug')('demo:server')
var http = require('http')
var basicConfig = require('./basic.config')
require('../src/utils/redis').connect()

/**
 * 全局未捕获异常捕捉
 */
process.on('uncaughtException', (err) => {
  console.log('uncaughtException')
  console.error(err)
})

/**
 * Get port from environment and store in Express.
 */

var port = normalizePort(process.env.PORT || basicConfig.api_port)
// 打印输出端口号
console.log('端口【' + port + '】已启动，正在监听中...')
/**
 * Create HTTP server.
 */

var server = http.createServer(app.callback())

/**
 * Listen on provided port, on all network interfaces.
 */

server.listen(port)
server.on('error', onError)
server.on('listening', onListening)

/**
 * Normalize a port into a number, string, or false.
 */

function normalizePort (val) {
  var port = parseInt(val, 10)

  if (isNaN(port)) {
    // named pipe
    return val
  }

  if (port >= 0) {
    // port number
    return port
  }

  return false
}

/**
 * Event listener for HTTP server "error" event.
 */

function onError (error) {
  if (error.syscall !== 'listen') {
    throw error
  }

  var bind = typeof port === 'string'
    ? 'Pipe ' + port
    : 'Port ' + port

    // handle specific listen errors with friendly messages
  switch (error.code) {
    case 'EACCES':
      console.error(bind + ' requires elevated privileges')
      process.exit(1)
    case 'EADDRINUSE':
      console.error(bind + ' is already in use')
      process.exit(1)
    default:
      throw error
  }
}

/**
 * Event listener for HTTP server "listening" event.
 */

function onListening () {
  var addr = server.address()
  var bind = typeof addr === 'string'
    ? 'pipe ' + addr
    : 'port ' + addr.port
  debug('Listening on ' + bind)
}
