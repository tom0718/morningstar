let departmentArray = [];

$(function(){

    let idFlag = false;
    let emailFlag = false;
    let codeFlag = false;
    let reg = /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,12}$/;
    let regEmail = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;

    if(id){
        idFlag = true;
        emailFlag = true;
    }

    $('.department').each(function(i){
        let department = $(this).val();
        let gender = $(this).next('.gender').val();

        departmentArray.push(department+gender);
    });

    $(document).on('change','input[name=part]',function(){

        let _part = $('input[name=part]:checked').val();

        let staffDiv = $('.staff-div');
        let codeDiv = $('.code-div');

        if(_part === 'staff'){
            staffDiv.show();
            codeDiv.hide();
        }else if(_part === 'pastor'){
            staffDiv.hide();
            codeDiv.hide();
        }else if(_part === 'admin' || _part === 'word'){
            staffDiv.hide();
            codeDiv.show();
        }
    });

    $(document).on('change','#id',function(){
        idFlag = false;
    });

    $(document).on('change','#email',function(){
        emailFlag = false;
    });


    $(document).on('click','#idCheck',function(){

        let id = $('#id');

        if(!id.val()){
            alert('아이디를 입력하세요.');
            id.focus();
            return false;
        }

        $.ajax({
            type:'post',
            url: '/account/checkId',
            data:{id:id.val()},
            beforeSend: function(){
                $.run_waitMe($('body'));
            },
            complete: function(){
                $('body').waitMe('hide');
            },
            success:function(data){

                if(data.exist == false){ // id 가 없음.
                    alert('사용가능한 아이디입니다.');
                    idFlag = true;
                }else if(data.exist == true){
                    idFlag = false;
                    alert('이미 사용중인 아이디입니다.');
                }
            },
            error:function(){
                alert('다시 시도해 주세요.');
            }
        });

    });

    $(document).on('click','#emailCheck',function(){

        let email = $('#email');

        if(!email.val()){
            alert('이메일을 입력하세요.');
            email.focus();
            return false;
        }

        $.ajax({
            type:'post',
            url: '/account/checkEmail',
            data:{email:email.val(), id:id},
            beforeSend: function(){
                $.run_waitMe($('body'));
            },
            complete: function(){
                $('body').waitMe('hide');
            },
            success:function(data){

                if(data.exist == false){
                    alert('사용가능한 이메일입니다.');
                    emailFlag = true;
                }else if(data.exist == true){
                    emailFlag = false;
                    alert('이미 사용중인 이메일입니다.');
                }
            },
            error:function(){
                alert('다시 시도해 주세요.');
            }
        });

    });

    /*
    $(document).on('blur','#password', function() {

        if(!reg.test($(this).val())){
            alert('8~12자리의 영문대문자,소문자,숫자,특수문자로 비밀번호를 구성해 주세요.');
            return false;
        }

    });
    */

    $(document).on('keyup', '#phoneNumber', function() {
        $(this).val( $(this).val().replace(/[^0-9]/g, "").replace(/(^02|^0505|^1[0-9]{3}|^0[0-9]{2})([0-9]+)?([0-9]{4})$/,"$1-$2-$3").replace("--", "-") );
    });

    $(document).on('click','#addDepartment',function(){

        let department = $('#department option:selected').val();
        let departmentName = $('#department option:selected').text();
        let gender = $('input[name=gender]:checked').val();

        if(!department){
            alert('관리부서를 선택해 주세요.');
            return false;
        }

        if(!gender){
            alert('관리부서 성별을 선택해 주세요.');
            return false;
        }

        let htm = '';

        $('input[name=gender]:checked').each(function(i){

            let selectedDepartment = department+$(this).val();
            let genderName = $(this).val() == 'M' ? '남' : '여';

            // 선택한 부서가 있는지 체크
            if(departmentArray.includes(selectedDepartment) == false){
                departmentArray.push(selectedDepartment);
                htm += '<span class="btn btn-outline-light department-span">'+ departmentName +' '+ genderName +' <input type="hidden" class="department" value="'+ department +'"/><input type="hidden" class="gender" value="'+ $(this).val() +'"/><span class="delete-department">&nbsp;X</span></span>';
            }
        });



        $('#department-div').append(htm);

    });

    $(document).on('click','.delete-department',function(){
        if(confirm('해당 관리부서를 삭제할까요?')){
            $(this).closest('.department-span').remove();
        }
    });


    $(document).on('click','#save',function(){

        let part = $('input[name=part]:checked').val();
        let id = $('#id');
        let pwd = $('#password');
        let name = $('#name');
        let phone = $('#phoneNumber');
        let code = $('#code option:selected').val();
        let email = $('#email');


        if(!part){
            alert('관리자구분을 선택해주세요.');
            return false;
        }

        if(part && part === 'admin'){
            if(!code){
                alert('교회를 선택해주세요.');
                return false;
            }
        }

        if(!id.val()){
            alert('아이디를 입력하세요.');
            id.focus();
            return false;
        }

        if(idFlag == false){
            alert('아이디체크를 해주세요.');
            return false;
        }

        if(!id && !pwd.val()){
            alert('비밀번호를 입력해주세요.');
            pwd.focus();
            return false;
        }

        if(pwd.val() && !reg.test(pwd.val())){
            alert('8~12자리의 영문대문자,소문자,숫자,특수문자로 비밀번호를 구성해 주세요.');
            pwd.focus();
            return false;
        }

        if(!name.val()){
            alert('이름을 입력해주세요.');
            name.focus();
            return false;
        }

        if(!phone.val()){
            alert('전화번호를 입력해주세요.');
            phone.focus();
            return false;
        }

        if(!email.val()){
            alert('이메일을 입력해주세요.');
            email.focus();
            return false;
        }

        if(emailFlag == false){
            alert('이메일 체크를 클릭해주세요.');
            return false;
        }

        if(!regEmail.test(email.val())){
            alert('이메일을 정확하게 입력해주세요.');
            email.focus();
            return false;
        }


        if(part == 'staff'){

            let department = $('.department');

            if(department.length < 1){
                alert('관리부서를 선택해주세요.');
                return false;
            }


            department.each(function(i){
                $(this).attr('name','roleList['+ i +'].department');
                $(this).next('.gender').attr('name','roleList['+ i +'].gender');
            });

        }

        if(confirm('계정을 등록할까요?')){
            $('#form').submit();
        }

    });

    $(document).on('click','#delete',function(){

        let id = $('#id').val();

        if(confirm('관리자 계정을 삭제할까요?')){
            $.ajax({
                type:'post',
                url: '/account/delete',
                data:{id:id},
                beforeSend: function(){
                    $.run_waitMe($('body'));
                },
                complete: function(){
                    $('body').waitMe('hide');
                },
                success:function(data){

                    if(data.result > 0){
                        alert('관리자 계정을 삭제하였습니다.');
                        location.href = '/account/list';
                    }else{
                        alert('다시 시도해 주세요.');
                    }

                },
                error:function(){
                    alert('다시 시도해 주세요.');
                }
            });
        }
    });





});

