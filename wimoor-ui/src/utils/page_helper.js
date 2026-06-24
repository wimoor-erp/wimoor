export function closeTab(emitter){
	emitter.emit("removeTab", 100);
}

export function redirectToList(router,m_path,m_name){
	var refreshvalue="";
	if(router.currentRoute.value.query.replace){
		refreshvalue=true;
	}
	router.push({
		path:m_path,
		query:{
			title:m_name,
			path:m_path,
			replace:true,
			refresh:refreshvalue,
		},
	})
}
export function redirectToEdit(router,m_path,m_name){
	router.push({
		path:m_path,
		query:{
			title:m_name,
			path:m_path,
		},
	})
}
export function redirectToDetail(router,m_path,m_name){
	router.push({
		path:m_path,
		query:{
			title:m_name,
			path:m_path,
			replace:true,
		},
	})
}