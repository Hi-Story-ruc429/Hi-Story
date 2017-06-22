
//var basePath = 'http://localhost:8080/AIWEB16';
var Transcripts = Transcripts || {};
var content = content||{};
$(function(){
    //Transcripts.addFileList();

	
	content._seach_suggest = $('#seach_suggest');
	content._seach_message = $('#seach_message');
	content._seach_content = $('#seach_content');
	content._nav = $('#nav');
	content._adv = $('#adv');
	content._adv1 = $('#adv1');
	content._ad1 = $('#ad1');
	content._ad2 = $('#ad2');
	content._ev1 = $('#ev1');
//	$('#adv').empty();
	
	var keycode= $.cookie('key');
	 $('#user_input').focus();
	 if(keycode){
		 $('#user_input').val(keycode);
		 $.cookie('queryHistary', keycode);
		 Transcripts.cleanContent();
			$('#seach_content').empty();
       	$('#seach_content').html("<p>Please <b>press enter</b> or <b>click search button</b> to get search results!</p>");
	 }
	 $.cookie('key', "");
	 
	var history_content = $.cookie('queryHistary');
	if(history_content){
		 $('#user_input').val(history_content);
		 Transcripts.sendQuest();
	}else{
		window.location.href = "./index.html";
	}

//
//	 var queryHis= $.cookie('queryHistary');
//	 if(!queryHis){
//	 	alert('meiyou');
//	 	
//		 Transcripts.cleanContent ();
//	 }else{
//		 alert('you');
//		 alert(queryHis);
//	 	$('#user_input').val(queryHis);
//	 }
//
	 $("#search_btn").click(function () {
     	Transcripts.sendQuest();
   	});
//	 $('#user_input').val("test");
	document.onkeydown = function(event){
	//����Enter�����¼�		
		if(event.keyCode == 13){	
			Transcripts.sendQuest();
		     return false;
		 }	
	}


	
});
Transcripts.cleanContent = function(){
	content._seach_content.empty();
	content._seach_message.empty();
	content._seach_suggest.empty();
	content._nav.empty();
	content._ev1.empty();
	content._nav.empty();
	content._adv.empty();
	content._adv1.empty();
	content._ad1.empty();
	content._ad2.empty();
};
Transcripts.sendQuest = function(){
	var search_content = $.trim($('#user_input').val());
	if(search_content){
		$.cookie('queryHistary', search_content);
		var start = 0;
		var rows = 10;
		Transcripts.getResultList(search_content,start,rows);
	}
};

