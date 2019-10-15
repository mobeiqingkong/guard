import {
  mongodb_url as mongodbUrl
} from '../../config/basic.config'
const logger = require('../middlewares/Logger').getLogger('mongoose')

var mongoose = require('mongoose')

class DB {
  constructor () {
    this.conn = null
  }
  async connect () {
    var db = mongoose.connection
    db.on('error', console.error.bind(console, 'connection error'))
    db.once('open', (cb) => {
      console.log('open db connection')
    })
    mongoose.set('useCreateIndex', true)
    mongoose.set('debug', function (collectionName, methodName, arg1, arg2, arg3, arg4) {
      let logMsg = ''
      logMsg = `Model ${collectionName} ${methodName} ${JSON.stringify(arg1)}`
      if (arg2) logMsg += ` ${JSON.stringify(arg2)}`
      if (arg3) logMsg += ` ${JSON.stringify(arg3)}`
      if (arg4) logMsg += ` ${JSON.stringify(arg4)}`
      logger.debug(logMsg)
    })
    mongoose.set('useFindAndModify', false)
    this.conn = mongoose.connect(mongodbUrl, { useNewUrlParser: true })
  }
}

module.exports = new DB()
