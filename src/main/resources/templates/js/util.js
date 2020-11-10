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
function getAjax(url,type) {
	var data = null;
	$.ajax({
		url : url, // json文件路径
		type: type,
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

function getUrlKey (name) {
    return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.href) || [, ""])[1].replace(/\+/g, '%20')) || null
}

//获取cookie、
  function getCookie(name) {
var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
if (arr = document.cookie.match(reg))
  return (arr[2]);
else
  return null;
}

//设置cookie,增加到vue实例方便全局调用
  function setCookie (c_name, value, expiredays) {
var exdate = new Date();
exdate.setDate(exdate.getDate() + expiredays);
document.cookie = c_name + "=" + escape(value) + ((expiredays == null) ? "" : ";expires=" + exdate.toGMTString());
}

//删除cookie
  function delCookie (name) {
var exp = new Date();
exp.setTime(exp.getTime() - 1);
var cval = getCookie(name);
if (cval != null)
 document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString();
}