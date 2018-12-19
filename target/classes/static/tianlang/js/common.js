$(function(){
	loadTheme();
	nav();
	footer();
});

/**
 * 加载主题
 */
function loadTheme(){
	//根据cookie name 获取 value
	var value = getCookieValue("theme");
	//当cookies中没有主题值时，为主题赋初值
	if (value.length == 0) {
		value = "white";
	}
	//判断主题值是图片还是颜色，并为body更换背景
	if (value == "white") {
		$("body").css("background-color",value);
		$("body").css("color","black");
	}else if (value.indexOf("_")!=-1) {
		value = value.replace("_",'#');
		$("body").css("background-color",value);
		$("body").css("color","black");
	}else if (value == "tectile" || value == "cloth") {
		$("body").css("background-image","url('/images/theme/"+value+".png')");
		$("body").css("color","white");
	} else {
		$("body").css("background-image","url('/images/theme/"+value+".jpg')");
		$("body").css("color","white");
	}
	
}

/**
 * 修改主题
 * @param value
 */
function changeToTheme (value) {
	//获取当前时间
	var date = new Date();
	var expiresDays = 36500;//单位：天
	//将date设置为expiresDays天以后的时间
	date.setTime(date.getTime() + expiresDays*24*3600*1000);
	var time = date.toGMTString();
	//修改cookies
	document.cookie = "theme=" + value + "; expires=" + time;
	
	if (value == "white") {
		$("body").css("background-image","none");
		$("body").css("background-color",value);
		$("body").css("color","black");
	}else if (value.indexOf("_")!=-1) {
		value = value.replace("_",'#');
		$("body").css("background-image","none");
		$("body").css("background-color",value);
	}else if (value == "tectile" || value == "cloth") {
		$("body").css("background-color","none");
		$("body").css("background-image","url('/images/theme/"+value+".png')");
		$("body").css("color","white");
	} else {
		$("body").css("background-color","none");
		$("body").css("background-image","url('/images/theme/"+value+".jpg')");
		$("body").css("color","white");
	}
}

/**
 * 加载字体
 */
function loadSize(){
	//根据cookie name 获取 value
	var size = getCookieValue("size");
	//当cookies中没有主题值时，为主题赋初值
	if (size.length == 0) {
		size = "22";
	}
	$(".content").css("font-size", size+"px");
	$("#size").empty();
	$("#size").append(size);
}

/**
 * 修改字体大小
 * @param value
 */
function changeFontSize (value) {
	//获取当前时间
	var date = new Date();
	var expiresDays = 36500;//单位：天
	//将date设置为expiresDays天以后的时间
	date.setTime(date.getTime() + expiresDays*24*3600*1000);
	var time = date.toGMTString();
	
	var size = $("#size").text();
	if(value == "-"){
		if(size > 12){
			size = parseInt(size) - 1;
		}
	}
	
	if(value == "+"){
		if(size < 40){
			size = parseInt(size) + 1;
		}
	}
	//修改cookies
	document.cookie = "size=" + size + "; expires=" + time;
	$(".content").css("font-size", size+"px");
	$("#size").empty();
	$("#size").append(size);
}

/**
 * 加载背景颜色
 */
function loadColor(){
	//清空主题图片
	$("body").css("background-image","none");
	//根据cookie name 获取 value
	var color = getCookieValue("color");
	//当cookies中没有主题值时，为主题赋初值
	if (color.length == 0) {
		color = "ffffff";
		$("body").css("color","black");
	} else {
		if (color == "333333") {
			$("body").css("color","white");
		} else {
			$("body").css("color","black");
		}
	}
	$("body").css("background-color","#"+color);
}

/**
 * 修改背景颜色
 * @param value
 */
function changeColor (value) {
	//获取当前时间
	var date = new Date();
	var expiresDays = 36500;//单位：天
	//将date设置为expiresDays天以后的时间
	date.setTime(date.getTime() + expiresDays*24*3600*1000);
	var time = date.toGMTString();
	
	//修改cookies
	document.cookie = "color=" + value + "; expires=" + time;
	if (value == "333333") {
		$("body").css("color","white");
	}else {
		$("body").css("color","black");
	}
	$("body").css("background-color","#"+value);
}


