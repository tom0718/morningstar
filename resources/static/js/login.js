$(document).ready(function () {

    $(document).on('click', '#login', function () {

        let username = $('#username');
        let password = $('#password');

        if (!username.val()) {
            alert('아이디를 입력하세요.');
            username.focus();
            return false;
        }

        if (!password.val()) {
            alert('비밀번호를 입력하세요.');
            password.focus();
            return false;
        }

        $('form').submit();
    });

    $(document).on('click', '#findId', function () {


        $('.modal-title').text('아이디 찾기');
        $('#item-title').text('이메일:');
        $('#message-text').val('입력하신 이메일로 아이디를 발송해 드립니다.');

        $('#type').val('id');

        $('#findModal').modal('show');

    });

    $(document).on('click', '#findPwd', function () {


        $('.modal-title').text('비밀번호 찾기');
        $('#item-title').text('아이디:');
        $('#message-text').val('등록된 이메일로 임시비밀번호를 발송해 드립니다.');

        $('#type').val('pwd');

        $('#findModal').modal('show');

    });

    $(document).on('click', '#findAction', function () {

        let type = $('#type').val();
        let _input = $('#input').val();

        let msg = type == 'id' ? '이메일' : '아이디';

        if (!_input) {
            alert(msg + '을/를 입력해 주세요.');
            return false;
        }

        $.ajax({
            type: 'post',
            url: '/findItem',
            data: {type:type, item:_input},
            beforeSend: function () {
                $.run_waitMe($('body'));
            },
            complete: function () {
                $('body').waitMe('hide');
            },
            success: function (data) {
                if(data.result == 'success'){
                    alert('발송된 이메일을 확인해 주세요.');
                    location.reload();
                }else if(data.result == 'fail'){
                    alert('일치하는 계정이 없습니다.\n다시 확인해 주세요.');
                }else if(data.result == 'stop'){
                    alert('정지된 계정입니다.\n관리자에게 문의해 주세요.');
                    location.reload();
                }

            },
            error: function () {
                alert('다시 시도해 주세요.');
            }
        });

    });


});