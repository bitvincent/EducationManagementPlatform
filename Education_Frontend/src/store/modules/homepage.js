import { getRecommendClass, searchClass } from '@/api/classes'

const state = {
  classes: [],
  schools: [{
    id: 1,
    label: '北京大学'
  }, {
    id: 2,
    label: '清华大学'
  }, {
    id: 3,
    label: '人民大学'
  }, {
    id: 4,
    label: '复旦大学'
  }, {
    id: 5,
    label: '浙江大学'
  }, {
    id: 6,
    label: '南京大学'
  }, {
    id: 7,
    label: '同济大学'
  }],
  classCategories: [{
    id: 1,
    label: '哲学'
  }, {
    id: 2,
    label: '经济学'
  }, {
    id: 3,
    label: '法学'
  }, {
    id: 4,
    label: '教育学'
  }, {
    id: 5,
    label: '文学'
  }, {
    id: 6,
    label: '历史学'
  }, {
    id: 7,
    label: '理学'
  }, {
    id: 8,
    label: '工学'
  }, {
    id: 9,
    label: '农学'
  }, {
    id: 10,
    label: '医学'
  }, {
    id: 11,
    label: '军事学'
  }, {
    id: 12,
    label: '管理学'
  }, {
    id: 13,
    label: '艺术学'
  }]
}

const mutations = {
  SET_CLASSES: (state, classes) => {
    state.classes = classes
  }
}

const actions = {
  searchClasses({ commit }, cond) {
    return new Promise((resolve, reject) => {
      searchClass(cond).then(response => {
        const { data } = response
        commit('SET_CLASSES', data)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  getClasses({ commit }) {
    return new Promise((resolve, reject) => {
      getRecommendClass({ neednum: 11 }).then(response => {
        const { data } = response
        commit('SET_CLASSES', data)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