/**
 * 使用方法，在html中加入下面的标签，
 * 加入<script type="text/javascript">$(function(){nav()});</script>
 * <!-- 导航 -->
 * <div id="nav" class="navbar navbar-inverse" role="navigation"></div>
 **/
function nav () {
	$("#nav").append(
		"<div class='container-fluid'>" +
			"<div class='navbar-header'>" +
				"<button type='button' class='navbar-toggle' data-toggle='collapse' data-target='#example-navbar-collapse'>" +
					"<span class='sr-only'>切换导航</span>" +
					"<span class='icon-bar'></span>" +
					"<span class='icon-bar'></span>" +
					"<span class='icon-bar'></span>" +
				"</button>" +
			"</div>" +
			"<div class='collapse navbar-collapse' id='example-navbar-collapse'>" +
				"<ul class='nav navbar-nav'>" +
					"<li id='nav_home' class='active'><a href='/'><span class='glyphicon glyphicon-home'></span>首页</a></li>" +
					"<li id='nav_book'><a href='/client/book'><span class='glyphicon glyphicon-book'></span>文库</a></li>" +
//					"<li id='nav_know'><a href='/client/know'><span class='glyphicon glyphicon-tasks'></span>社区</a></li>" +
					"<li id='nav_message'><a href='/client/message'><span class='glyphicon glyphicon-envelope'></span>留言</a></li>" +
					"<li id='nav_setup' class='dropdown'>" +
						"<a href='#' class='dropdown-toggle' data-toggle='dropdown'>" +
							"<span class='glyphicon glyphicon-cog'></span>设置" +
						"</a>" +
						"<ul class='dropdown-menu'>" +
							"<li><a href='/client/changeTheme'>更换主题</a></li>" +
							"<li class='divider'></li>" +
							"<li><a href='/manage/index'>管理员入口</a></li>" +
						"</ul>" +
					"</li>" +
				"</ul>" +
			"</div>"+
		"</div>");
}

/**
 * 使用方法，在html中加入下面的标签，
 * 加入<script type="text/javascript">$(function(){footer()});</script>
 * <!--footer--->
 * <div id="footer" class="footer text-center"></div>
 **/
function footer () {
	$("#footer").append(
		"<div class='container'>"+
			"<div class='footer-grids'>"+
				"<div class='col-md-6 footer-text'>"+  
					"<h3>关于我</h3>"+
					"<p>本网站是我学习的记录，有一些比较好用的技能</p>"+
					"<p>我将这些技能记录了下来，留作备用，并且分享给大家</p>"+
					"<p>如果本站侵犯了您的权益请联系本站，本站会立即删除</p>"+
					"<h3>导航：<span style='color: white;font-size: 20px;'>"+
						"<a class='label label-primary' href='/'>首页</a>"+
						"<a class='label label-primary' href='/client/book'>文库</a>"+
						"<a class='label label-primary' href='/client/know'>社区</a>"+
					"</span></h3>"+
				"</div>"+
				"<div class='col-md-6 footer-info'>"+
					"<h3>联系我</h3>"+
					"<p>如果您发现本网站没有，您恰好有的资源，请您邮件我，从而达到资源共享，最后每个人都能找到自己需要的东西，并且本网站也会把您记录下来，在页面展示您在什么时间，共享了什么资源！</p>"+
					"<div class='support'>"+
					   "<h3>备案：<span style='color: white;font-size: 16px;'>京ICP备18041428号-1</span></h3>"+
					   "<h3>邮箱：<span style='color: white;font-size: 18px;'>suzengxin@foxmail.com</span></h3>"+
					"</div>"+
				"</div>"+
				"<div class='clearfix'> </div>"+
			"</div>"+
		"</div>");
}

/**
 * 根据cookie name获取value
 **/
function getCookieValue(cookieName){
	if (document.cookie.length>0){
		c_start=document.cookie.indexOf(cookieName + "=")
		if (c_start!=-1){ 
			c_start=c_start + cookieName.length+1 
			c_end=document.cookie.indexOf(";",c_start)
		if (c_end==-1) c_end=document.cookie.length
			return unescape(document.cookie.substring(c_start,c_end))
		} 
	}
	return ""
}