Transcripts.getResultList = function(query, start, rows){
	
	var parm ={
			query_string:query,
			start:start,
			rows:rows
	};
	 $.ajax("./my_query", {
         type: "POST",
         dataType: "json",
         data:parm||{},  
         success: function (resultInfo) {        	
             if (resultInfo.result == "fail"){
//            	 alert("��ȡ�ļ�ʧ�ܣ����Ժ����ԣ�");
             }else if (resultInfo.result == "success") {
            	
                initBottomIndex(start + 1,resultInfo.resHead.numFoound);
                if(resultInfo.resHead.numFoound > 0){
//                	$('#adv').empty();
                	Transcripts.addviewList(resultInfo);
                }else{
                	$('#seach_suggest').empty();                	
                	$('#seach_content').empty();
                	$('#seach_content').html("<p>It's a pity, there is <font color='red'>no results</font>, please try another query!</p>");
//                	$('#adv').empty();
                }              
          		$('#seach_message').empty();
    			var resultNum = resultInfo.resHead.numFoound;
    			var costTime =  resultInfo.resHead.QTime;
    			var message_html = ""
            	 if(start == 0){
            		 $('#seach_suggest').empty();
            		 message_html += "<div class='col-xs-12'><i>Get about "+resultNum+" Results, cost "+costTime+" miliseconds.</i></div><hr>";
                 }else{
                	 $('#seach_suggest').empty();
        			message_html += "<div class='col-xs-12'><i>Get about "+resultNum+" Results, this is "+parseInt(start + 1)+" page (cost "+costTime+" miliseconds).</i></div><hr>";
                 }
         		$('#seach_message').html(message_html);
         		$('#seach_suggest').empty();
         		var seach_suggest_html = "<br>";
         		if(resultInfo.suggest_string){
         			$('#seach_suggest').empty();
         			seach_suggest_html+= "<div class='col-xs-12'><i><font color='red'>Maybe you want to search:</font>" +
         					"<a href='javascript:void(0);' id ='suggest_content'>"+" " +resultInfo.suggest_string+"</a></i></div>";
         		}
         		$('#seach_suggest').html(seach_suggest_html);
         		$('#suggest_content').click(function(){
         			var search_content =resultInfo.suggest_string;
         			$('#user_input').val(search_content);
         			sendQuest();
         		});
             }
          },   
         error: function (request, textStatus, errorThrown) {
//        	 
//         	alert("����������,���Ժ����ԣ�");
//         	alert(errorThrown);
         }
     });
	//book query
	 $.ajax("./my_book", {
         type: "POST",
         dataType: "json",
         data:parm||{},  
         success: function (resultInfo) {        	
             if (resultInfo.result == "fail"){
//            	 alert("��ȡ�ļ�ʧ�ܣ����Ժ����ԣ�");
             }else if (resultInfo.result == "success") {            	 
                if(resultInfo.resHead.numFoound > 0){
//                	var book_html = $('#adv').html();
                	$('#adv1').empty();
//                	book_html += showbook(resultInfo);
                	$('#adv1').html(showbook(resultInfo));
                }
             }
          },   
         error: function (request, textStatus, errorThrown) {
         }
     });
};

Transcripts.addviewList = function(resultInfo){
	//$('#seach_suggest').empty();
	//��ʾ��ѯͷ����Ϣ

	$('#seach_content').empty();
	var content_html = getContent(resultInfo.res);
	$('#seach_content').html(content_html);	
	$('#adv').empty();
	//var ads_evaluation_html = refreshAdsAndEvaluation(resultInfo);
	//$('#adv').html(ads_evaluation_html);
//	var info_html = $('#adv').html();
//	info_html += getPeople(resultInfo.infobox);
	$('#adv').html(getPeople(resultInfo.infobox));
	
};

function getPeople(infobox){
	
	var obj = eval("("+infobox+")");
	var html = "";
//	alert("obj:"+obj);
	for(var from in obj){
	var name = obj[from]["infobox"]["中文名"][0];
//	alert("name"+name);
	oo = obj[from]["infobox"];
	//window.alert(name);
	html += "<div class='panel panel-default'>"+
    "<div class='panel-heading'>"+name+"</div>"+
    "<div class='panel-body' style='text-align: left;'>";
	html += '<table border="1" width="100%">';
	for(var key in oo){
		html += '<tr>'
        html += '<td width="30%">' + key + '</td>';
        html += '<td width="70%">' + oo[key]+ '</td>';
        html += '</tr>'
		
	}
	html += '</table>';
	html += "</div></div>";
	}
    return html;
}

function showbook(resultInfo){
	var html_ads = "";
	var resultNum = resultInfo.resHead.numFoound;
	html_ads +="<div class='panel panel-default'>"+
    "<div class='panel-heading'>相关书籍:</div>"+
    "<div class='panel-body'>";
//	html_ads += "<div class='col-xs-12'><i>Get about "+resultNum+".</i></div><hr>";
	for(var i = 0; i < 10; i++){
		html_ads += "<a href=http://"+resultInfo.res[i].url+" target='_blank'><p>"+resultInfo.res[i].title +"</p></a>";
	}
	html_ads += "<a href='http://www.zh5000.com/ZHJD/gxjd/' target='_blank'><p>more >>></p></a>";
    html_ads += "</div></div>";
    return html_ads;
}

