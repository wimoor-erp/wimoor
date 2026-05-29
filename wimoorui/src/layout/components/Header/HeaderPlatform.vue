<template>
 <div  class="block">
	 <el-button  size="large" @click="drawer = true"  circle  type="primary" plain>
		  <el-icon style="vertical-align: middle;font-size:18px;">
              <laptop-computer />
          </el-icon>
	  </el-button>
	  <!-- 弹出层 -->
	  <el-drawer v-model="drawer" title="平台选择" direction="ttb" :append-to-body='false' size="">
      <el-row  class="sys-content" :gutter="16">
		  <el-col :lg="3" :md="4" v-for="p in plant" :key="p.plantid" >
			  <div class="platform" >
			  <div class="addAuthorization" v-show='p.imgsty!==""'>
				 <el-icon style="font-size:20px"><plus /></el-icon>
				 &nbsp;<span> 添加授权</span>
			  </div>
			 <el-image :class='p.imgsty' :src="$require(p.url)"></el-image>

			  </div>
		  </el-col>
	  </el-row >
     </el-drawer>
	</div>
</template>

<script >
import { h, resolveComponent,ref } from 'vue'
import { ElDivider } from 'element-plus'
import {Plus} from '@element-plus/icons-vue';
import {LaptopComputer} from '@icon-park/vue-next';
export default {
        name: "HeaderPlatform",
        components:{
			LaptopComputer,Plus
		},
	setup() {
		//数据
	   let plant=[
			   {plantid:'amazon',url:'plantlogo/unauthorized-amz.png',imgsty:''},
			   {plantid:'amazon',url:'plantlogo/unauthorized-spee.png',imgsty:'img-gray' },
			   {plantid:'amazon',url:'plantlogo/unauthorized-wish.png',imgsty:'img-gray' },
			   {plantid:'amazon',url:'plantlogo/unauthorized-smt.png',imgsty:'img-gray' },
			   {plantid:'amazon',url:'plantlogo/unauthorized-ebay.png',imgsty:'img-gray' },
			   {plantid:'amazon',url:'plantlogo/unauthorized-cd.png',imgsty:'img-gray' },
			   {plantid:'amazon',url:'plantlogo/unauthorized-dh.png' ,imgsty:'img-gray'},
			   {plantid:'amazon',url:'plantlogo/unauthorized-walmart.png',imgsty:'img-gray' },
			   {plantid:'amazon',url:'plantlogo/unauthorized-kilimall.png' ,imgsty:'img-gray'},
			   {plantid:'amazon',url:'plantlogo/unauthorized-mgt.png' ,imgsty:'img-gray'},
			   {plantid:'amazon',url:'plantlogo/unauthorized-woc.png',imgsty:'img-gray' },
			   {plantid:'amazon',url:'plantlogo/unauthorized-jd.png' ,imgsty:'img-gray'},
		       
	   ]
       return {
	   size: 0,  
	   plant, 
	   spacer: h(ElDivider, { direction: 'vertical' }),
	   drawer: ref(false),
    }
  },
	}
</script>
<style >
.addAuthorization{position: absolute;opacity:0;color:var(--el-color-primary);display:flex;}
.platform:hover .addAuthorization{opacity:1;transition:opacity 0.3s}
.platform:hover .img-gray{opacity:0;transition:opacity 0.3s}
.platform{ height:72px;align-items: center;display: flex !important;justify-content: center;
           border:1px solid var(--el-border-color-base);cursor: pointer;border-radius: 2px;
		   transition: all .2s ease-in-out;
}
.platform .img-gray{opacity: 0.5;}
.platform:hover{
	 border-color:var(--el-color-primary-light-2);
}
   .block .el-overlay{
	  z-index: 2000!important;
  }
    .block  .el-overlay .el-drawer.ttb{border-top:1px solid #eee} 
@media screen and (max-width:1440px){
	.platform{
		height:64px;
	}
}
.sys-content .is-guttered{padding-bottom:16px;}
.block{
	display: flex;
	align-items: center;
	justify-content: center;
	margin-bottom:16px;
}
.ttb .el-drawer__header{
	margin-bottom: 0px;
}
</style>