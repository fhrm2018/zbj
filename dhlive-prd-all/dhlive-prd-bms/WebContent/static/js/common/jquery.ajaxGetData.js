/*!
 * Ajax Get Data
 * version: 2.0-2017.04.18
 * Requires jQuery v1.7 or later
 * Copyright (c) 2017
 */

jQuery.ajaxGetData = function(options) {
	var settings = $.extend({
		"ajaxUrl": "", //请求url
		"pageIndex": 1, //当前要显示的页
		"spanInterval": 4, //当前页两侧显示页数
		"recordsNum": 15, //每页显示记录数
		"container": "#showrecords", //数据显示容器
		"pager": "#showpager", //页数显示容器
		"clickStyle": true, //第一次绑定进入true,内部使用false
		"fnData": function(data){
				return data;
			}, //处理函数
		"postData": {}, //提交的参数，需要提前定义
		"doneFn": function(){}, //分页数据加载完后执行
		"headtype": 1, //1显示（无值时不显示）
		"pagershow": 1 //是否总是显示分页，1显示
	}, options);
	
	if(!chkinteger(settings.pageIndex)){
		return false;	
	}
	
	var $cssbox = $(settings.container),
		$loading = $cssbox.siblings('.loading').removeClass('hide'),
		$nodata  = $cssbox.siblings('.nodata').addClass('hide'),
		$container = $(settings.container).addClass('hide'),
		$pager = $(settings.pager),
		$postData = settings.postData;

	if(!$loading.length){
		$loading = $('<div class="loading ptb15 mt10"></div>').insertAfter($cssbox);
	}
	if(!$nodata.length){
		$nodata = $('<div class="nodata hide ptb15 ac mt10"></div>').insertAfter($cssbox);
	}
	//if(settings.headtype != 1)var $table = $container.parent().addClass('hide'); //无值时 整个表格才需要隐藏;
	$postData.page = settings.pageIndex;
	$postData.rows = settings.recordsNum;
	$.ajax({
		url: settings.ajaxUrl,
		data: $postData,
		type: "post",
		dataType: "json",
		success: function (data) {
			//var data= {"code": 1000, "data":{"total":120, "rows": [{},{},{},{}]}};
			$loading.addClass('hide');
			//$pager.addClass('hide');	// 分页容器
			if(data.code == '1000'){
				var objs = data.data;
				/*try{
					var details = jQuery.parseJSON(data.detail);
				}catch(e){
					console.log(data.detail);
					return false;
				}*/
				//if(!details)return false;
				var intPageIndex = parseInt(settings.pageIndex);
				var total = objs.total; //总记录数
				if(total == 0){ //没有数据
					$pager.addClass('hide');
					$container.empty();
					settings.doneFn();
					$nodata.removeClass('hide').html('没有查询到相关数据！');
					return false;
				};
				
				//将总记录数结果 得到 总页数
				var pageS = total;
				if (pageS % settings.recordsNum == 0){
					pageS = pageS / settings.recordsNum;
				}else{
					pageS = parseInt(total / settings.recordsNum) + 1;
				}
				
				if (intPageIndex > pageS) { // 请求页数超出实际总页数
//					$container.removeClass('hide');
//					///if(settings.headtype != 1)$table.removeClass('hide');
//					popLayer("页数发生错误！");
//					return false;
					//$pager.addClass('hide');
					$container.empty();
					settings.doneFn();
					$nodata.removeClass('hide').html('没有查询到相关数据！');
					return false;
				}

				// 获取显示数据的表格

				$container.removeClass('hide').empty();
				//if(settings.headtype != 1)$table.removeClass('hide');
				
				//append 参数为 数据，当前页数，每页记录数，总页数
				if(objs.rows){
					var listrecords = objs.rows;
				}else if(objs.repayments){
					var listrecords = objs.repayments;
				}
				$container.append(settings.fnData(listrecords, intPageIndex, settings.recordsNum, pageS, total));
				$container.find("tr:odd").addClass("bgcolor");
				
				//如果有设置pagershow为0则不显示分页
				if(settings.pagershow == 0){
					//$pager.addClass('hide');
				}else{
					$pager.removeClass('hide');
					// 首页
//					var $bnsFirst = $pager.find(".first");
//					if (intPageIndex == 1)
//						$bnsFirst.removeData('page').addClass("disabled");
//					else {
//						$bnsFirst.data('page', 1).removeClass("disabled");
//					}
		
					// 设置页数 页码
					//$pager.find(".curpage").html(intPageIndex);
					//$pager.find(".allpages").html(pageS);
					$pager.find(".allrecords").html(total);
					$pager.find(".eachrecords").html(settings.recordsNum);	//objs.rows.length
		
					// 上一页
					var $bnsPrev = $pager.find(".prev");
					if (intPageIndex == 1){
						$bnsPrev.removeData('page').addClass("disabled");
					} else {
						$bnsPrev.data('page', intPageIndex - 1).removeClass("disabled");
					}
		
					// 下一页
					var $bnsNext = $pager.find(".next");
					if (intPageIndex == pageS) {
						$bnsNext.removeData('page').addClass("disabled");
					} else {
						$bnsNext.data('page', intPageIndex + 1).removeClass("disabled");
					}
		
					// 尾页
//					var $bnsLast = $pager.find(".last");
//					if (intPageIndex == pageS){
//						$bnsLast.removeData('page').addClass("disabled");
//					} else {
//						$bnsLast.data('page', pageS).removeClass("disabled");
//					};

					//设置分页的格式
					var interval 	= 	parseInt(settings.spanInterval), //设置间隔
						pageHtml 	=	'',
						start 		= 	Math.max(1, intPageIndex - interval), //设置起始页
						end 		= 	Math.min(intPageIndex + interval, pageS); //设置末页
	
					if (intPageIndex < interval + 1) {
						end = (2 * interval + 1) > pageS ? pageS : (2 * interval + 1);
					}
					if ((intPageIndex + interval) > pageS) {
						start = (pageS - 2 * interval) < 1 ? 1 : (pageS - 2 * interval);
					}
					
					//生成中间页码
//					for (var j = start; j < end + 1; j++) {
//						if (j == intPageIndex) {
//							pageHtml += '<a href="" onclick="return !1;" class="current">' + j + '</em>';
//						}else {
//							pageHtml += '<a href="" onclick="return !1;" data-page="' + j + '">' + j + '</a>';
//						}
//					} //for
					pageHtml += '<a href="" onclick="return !1;">' + intPageIndex + '</em>';
					$pager.find(".eachItem").html(pageHtml);
	
		
					// 设置当前页
					//$pager.find(".keypageipt").val(intPageIndex);
		
					var goRecordsPage = function(pg) {
						if(pg){
							$.ajaxGetData({
								"ajaxUrl": settings.ajaxUrl,
								"pageIndex": pg,
								"spanInterval": settings.spanInterval,
								"recordsNum": settings.recordsNum,
								"container": settings.container,
								"pager": settings.pager,
								"clickStyle": false,
								"fnData": settings.fnData,
								"postData": settings.postData,
								"doneFn": settings.doneFn,
								"headtype": settings.headtype,
								"pagershow": settings.pagershow
							});
						};
					};
					
					// 统一绑定委派事件
					if (settings.clickStyle) {
						$pager.off("click."+ settings.pager)	 /* 先解除绑定  */
							  .on("click."+ settings.pager, "a", function(){
							goRecordsPage($(this).data("page"));
						});
						$pager.off("click."+ settings.pager)	 /* 先解除绑定  */
							  .on("click."+ settings.pager, ".writeBtn", function(){
							goRecordsPage($(this).data("page"));
						});
						$pager.find(".keypageipt").off("keydown." + settings.pager)
												  .on("keydown." + settings.pager, function(event) {
							var keycode = event.keyCode;
							if (keycode == 13) {
								event.preventDefault();
								goRecordsPage($(this).val());
							}
						});
						$pager.find(".btn").off("click." + settings.pager)
											  .on("click." + settings.pager, function() {
							goRecordsPage($(this).prev().val());
						});
					};
				
				}
				settings.doneFn(data);
			}else if(data.code == '2001'){
				top.location.href = 'login.html';
			}else{
				$container.addClass('hide');
				$nodata.removeClass('hide').html(data.message);
				//if(settings.headtype != 1)$table.removeClass('hide');
				settings.doneFn();
			}
		}, //sucess
		statusCode : {
			404 : function() {
				$loading.addClass('hide');
				//$pager.addClass('hide');
				$nodata.removeClass('hide').html(stringMsg.serverErr);
				//if(settings.headtype != 1)$table.removeClass('hide');
			},
			400 : function() {
				$loading.addClass('hide');
				//$pager.addClass('hide');
				$nodata.removeClass('hide').html(stringMsg.serverErr);
				//if(settings.headtype != 1)$table.removeClass('hide');
			}
		},
		error:function(data){
			$loading.addClass('hide');
			//$pager.addClass('hide');
			$nodata.removeClass('hide').html(stringMsg.serverErr);
			//if(settings.headtype != 1)$table.removeClass('hide');
			settings.doneFn();
		}
	
	}); //ajax

};   //function