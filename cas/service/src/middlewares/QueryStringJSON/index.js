var merge = require('merge-descriptors')

module.exports = function (app) {
  var qs = require('qs')
  var converter = function (value) {
    try {
      return JSON.parse(value)
    } catch (err) {
      return value
    }
  }

  merge(app.request, {

    /**
     * Get parsed query-string.
     *
     * @return {Object}
     * @api public
     */

    get query () {
      var str = this.querystring
      if (!str) return {}

      var c = this._querycache = this._querycache || {}
      var query = c[str]
      if (!query) {
        c[str] = query = qs.parse(str, {
	        decoder (str, decoder, charset) {
		        const strWithoutPlus = str.replace(/\+/g, ' ');
		        if (charset === 'iso-8859-1') {
			        return strWithoutPlus.replace(/%[0-9a-f]{2}/gi, unescape);
		        }
		        if (!Number.isNaN(str) && (/^(\d+|\d*\.\d+)$/.test(str) || /^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$/.test(str))) {
							let result = parseFloat(str)
							if (str !== String(result)) {
								return str
							}
							return result
						}
		        const keywords = {
			        true: true,
			        false: false,
			        null: null,
			        undefined,
		        }
		        if (str in keywords) {
			        return keywords[str]
		        }
		        // utf-8
		        try {
			        return decodeURIComponent(strWithoutPlus);
		        } catch (e) {
			        return strWithoutPlus;
		        }
	        }
        })
      }
      return query
    },

    /**
     * Set query-string as an object.
     *
     * @param {Object} obj
     * @api public
     */

    set query (obj) {
      this.querystring = qs.stringify(obj)
    }
  })

  return app
}
