export function uiSchemaObj(){
     return{
		 skip_offer:{
			 'ui:title': '',
			 'ui:description': '', 
			
		 },
		 brand:{
			 items:{
			    value:{
			             "anyOf":{
			                 "ui:widget": "RadioWidget",
			                 "ui:title": "测试 OneOf object",
			             
			             },    
							 
			        },
				 }
		 },
	 }
}