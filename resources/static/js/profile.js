$(function(){

    let emailFlag = true;
    let reg = /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,12}$/;
    let regEmail = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;


    $(document).on('change','#email',function(){
        emailFlag = false;
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
            data:{email:email.val(), id:$('#id').val()},
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


    $(document).on('click','#save',function(){

        let id = $('#id').val();
        let pwd = $('#password');
        let name = $('#name');
        let phone = $('#phoneNumber');
        let email = $('#email');


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
            alert('이메일 체크해를 클릭해주세요.');
            return false;
        }

        if(!regEmail.test(email.val())){
            alert('이메일을 정확하게 입력해주세요.');
            email.focus();
            return false;
        }

        if(confirm('정보를 수정할까요?')){
            let params = {
                password : pwd.val(),
                name : name.val(),
                phone : phone.val(),
                email : email.val()
            }
            $.ajax({
                type:'post',
                url: '/account/modifyProfile',
                data:params,
                beforeSend: function(){
                    $.run_waitMe($('body'));
                },
                complete: function(){
                    $('body').waitMe('hide');
                },
                success:function(data){

                    if(data.result == 'success'){
                        alert('수정하였습니다.');
                    }else if(data.result == 'fail'){
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

