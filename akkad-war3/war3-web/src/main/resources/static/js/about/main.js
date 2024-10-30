$(document).ready(function () {
	hljs.initHighlightingOnLoad();
	clickTreeDirectory();
	serachTree();
	pjaxLoad();
	showArticleIndex();
	switchTreeOrIndex();
	scrollToTop();
	pageScroll();
});
// 页面滚动
function pageScroll(){
    var start_hight = 0;
    $(window).on('scroll', function () {
        var end_hight = $(window).scrollTop();
        var distance = end_hight - start_hight;
		start_hight = end_hight;
		var $header = $('#header');
        if(distance > 0 && end_hight > 50){
			$header.hide();
		}else if(distance < 0){
			$header.show();
		}else{
			return false;
		}
    })
}
// 回到顶部
function scrollToTop(){
	$("#totop-toggle").on("click", function(e){
		$("html").animate({scrollTop:0},200);
	});
}
// 侧面目录
function switchTreeOrIndex(){
	$('#sidebar-toggle').on('click', function () {
		if ($('#sidebar').hasClass('on')){
			scrollOff();
		}else{
			scrollOn();
		}
	});
	$('body').click(function(e) {
		if (window.matchMedia("(max-width: 1100px)").matches) {
			var target = $(e.target);
			if(!target.is('#sidebar *')) {
				if ($('#sidebar').hasClass('on')){
					scrollOff();
				}
			}
		}
	});
	if (window.matchMedia("(min-width: 1100px)").matches) {
		scrollOn();
	}else{
		scrollOff();
	};
}

//生成文章目录
function showArticleIndex() {
	$(".article-toc").empty();
	$(".article-toc").hide();	
	$(".article-toc.active-toc").removeClass("active-toc");
	$("#tree .active").next().addClass('active-toc');
	
	var labelList = $("#article-content").children();
	var content = "<ul>";
	var max_level = 4;
	for ( var i = 0; i < labelList.length; i++ ) {
		var level = 5;
		if ( $(labelList[i]).is("h1") ) {
			level = 1;
		}else if ( $(labelList[i]).is("h2") ) {
			level = 2;
		}else if ( $(labelList[i]).is("h3") ) {
			level = 3;
		}else if ( $(labelList[i]).is("h4") ) {
			level = 4;
		}
		if(level < max_level){
			max_level = level;
		}
	}
	for ( var i = 0; i < labelList.length; i++ ) {
		var level = 0;
		if ( $(labelList[i]).is("h1") ) {
			level = 1 - max_level + 1;
		}else if ( $(labelList[i]).is("h2") ) {
			level = 2 - max_level + 1;
		}else if ( $(labelList[i]).is("h3") ) {
			level = 3 - max_level + 1;
		}else if ( $(labelList[i]).is("h4") ) {
			level = 4 - max_level + 1;
		}
		if(level != 0){
			$(labelList[i]).before('<span class="anchor" id="_label'+ i +'"></span>');
			content += '<li class="level_'+level+'"><i class="fa fa-circle" aria-hidden="true"></i><a href="#_label'+ i +'"> '+ $(labelList[i]).text() +'</a></li>';
		}
	}
	content += "</ul>"

	$(".article-toc.active-toc").append(content);
	
	if(null != $(".article-toc a") && 0 != $(".article-toc a").length){
		
		// 点击目录索引链接，动画跳转过去，不是默认闪现过去
		$(".article-toc a").on("click", function(e){
			e.preventDefault();
			// 获取当前点击的 a 标签，并前触发滚动动画往对应的位置
			var target = $(this.hash);
			$("body, html").animate(
				{'scrollTop': target.offset().top},
				500
			);
		});


		// 监听浏览器滚动条，当浏览过的标签，给他上色。
		$(window).on("scroll", function(e){
			var anchorList = $(".anchor");
			anchorList.each(function(){
				var tocLink = $('.article-toc a[href="#'+$(this).attr("id")+'"]');
				var anchorTop = $(this).offset().top;
				var windowTop = $(window).scrollTop();
				if ( anchorTop <= windowTop+100 ) {
					tocLink.addClass("read");
				}
				else {
					tocLink.removeClass("read");
				}
			});
		});	
	}
	$(".article-toc.active-toc").show();
	$(".article-toc.active-toc").children().show();
}

function pjaxLoad(){
	$(document).pjax('#menu a', '#content', {fragment:'#content', timeout:8000});
	$(document).pjax('#tree a', '#content', {fragment:'#content', timeout:8000});
	$(document).pjax('#index a', '#content', {fragment:'#content', timeout:8000});
	$(document).on({
		"pjax:complete": function(e) {
			$("pre code").each(function (i, block){
				hljs.highlightBlock(block);
			});
			// 添加 active
			$("#tree .active").removeClass("active");
			var title = $("#article-title").text().trim();
			if ( title.length ) {
				var searchResult = $("#tree li.file").find("a:contains('" + title + "')");
				if ( searchResult.length ) {
					$(".fa-minus-square-o").removeClass("fa-minus-square-o").addClass("fa-plus-square-o");
					$("#tree ul").css("display", "none");
					if ( searchResult.length > 1 ) {
						var categorie = $("#article-categories span:last a").html();
						if (typeof categorie != "undefined") {
							categorie = categorie.trim();
							searchResult = $("#tree li.directory a:contains('" + categorie + "')").siblings().find("a:contains('" + title + "')");
						}
					}
					searchResult[0].parentNode.classList.add("active");
					showActiveTree($("#tree .active"), true) 
				}
				showArticleIndex();
			}
		}
	});
}

