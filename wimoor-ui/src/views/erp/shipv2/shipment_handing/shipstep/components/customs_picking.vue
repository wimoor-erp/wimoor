<template>
  <div class="flex-between">
    <div style="padding-left:15px;padding-bottom:20px">
      <el-radio-group v-model="tabActive"     @change="tabchange"  >
        <el-radio-button label="出口报关" :value="1" />
        <el-radio-button label="进口报关" :value="2" />
      </el-radio-group>
    </div>
  </div>


		  <CustomsDeclaration ref="customsDeclarationRef" v-if="tabActive===1"  ></CustomsDeclaration>
      <CustomsClearance ref="customsClearanceRef" v-if="tabActive===2"  ></CustomsClearance>


</template>
<script>
</script>
<script setup>
	import { ref,reactive,onMounted,nextTick, toRefs } from 'vue'
	import { useRoute,useRouter } from 'vue-router'
	import CustomsDeclaration from "./customs_declaration.vue";
  import CustomsClearance from "./customs_clearance.vue";

	const emit = defineEmits(['change']);
	const state =reactive({
		tabActive:1,
		data:{},
	})
	const{
		data,
		tabActive,
	}=toRefs(state)
	let router = useRouter()
	const customsDeclarationRef=ref();
  const customsClearanceRef=ref();
    function show(row,mstep){
        localRow.value=row;
        state.tabActive=parseInt(mstep);
        shipmentid.value=row.shipmentid;
        shipmentRef.value.getBaseInfo(row.shipmentid);
    }
    function handleShipmentInfo(data){
        state.shipmentData = data;
        nextTick(()=>{
             handleStep(data);
        });
    }
			function tabchange(val){
				handleStep(state.data);
			}
			function handleStep(data){
				nextTick(()=>{
				   if(state.tabActive===1){
								customsDeclarationRef.value.loadOptData(state.data);
						}else if(state.tabActive===2){
								customsClearanceRef.value.loadOptData(state.data);
						}
				})
			}
  function loadOptData(data){
    state.data=data;
    handleStep(data);
  }
  defineExpose({loadOptData})
</script>
<style >

	.ship-detail-dialog .el-tabs--card>.el-tabs__header .el-tabs__item.is-active{
		    border-top-color: #ff7315;
	}
 .ship-detail-dialog .el-tabs--card>.el-tabs__header .el-tabs__item{
	 border-top: 2px solid transparent;
 }
</style>
