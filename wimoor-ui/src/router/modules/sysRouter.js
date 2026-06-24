/** When your routing erp is too long, you can split it into small modules **/
 
export default [
  {
	 path: '/ssologin',
	 name: 'SSOLogin',
	 component:()=>import("@/views/sys/SSOLogin.vue")
   },
  {
    path: '/login',
    name: 'Login',
  	component:()=>import("@/views/sys/Login.vue")
  },
  {
    path: '/register',
    name: 'Register',
    component:()=>import("@/views/sys/Register.vue")
  },
  {
    path: '/resetPassword',
    name: 'ResetPassword',
    component:()=>import("@/views/sys/userCenter/resetPassword.vue")
  },
  {
    path: '/orderblank',
    name: 'OrderBlank',
     component:()=>import("@/views/erp/ship/orderblank/index.vue")
  },
  {
    path: '/authresult',
    name: 'AuthResult',
     component:()=>import("@/views/amazon/storeAuth/authResult/index.vue")
  },
  {
    path: '/advresult',
    name: 'AdvResult',
    component:()=>import("@/views/amazon/storeAuth/advResult/index.vue")
  },
  {
  	 path: '/quote',
  	 name: 'quote',
     component:()=>import("@/views/erp/shipv2/quote/price.vue")
   },

]