// 搜索框输入事件
function serachTree() {
	// 解决搜索大小写问题
	jQuery.expr[':'].contains = function (a, i, m) {
		return jQuery(a).text().toUpperCase().indexOf(m[3].toUpperCase()) >= 0;
	};

	$("#search-input").on("input", function(e){
		e.preventDefault();

		// 获取 inpiut 输入框的内容
		var inputContent = e.currentTarget.value;

		// 没值就收起父目录，但是得把 active 的父目录都展开
		if ( inputContent.length === 0 ) {
			$(".fa-minus-square-o").removeClass("fa-minus-square-o").addClass("fa-plus-square-o");
			$("#tree ul").css("display", "none");
			if ( $("#tree .active").length ) {
				showActiveTree($("#tree .active"), true);
			}
			else {
				$("#tree").children().css("display", "block");
			}
		}
		// 有值就搜索，并且展开父目录
		else {
			$(".fa-plus-square-o").removeClass("fa-plus-square-o").addClass("fa-minus-square-o");
			$("#tree ul").css("display", "none");
			var searchResult = $("#tree li").find("a:contains('" + inputContent + "')");
			if ( searchResult.length ) { 
				showActiveTree(searchResult.parent(), false) 
			}
		}
	});
}

// 点击目录事件
function clickTreeDirectory() {
	// 判断有 active 的话，就递归循环把它的父目录打开
	var treeActive = $("#tree .active");
	if ( treeActive.length ) {
		showActiveTree(treeActive, true);
	}

	// 点击目录，就触发折叠动画效果
	$(document).on("click", "#tree a[class='directory']", function (e) {
		// 用来清空所有绑定的其他事件
		e.preventDefault();

		var icon = $(this).children(".fa");
		var iconIsOpen = icon.hasClass("fa-minus-square-o");
		var subTree = $(this).siblings("ul");

		icon.removeClass("fa-minus-square-o").removeClass("fa-plus-square-o");

        if (iconIsOpen) {
            if (typeof subTree != "undefined") {
                subTree.slideUp({ duration: 100 });
            }
            icon.addClass("fa-plus-square-o");
        } else {
            if (typeof subTree != "undefined") {
                subTree.slideDown({ duration: 100 });
            }
            icon.addClass("fa-minus-square-o");
        }
	});
}

// 循环递归展开父节点
function showActiveTree(jqNode, isSiblings) {
	if ( jqNode.attr("id") === "tree"  ) { return; }
	if ( jqNode.is("ul") ) {
		jqNode.css("display", "block");

		// 这个 isSiblings 是给搜索用的
		// true 就显示开同级兄弟节点
		// false 就是给搜索用的，值需要展示它自己就好了，不展示兄弟节点
		if ( isSiblings ) { 
			jqNode.siblings().css("display", "block");
			jqNode.siblings("a").css("display", "inline");
			jqNode.siblings("a").find(".fa-plus-square-o").removeClass("fa-plus-square-o").addClass("fa-minus-square-o");
		}
	}
	jqNode.each(function(){ showActiveTree($(this).parent(), isSiblings); });
}

function scrollOn(){
	var $sidebar = $('#sidebar'),
		$content = $('#content'),
		$header = $('#header'),
		$footer = $('#footer'),
		$togglei = $('#sidebar-toggle i');

	$togglei.addClass('fa-close');
	$togglei.removeClass('fa-arrow-right');
	$sidebar.addClass('on');
	$sidebar.removeClass('off');

	if (window.matchMedia("(min-width: 1100px)").matches) {
		$content.addClass('content-on');
		$content.removeClass('content-off');
		$header.addClass('header-on');
		$header.removeClass('off');
		$footer.addClass('header-on');
		$footer.removeClass('off');
	}
}
function scrollOff(){
	var $sidebar = $('#sidebar'),
		$content = $('#content'),
		$header = $('#header'),
		$footer = $('#footer'),
		$togglei = $('#sidebar-toggle i');

	$togglei.addClass('fa-arrow-right');
	$togglei.removeClass('fa-close');
	$sidebar.addClass('off');
	$sidebar.removeClass('on');

	$content.addClass('off');
	$content.removeClass('content-on');
	$header.addClass('off');
	$header.removeClass('header-on');
	$footer.addClass('off');
	$footer.removeClass('header-on');
}