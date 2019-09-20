var models = {};
(async function () {
  const fs = require('fs')
  const path = require('path')
  const files = await fs.readdirSync(path.join(__dirname, '../models'))
  files.forEach(file => {
    if (file.includes('Entity')) {
      const model = require(path.join(__dirname, `../models/${file}`))
      const [modelName] = file.split(/Entity/)
      models[modelName] = model
    }
  })
})();

// 简单的增删改查，复杂的操作单独写model的helper
exports.find = async (model, query, projection, populate = '') => {
  return models[model].find(query, projection).populate(populate)
}

exports.findOne = async (model, query, populate = '') => {
  return models[model].findOne(query).populate(populate)
}

exports.add = async (model, body) => {
  return models[model].create(body)
}

exports.update = async (model, filter, body) => {
  return models[model].update(filter, body)
}

exports.delete = async (model, filter) => {
  return models[model].remove(filter)
}

