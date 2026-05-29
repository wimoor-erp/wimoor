export function schemaTran(data,schema){
	if(data.items.properties.language_tag){
	    data.items.properties.language_tag =schema.$defs.language_tag.default; 
	}
	if(data.items.properties.marketplace_id){
	    data.items.properties.marketplace_id =schema.$defs.marketplace_id.default; 
	}
	
   //把外层array类型变为object
		 data.type="object"
		 const {items,...rest} = data;
		 data = {
			  ...rest,
			 properties:{
				 items:items
			 }
		 } 
		 
		 console.log(data)
	
   return data
}