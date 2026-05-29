<template>
    <div class="main-sty">
       <div class="con-header" >
         <el-row>
           <el-space >
         <el-select v-model="store"  placeholder="全部店铺">
               <el-option  v-for="item in storeList"   :key="item.value"  :label="item.label" :value="item.value"   >
               </el-option>
         </el-select>
         <el-select v-model="country"  placeholder="全部国家">
               <el-option  v-for="item in countryList"   :key="item.value"  :label="item.label" :value="item.value"   >
               </el-option>
         </el-select>
         <el-select v-model="orderType"  placeholder="全部状态">
               <el-option  v-for="item in orderType"   :key="item.value"  :label="item.label" :value="item.value"   >
               </el-option>
         </el-select>
         <el-date-picker
                 v-model="dateValue"
         		  @change = "dateChange"
                 type="daterange"
         		  :clearable="false"
                 range-separator="至"
                 start-placeholder="开始日期"
                 end-placeholder="结束日期"
                 :shortcuts="shortcuts"
               />
          <el-input  v-model="searchKeywords" placeholder="请输入" class="input-with-select" >
             <template #prepend>
               <el-select v-model="selectlabel" @change='searchTypeChange' placeholder="SKU" style="width: 110px">
                 <el-option label="SKU" value="1"></el-option>
                 <el-option label="ASIN" value="2"></el-option>
                 <el-option label="订单号" value="3"></el-option>
               </el-select>
             </template>
             <template #append>
               <el-button @click="searchConfirm">
                  <el-icon style="font-size: 16px;align-itmes:center">
                   <search />
                </el-icon>
               </el-button>
             </template>
           </el-input>
          <el-popover   :popper-append-to-body="false" v-model:visible="moreSearchVisible" :width="400" trigger="click">
                <template #reference>
                  <el-button  title='高级筛选'  class='ic-btn'>
                  <menu-unfold theme="outline" size="16"  :strokeWidth="3"/>
                  </el-button>
                </template>
       		  <el-form :model="form" label-width="80px">
       		  <el-form-item label="颜色">
       		       <el-select  :popper-append-to-body="false" placeholder="颜色选择" @change="colorChange">
       		       <el-option
       		              v-for="item in color"
       		             :key="item.value"
       		             :label="item.label"
       		             :value="item.value"
       		           >
       				   <div class="color-select">
       		             <span >{{ item.label }}</span>
       		             <span :class="'cilcle-'+item.value"></span  >
       					</div> 
       		           </el-option>
       		       </el-select>
       		     </el-form-item>
       			  <el-form-item>
       			       <el-button type="primary" @click="submitForm(formRef)">搜索</el-button>
       			       <el-button @click="resetForm(formRef)">取消</el-button>
       			     </el-form-item>
       			</el-form>
              </el-popover>
           <el-button>重置</el-button>
         </el-space>
         </el-row>
          <!--功能区域 -->
         <el-row>
          <el-space >
           <el-button type="primary" class="im-but-one">
             <plus theme="outline" size="18" fill="#fff" :strokeWidth="4"/>
             <span>上传发票</span>
           </el-button>
          </el-space>
          <div class='rt-btn-group'>
           <el-button   class='ic-btn' title='帮助文档'>
            <help theme="outline" size="16" :strokeWidth="3"/>
          </el-button>
          </div>
       </el-row>
       </div>
        <!--表单-->
        <el-row>
            <el-table :data="tableData.slice((currentPage-1)*pagesize,currentPage*pagesize)" border style="width: 100%;margin-bottom:16px;">
                <el-table-column type="selection" width="38" />
                <el-table-column prop="name"  label="店铺" sortable />
                <el-table-column prop="types"  label="国家" sortable />
                <el-table-column prop="person"  label="订单号"  sortable />
                <el-table-column prop="time"  label="图片" sortable />
				 <el-table-column prop="time"  label="名称/SKU" sortable />
                <el-table-column prop="remaks"  label="购买时间"  sortable />
                <el-table-column prop="operate"  label="操作" width="140" sortable >
                    <template #default="scope">
                        <el-button class='el-button--blue' @click="(scope.$index, scope.row)">详情</el-button>
                        <el-button class='el-button--blue' @click="(scope.$index, scope.row)">编辑</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination background   layout="total, sizes, prev, pager, next, jumper"   :total="tableData.length"
                           :page-sizes="[10, 20, 50, 100]"  :page-size="pagesize" style='margin-left:auto'
                           :current-page="currentPage"  @size-change="handleSizeChange"   @current-change="handleCurrentChange">
            </el-pagination>
        </el-row>

    </div>
</template>
<script>
    import {MenuUnfold,Plus,SettingTwo,Help,Copy,MoreOne,} from '@icon-park/vue-next';
    import { ref,reactive,onMounted } from 'vue'
    import {Search,ArrowDown,} from '@element-plus/icons-vue'
    export default{
        name: 'VAT发票',
        components:{
			MenuUnfold,
			MoreOne,
            Help,
            Search,
            Plus,
            ArrowDown
        },
        setup(){
            let markType=ref('')
            let markData = [
                    {label:'节日',value:'1'},{label:'策略',value: '2'},{label:'状态',value: '3'}
                ]
            let tableData=[
                {
                    name:'圣诞节',
                    types:'节日',
                    person:'张三',
                    time:'2020-02-14',
                    remaks:'',
                }
            ]
            let pagesize=10
            let currentPage=1
            //方法
            function handleSizeChange(size){
                pagesize = size
            }
            function handleCurrentChange(currentPage){
                currentPage = currentPage;
            }
            //数据接收
                return{
                tableData,
                pagesize,
                currentPage,
                markType,
                markData,
            }
        }

    }
</script>
<style>
    .con-header .el-row{margin-bottom:16px;}
</style>