function getContent(resultList){
	var html = "";
	for(var i = 0; i < resultList.length; i++){
		html += "<div class='row'><div class='col-xs-12'>"+
        "<h4><a href=http://"+resultList[i].url+" target='_blank' ><font color='#1a0dab'>"+resultList[i].title +"</font></a></h4>" +
        "<p>"+resultList[i].content +"...</p>"+
        "<ul class='list-inline'>"+
        "<li><a href=http://"+resultList[i].url+" target='_blank' ><font color='#006621'>"+resultList[i].url +"</font></a></li>"+
        "<li><a href='javascript:void(0);' target='_blank' ><font color='#666'><u>"+resultList[i].id+"</u></font></a></li>"+
//         "<p class='pull-right'>"+
//             "<span id = '"+resultList[i].id+"' class='label label-default show_id'>"+resultList[i].id+"</span>"+
//         "</p>" +
         "</ul> </div></div>";
	}
	return html;
};
function initBottomIndex(startIndex, rowSize){
	$('#nav').empty();
	if(rowSize == 0){
		return;
	}
	var totlePage = parseInt(rowSize/10) + 1;
//	alert(totlePage);
    var html = "";
	html += "<hr><nav><ul class='pagination'>";
   	if(startIndex != 1){
		html += "<li><a href='#' aria-label='Previous'>" +
				"<span aria-hidden='true' class='indexP'>Previous</span></a></li>";
	}
   	//��ǰ����
   	var up_num = 0;
   	var begin = startIndex - 5;
   	if(begin < 1){
   		begin = 1;
   	}
   	for(var i = begin; i < startIndex; i++){
	    html+="<li class='index' id = '"+i +"'><a href='#'>"+i+"</a></li>";
	    up_num++;
   	}
   	//�������
   	for(var i = startIndex; i < startIndex + 11 - up_num && i < totlePage; i++){
   		if(i == startIndex){
   		    html+="<li class='index active' id = '"+i +"'><a href='#'>"+i+"</a></li>";
   		}else{
   		    html+="<li class='index ' id = '"+i +"'><a href='#'>"+i+"</a></li>";
   		}
   	}
    html+="<li>"+
               "<a href='#' aria-label='Next'><span aria-hidden='true' class ='indexN'>Next</span>" +
               "</a></li></ul></nav>";
	$('#nav').html(html);
	bindIndexEvent();
	bindNextIndexEvent(rowSize);
	bindPreviousIndexEvent(rowSize);
};
function bindIndexEvent(){
	
	$(".index").click(function(){
		clearActive();
		var message_index = parseInt($(this).attr('id')) - 1;
		var search_content = $.cookie('queryHistary');
		$('#user_input').val(search_content);
		if(search_content){		
			var start = message_index;
			var rows = 10;
			Transcripts.getResultList(search_content,start,rows);
		}        
	});
}
function bindNextIndexEvent(rowSize){
	
	$(".indexP").click(function(){
		previousActive(rowSize);
	});
}
function bindPreviousIndexEvent(rowSize){
	$(".indexN").click(function(){
		nextActive(rowSize);
	});
}
function clearActive(){
	var index = $(".index");
	for(var i = 0; i < index.length; i++){
		$($(".index")[i]).removeClass('active');
	}
}
function nextActive(rowSize){
	var index = $(".index");
	for(var i = 0; i < index.length; i++){
		if($($(".index")[i]).hasClass('active')){
			$($(".index")[i]).removeClass('active');
			var search_content = $.cookie('queryHistary');
			$('#user_input').val(search_content);
			if(i + 1 >= index.length){
				
				var message_index = parseInt($($(".index")[i]).attr('id'));				
				Transcripts.getResultList(search_content,message_index,10);
				break;
			}else{
				var message_index = parseInt($($(".index")[i + 1]).attr('id')) - 1;
				Transcripts.getResultList(search_content,message_index,10);
				break;
			}
		}
	
	}
};
function previousActive(rowSize){
	var index = $(".index");
	for(var i = 0; i < index.length; i++){
		if($($(".index")[i]).hasClass('active')){
			$($(".index")[i]).removeClass('active');
			var search_content = $.cookie('queryHistary');
			$('#user_input').val(search_content);
			if(i - 1 < 0){			
				var message_index = parseInt($($(".index")[0]).attr('id')) -1;				
				Transcripts.getResultList(search_content,message_index,10);
				break;
			}else{
				var message_index = parseInt($($(".index")[i - 1]).attr('id')) - 1;
				Transcripts.getResultList(search_content,message_index,10);
				break;
			}
		}
	
	}
}

