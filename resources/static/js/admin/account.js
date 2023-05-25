$(document).ready(function(){
	$(document).on('click','.searchAccount', function(){
		$('#listSearch').attr('action', '/account/list').submit();
	});
	
	$(document).on('click','.deleteAccount', function(){
		var id = $(this).attr('data-id');
		$.ajax({
			type:'post',
			url:'/account/delete',
			data:{id:id},
			success:function(data){
				if(data.result === 1){
					alert('삭제하였습니다.');
					location.href = '/account/list';
				}else{
					alert('다시 시도해 주세요.');
				}
			}
		});
	});
	
	$(document).on('click','.selectedCategory', function(){
		var $type = $(this).attr('data-type');
		var array = [];
		
		$('.role_'+$type).find('.selectedId').each(function(i){
			array.push($(this).val());
		});
		
		console.log('array', array);
		
		var htm = '';
		$('.checkCategory').each(function(i){
			var $this = $(this);
			if($this.is(":checked")){
				var id = $this.attr("data-id");
				var title = $this.attr("data-name");
				console.log('id', id);
				var temp = $.grep(array, function(n, i){
					return (n == id);
				});
				
				console.log('temp', temp.length);
				
				if(temp.length == 0){
					htm += '<div class="dotLineBox">';
					htm += '  <p class="ModiCategory">'+ title +'</p>';
					htm += '  <input type="hidden" class="selectedId" value="'+ id +'"/> ';
					htm += '  <p class="closeBtnWrap">';
					htm += '    <button type="button" class="closeBtn"></button>';
					htm += '  </p>';
					htm += '</div>';
				}
			}
		});
		var $length = $('.role_'+$type+' .dotLineBox').length;
		
		console.log('length', $length);
		
		if($length > 0){
			$('.role_'+$type+' .dotLineBox:last').after(htm);
		}else{
			$('.role_'+$type).prepend(htm);
		}
		
		
		$(".authdelBox_wrap, .popupBox").hide();
		
	});
	
	$(document).on('click','.closeBtn', function(){
		
		$(this).closest('.dotLineBox').remove();
	});
	
	$(document).on('click','#modifyAccount', function(){
		
		if(!$('input[name=name]').val()) {
			alert('이름을 입력하세요.');
			return false;
		}
		
		if(!$('input[name=phone]').val()) {
			alert('전화번호를 입력하세요.');
			return false;
		}
		
		$('.role_view input.selectedId').each(function(i){
			var $this = $(this);
			$this.attr("name", "viewList["+ i +"]");
		});
		
		$('.role_regist input.selectedId').each(function(i){
			var $this = $(this);
			$this.attr("name", "registList["+ i +"]");
		});
		
		$('.role_approval input.selectedId').each(function(i){
			var $this = $(this);
			$this.attr("name", "approvalList["+ i +"]");
		});
		
		$('.role_transfer input.selectedId').each(function(i){
			var $this = $(this);
			$this.attr("name", "transferList["+ i +"]");
		});
		
		$('#writeForm').submit();
	});
	
	$(document).on('click','.sendStartMail', function(){
		var $id = $(this).attr('data-id');
		
		$.ajax({
			type:'post',
			url:'/account/sendStartMail',
			data:{id:$id},
			beforeSend: function() {
				$("#wait").show();
				$(".popupBox").show();
	        },
	        complete:function() {
	        	$("#wait").hide();
				$(".popupBox").hide();
	        },
			success:function(data){
				
				if(data.result === true){
					alert('메일을 발송하였습니다.');
					location.reload();
				}else{
					alert('다시 시도해 주세요.');
				}
			}
		});
	});
	
	$(document).on('click','#resetAccount', function(){
		var $id = $(this).attr('data-id');
		if(confirm('계정을 초기화 할까요?')){
			$.ajax({
				type:'post',
				url:'/account/resetAccount',
				data:{id:$id},
				beforeSend: function() {
					$("#wait").show();
					$(".popupBox").show();
		        },
				success:function(data){
					$("#wait").hide();
					$(".popupBox").hide();
					if(data.result === true){
						alert('계정을 초기화하고 메일을 발송하였습니다.');
						location.reload();
					}else{
						alert('다시 시도해 주세요.');
					}
				}
			});
		}
		
	});
	
	$(document).on('click','.changeEnable', function(){
		var $id = $(this).attr('data-id');
		if(confirm($id +' 메일을 잠금해제하시겠습니까?')){
			$.ajax({
				type:'post',
				url:'/account/changeEnable',
				data:{id:$id},
				success:function(data){
					if(data.result === true){
						alert('메일잠금을 해제하였습니다.');
						location.reload();
					}else{
						alert('다시 시도해 주세요.');
					}
				}
			});
		}
		
	});
	
	$(document).on('click','.changeLock', function(){
		var $id = $(this).attr('data-id');
		if(confirm($id +' 메일을 사용할 수 없게 하시겠습니까?')){
			$.ajax({
				type:'post',
				url:'/account/changeLock',
				data:{id:$id},
				success:function(data){
					alert('메일을 사용할수 없게 변경하였습니다.');
					location.reload();
				}
			});
		}
		
	});
	
	$(document).on('click','.searchCategoryPupup', function(){
		var word = $('.category_keyword').val();
		if(!word){
			alert('검색어를 입력하세요.');
			return false;
		}
		var type = $('.selectedCategory').attr('data-type');
		
		searchCategory(word, type);
	});
	
	$(document).on('click','#checkAll', function() {
		$(".checkCategory").prop("checked",$(this).prop("checked"));
	});

	
});

function searchCategory(word, type){
	
	var array = [];
	
	$('.role_'+type).find('.selectedId').each(function(i){
		array.push($(this).val());
	});
	
	$.ajax({
		type:'post',
		url:'/account/searchCategoryPupup',
		data:{word:word},
		success:function(data){
			
			var htm = '';
			
			htm += '<tr>';
			htm += '	<th class="NoTh">NO</th>';
			htm += '	<th>선택 <input type="checkbox" id="checkAll"/></th>';
			htm += '	<th class="cateTh">카테고리</th>';
			htm += '</tr>';
			
			
			var checkCount = 1;
			
			$.each(data.list, function(i, item){
				
				
				var temp = $.grep(array, function(n, j){
					return (n == item.id);
				});
				
				console.log('temp', temp.length);
				
				if(temp.length == 0){
				
					htm += '<tr>';
					htm += '	<td>'+ (checkCount) +'</td>';
					htm += '	<td class="orangechBox"><input type="checkbox" class="checkCategory" data-id="'+ item.id +'" data-name="'+ item.name +'"/></td>';
					htm += '	<td class="fixWidth">'+ item.name +'</td>';
					htm += '</tr>';
					checkCount++;
				}
				
			});
			
			$('#accountPopTable').html(htm);
		}
	});
	
}

