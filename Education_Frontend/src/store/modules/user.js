import { getInfo, loginMplx, loginSMSMplx } from '@/api/user'
import { getToken, setToken, removeToken, setUserType, getUserType } from '@/utils/auth'
import router, { resetRouter } from '@/router'

const state = {
  token: getToken(),
  userType: getUserType(),
  teacherId: -1,
  name: '',
  avatar: '',
  introduction: '',
  roles: [],
  info: {}
  /*
    number(手机号)  teachername(老师名称)  gender（性别，int）  idcard(身份证号码)
     schoolid(学校id，int)  title (职称，int)  teacherlogo(老师头像,url)
  */
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_INTRODUCTION: (state, introduction) => {
    state.introduction = introduction
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_USERTYPE: (state, type) => {
    state.userType = type
  },
  SET_TEACHER_ID: (state, teacherId) => {
    state.teacherId = teacherId
  },
  SET_INFO: (state, info) => {
    state.info = info
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  }
}

const actions = {

  // our user login
  myLogin({ commit }, { type, form }) {
    return new Promise((resolve, reject) => {
      loginMplx(type, form).then(response => {
        const { data } = response
        if (!data || !data.token) {
          reject(response.message)
          return
        }
        commit('SET_TOKEN', data.token)
        setToken(data.token)
        commit('SET_USERTYPE', type)
        setUserType(type)
        if (data.teacherid) {
          commit('SET_TEACHER_ID', data.teacherid)
        }
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  mySMSLogin({ commit }, { type, form, verify_token }) {
    return new Promise((resolve, reject) => {
      loginSMSMplx(type, form, verify_token).then(response => {
        const { data } = response
        if (!data || !data.token) {
          reject(response.message)
          return
        }
        commit('SET_TOKEN', data.token)
        setToken(data.token)
        commit('SET_USERTYPE', type)
        setUserType(type)
        if (data.teacherid) {
          commit('SET_TEACHER_ID', data.teacherid)
        }
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo(state.userType, state.token).then(response => {
        const { data } = response

        if (!data) {
          reject('Verification failed, please Login again.')
        }

        // const { roles, name, avatar, introduction } = data

        // // roles must be a non-empty array
        // if (!roles || roles.length <= 0) {
        //   reject('getInfo: roles must be a non-null array!')
        // }
        const roles = []
        data.roles = roles
        commit('SET_ROLES', [roles])
        // commit('SET_NAME', name)
        // commit('SET_AVATAR', avatar)
        // commit('SET_INTRODUCTION', introduction)
        commit('SET_INFO', data)
        if (data.teacherid) {
          commit('SET_TEACHER_ID', data.teacherid)
        }
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user logout
  logout({ commit, state, dispatch }) {
    return new Promise((resolve, reject) => {
      commit('SET_TOKEN', '')
      commit('SET_ROLES', [])
      removeToken()
      resetRouter()
      resolve()
      // logout(state.token).then(() => {
      //   commit('SET_TOKEN', '')
      //   commit('SET_ROLES', [])
      //   removeToken()
      //   resetRouter()

      //   // reset visited views and cached views
      //   // to fixed https://github.com/PanJiaChen/vue-element-admin/issues/2485
      //   dispatch('tagsView/delAllViews', null, { root: true })

      //   resolve()
      // }).catch(error => {
      //   reject(error)
      // })
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '')
      commit('SET_ROLES', [])
      removeToken()
      resolve()
    })
  },

  // dynamically modify permissions
  async changeRoles({ commit, dispatch }, role) {
    const token = role + '-token'

    commit('SET_TOKEN', token)
    setToken(token)

    const { roles } = await dispatch('getInfo')

    resetRouter()

    // generate accessible routes map based on roles
    const accessRoutes = await dispatch('permission/generateRoutes', roles, { root: true })
    // dynamically add accessible routes
    router.addRoutes(accessRoutes)

    // reset visited views and cached views
    dispatch('tagsView/delAllViews', null, { root: true })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
