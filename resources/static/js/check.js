$(document).ready(function () {

    const secure = localStorage.getItem('secure');
    const name = localStorage.getItem('name');
    const phone = localStorage.getItem('phone');

    if(secure !== null && name !== null && phone !== null){
        $('input[name=name]').val(name);
        $('input[name=phone]').val(phone);

        checkMember(name, phone);
    }

    $(document).on('change','.attendType',function(){

        const _type = $('.attendType:checked').val();
        const reasonDiv = $('#reason-div');
        if(_type && _type !== 'mainChurch'){
            reasonDiv.show();
        }else{
            reasonDiv.hide();
        }

    });

    $(document).on('click', '#attend', function () {

        const _type = $('input[name=attendType]:checked').val();

        if (!_type) {
            alert('출석유형을 선택해 주세요.');
            return false;
        }

        if(_type !== 'mainChurch'){
            const _reason = $('#reason').val();
            if(!_reason){
                alert('사유를 입력해 주세요.');
                return false;
            }
        }

        // TODO 출석체크 후 이동
        $('#form').submit();
    });

    $(document).on('click', '#login', function () {

        let _name = $('input[name=name]');
        let _phone = $('input[name=phone]');

        if (!_name.val()) {
            alert('이름을 입력하세요.');
            _name.focus();
            return false;
        }

        if (!_phone.val()) {
            alert('핸드폰번호를 입력하세요.');
            _phone.focus();
            return false;
        }

        checkMember(_name.val(), _phone.val());
    });

    $(document).on('keyup', 'input[name=phone]', function() {
        $(this).val( $(this).val().replace(/[^0-9]/g, "").replace(/(^02|^0505|^1[0-9]{3}|^0[0-9]{2})([0-9]+)?([0-9]{4})$/,"$1-$2-$3").replace("--", "-") );
    });




});

function checkMember(_name, _phone){

    $.ajax({
        type: 'post',
        url: '/checkMember',
        data: {name:_name, phone:_phone},
        beforeSend: function () {
            $.run_waitMe($('body'));
        },
        complete: function () {
            $('body').waitMe('hide');
        },
        success: function (data) {
            if(data.result === 'success'){

                localStorage.setItem('secure', data.secure);
                localStorage.setItem('name', _name);
                localStorage.setItem('phone', _phone);

                $('input[name=secure]').val(data.secure);

                // TODO 출석체크(수요일, 일요일)
                const dayOfWeekNumber = data.dayOfWeekNumber; // 3, 7
                const checkAttend = data.checkAttend;
                const checkDawnAttend = data.checkDawnAttend;
                const checkTime = data.checkTime; // 수요, 일요예배는 2시간전부터 가능
                const time = data.time; // 새벽예배는 8시전까지만 가능

                const loginDiv = $('#login-div');
                const attendDiv = $('#attend-div');
                const worshipTitle = $('#worshipTitle');
                const worshipType = $('#worshipType');

                if((dayOfWeekNumber === 3 || dayOfWeekNumber === 7) && checkAttend === false && checkTime === true){

                    worshipTitle.text(dayOfWeekNumber === 3 ? '수요예배 :' : '주일예배 :');
                    worshipType.val(dayOfWeekNumber === 3 ? 'wednesday' : 'sunday');

                    loginDiv.hide();
                    attendDiv.show();

                }else if(dayOfWeekNumber !== 7 && checkDawnAttend === false && time < 9){
                //}else if(dayOfWeekNumber !== 7 && checkDawnAttend === false){

                    worshipTitle.text('새벽예배 :');
                    worshipType.val('dawn');

                    loginDiv.hide();
                    attendDiv.show();

                }else {
                    $('#form').submit();
                }

                //$('#form').submit();

            }else if(data.result === 'fail'){
                localStorage.removeItem('secure');
                localStorage.removeItem('name');
                localStorage.removeItem('phone');
                alert('일치하는 계정이 없습니다.\n다시 확인해 주세요.');
                return false;
            }

        },
        error: function () {
            alert('다시 시도해 주세요.');
        }
    });

}