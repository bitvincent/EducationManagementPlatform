
module.exports = [
  {
    // it doesn't work now
    url: '/kaptcha/getKaptcha',
    type: 'post',
    response: config => {
    }
  }, {
    url: '/kaptcha/verifyKaptcha',
    type: 'post',
    response: config => {
      const {verify_token, verify_code}=config.body
      return {
        code: 20000,
        data: {
          status: 0
        }
      }
    }
  }
]
