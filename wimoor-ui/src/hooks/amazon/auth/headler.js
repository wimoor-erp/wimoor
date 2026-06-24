
		
		function getgroupData(){
				let arr=[]
				gensearchApi.getStoreList().then((res)=>{
					storeList.value = res.data;
				})
			}