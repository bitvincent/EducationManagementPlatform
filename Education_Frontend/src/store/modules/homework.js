
const state = {
  currentHomework: {}
}

const mutations = {
  SET_HOMEWORK: (state, homework) => {
    console.log(homework)
    state.currentHomework = homework
  }
}

const actions = {
  setHomework({ commit }, hw) {
    console.log('action set')
    commit('SET_HOMEWORK', hw)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