Transcripts.addFileList = function(){
	var parm ={
			isupload:0
	};
	 $.ajax("./getfileList", {
         type: "POST",
         dataType: "json",
         data:parm||{},  
         success: function (resultInfo) {        	
             if (resultInfo.result == "fail"){
            	 $('#fileList').empty();
             }else if (resultInfo.result == "success") {
            	 Transcripts.AddFileListToView(resultInfo.fileList);
             }else if(resultInfo.result == "empty"){
            	 Transcripts.AddFileListToView(resultInfo.fileList);
             }
          },   
         error: function (request, textStatus, errorThrown) {
//         	alert("����������");
         }
     });
		
};

Transcripts.AddFileListToView = function(fileList){
	//alert(fileList);
	 $('#fileList').empty();
	 var fileTable= " <table id = 'myfileList' width='98%'  border='1px' border-radius='5px' class = 'tc faw ft14' style = 'margin-top:15px; margin-left:9px;' >"
	 fileTable += "<tr style = 'background:#19a569; color:white;border:2px;line-height:40px;'><td >���</td><td >�ļ�����</td><td >�ϴ�ʱ��</td><td>�ļ���С</td><td >�ϴ���ʦ</td><td>���ش���</td><td >�ļ�����</td></tr>";
	 if(fileList.length != 0){
		 var fileMessage = fileList.split("+");
		 for(var i = 0; i < fileMessage.length; i++){
			 var fileItem = fileMessage[i].split("|");
			 var fileId = fileItem[0]
			 var fileName = fileItem[1];
			 var fileSize = parseInt(fileItem[2]);
			 var fileSizeUnit  = "�ֽ�";
			 if(fileSize > 1024){
				 fileSize = parseInt((parseInt(fileItem[2])*1.0/1024)*10)/10;
				 fileSizeUnit = "KB";
			 }
			 if(fileSize > 1024){
				 fileSize = parseInt((parseInt(fileItem[2])*1.0/(1024*1024))*10)/10;
				 fileSizeUnit = "MB";
			 }
			 var fileTime = fileItem[3];
			 var fileTeacher = fileItem[4];
			 var fileDownTime = fileItem[5];
			 if(i % 2 == 0){
				 fileTable += "<tr style = 'line-height:40px;background:#EFEFEF' id = '" +
				 	fileId+ "'><td><a href='javascript:void(0);'>" + fileId+
				 	"</a></td><td><a href='javascript:void(0);'>" +fileName +" </a></td><td><a href='javascript:void(0);'>" +
				 	fileTime+"</a></td><td><a href='javascript:void(0);'>"+fileSize +fileSizeUnit+"</a></td><td><a href='javascript:void(0);'>" +fileTeacher +
				 	"</a></td><td><a href='javascript:void(0);'>" +fileDownTime+"��</a></td><td><a class = 'downbutton' href='./downFile?fileid=" + fileId + 
				 	"'>����</a></td></tr>";	
			 }else{
				 fileTable += "<tr style = 'line-height:40px;' id = '" +
				 	fileId+ "'><td><a href='javascript:void(0);'>" + fileId+
				 	"</a></td><td><a href='javascript:void(0);'>" +fileName +" </a></td><td><a href='javascript:void(0);'>" +
				 	fileTime+"</a></td><td><a href='javascript:void(0);'>"+fileSize +fileSizeUnit+"</a></td><td><a href='javascript:void(0);'>" +fileTeacher +
				 	"</a></td><td><a href='javascript:void(0);'>" +fileDownTime+"��</a></td><td><a class = 'downbutton' href='./downFile?fileid=" + fileId + 
				 	"'>����</a></td></tr>";	
			 }

		 }
	 }
	 fileTable+="</table>";
	$('#fileList').html(fileTable);
	
}


