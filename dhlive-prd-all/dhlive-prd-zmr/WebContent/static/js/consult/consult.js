;$(function () {
	/****查询快讯***/
	selFastConsult();
});

/****查询快讯***/
function selFastConsult(){
	/*var jqxhr= $.ajax({
        url: ctx+"/****",
        data: {},
    });
 	 jqxhr.done(function (data) {
       if (data.code == '1000') {
    	 var msgList = data.data;
    	 if(msgList && msgList.length>0){
    		  
    	 }
       }
    });*/
 	 var htmls='';
 	 for(var i=0;i<10;i++){
 		htmls+=appendConsult('11:26 11/28','打了七折！软银对Uber估值值480亿美元,值值480亿美元,值值480亿美元,值值480亿美元');
 	 }
 	 if(htmls!=''){
 		$(".consultContent ul").append(htmls);
 	 }
}

function appendConsult(consultTime,content){
	var html='<li>';
		    html+='<div class="text">';
		     html+='<span><i></i>';
		     html+=consultTime;
		     html+='</span><p>';
		     html+=content;
		     html+='</p>';
		    html+='</div>';
	    html+='</li>';
	    return html;
} 




