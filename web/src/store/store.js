/**
 * 示例：
   get: this.$store.state.xxx
   set: this.$store.commit('xxx'，xxx);
 */
import Vuex from 'vuex'
import Vue from 'vue'

Vue.use(Vuex)
export default new Vuex.Store({
  state: {
    // isValidator: true,
    toLink: ''
  },
  mutations: {
    // setValidator: (state, bool) => {
    //   state.isValidator = bool;
    // },
  }
})
