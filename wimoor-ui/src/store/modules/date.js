const state = {
 dateType:'day',
 dateValue:[],
 week:'',
 times:'',
 isActiveDate:true,
 tabName:'',
}

const mutations = {
   increment (state,val) {
      state.dateType =val 
    },
	getDate(state,val){
		state.dateValue = val
	},
	getWeek(state,val){
		state.week = val
	},
	getTimers(state,val){
		state.times =val
	},
	setActiveDate(state,val){
		state.isActiveDate =val
	},
	setTabName(state,val){
		state.tabName =val
	}
}

const actions = {

}
const getters = {
	
}

export default {
  namespaced: true,
  state,
  mutations,
  actions,
  getters,
}