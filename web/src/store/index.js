import { createStore } from 'vuex'
import ModuleUser from './user'
import ModulePk from './pk'
// vuex的使用
export default createStore({
  state: {
  },
  getters: {
  },
  mutations: {
  },
  actions: {
  },
  modules: {
    user: ModuleUser,
    pk: ModulePk,
  }
})
