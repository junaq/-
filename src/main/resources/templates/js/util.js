const router = new VueRouter({
	mode : 'history'
})


//路由转发
function goUrl(url, query) {
	router.push({
		path : url 
	});
	router.go(0);
}

// ajax 获取数据
function getAjax(url) {
	var data = null;
	$.ajax({
		url : url, // json文件路径
		async : false,
		dataType : "json",
		success : function(e) { // 成功
			data = e
		},
		error : function(e) { // 失败
			console.log('ajax加载失败')
		},
	});
	return data;
}