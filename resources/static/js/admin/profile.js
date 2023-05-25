$(document).ready(function(){

	
	$(document).on('click','#modifyProfile', function(){
		var $name = $('#name').val();
		var $company = $('#company option:selected').val();
		var $phone = $('#phone').val();
		var $password = $('#password').val();
		var $password1 = $('#password1').val();
		var $password2 = $('#password2').val();
		var $note = $('#note').val();
		
		if(!$password){
			alert('수정할려면 비밀번호를 입력해야 합니다.');
			return false;
		}
		
		if(($password1.length > 0 || $password2.length > 0) && ($password1 != $password2)){
			alert('비밀번호를 수정하려면 수정하려는 두 비밀번호가 같아야 합니다.');
			return false;
		}
		
		if(confirm('수정 할까요?')){
			$.ajax({
				type:'post',
				url:'/account/modifyProfile',
				data:{name:$name, company:$company, phone:$phone, password:$password, password1:$password1, note:$note},
				success:function(data){

					if(data.result){
						alert('프로필을 수정하였습니다.');
						location.reload();
					}else{
						alert('비밀번호가 다릅니다. 정확하게 입력해 주세요.');
					}
				}
			});
		}
		
	});




	
});

