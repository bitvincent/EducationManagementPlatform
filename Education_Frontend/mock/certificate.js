module.exports = [
  {
    url: '/class/getRecommendClass',
    type: 'post',
    response: config => {
      const data = Array(11)
        .fill(1)
        .map((_, idx) => {
          return {
            classid: idx + 1,
            classname: '操作系统',
            schoolid: 1,
            number: 100 + idx,
            description: '高级操作系统',
            classpic:
              'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg'
          };
        });
      return {
        code: 20000,
        data
      };
    }
  },
  {
    url: '/class/searchClass',
    type: 'post',
    response: config => {
      const { classname } = config.body;
      console.log(classname);
      return {
        code: 20000,
        data: [
          {
            classid: 1,
            classname: '操作系统',
            schoolid: 1,
            number: 100,
            description: '高级操作系统',
            classpic:
              'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg'
          },
          {
            classid: 2,
            classname: '计算机网络',
            schoolid: 1,
            number: 200,
            description: '高级操作系统',
            classpic:
              'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg'
          }
        ]
      };
    }
  }
];
