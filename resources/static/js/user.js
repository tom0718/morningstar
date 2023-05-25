$(function(){

    let familyArray = [];
    let providenceArray = [];

    $('.family').each(function(i){
       familyArray.push($(this).val());
    });

    $('.providence').each(function(i){
        let providenceName = $(this).next('.providenceName').val();
        providenceArray.push($(this).val()+providenceName);
    });

    $(document).on('keyup', '#phoneNumber', function() {
        $(this).val( $(this).val().replace(/[^0-9]/g, "").replace(/(^02|^0505|^1[0-9]{3}|^0[0-9]{2})([0-9]+)?([0-9]{4})$/,"$1-$2-$3").replace("--", "-") );
    });

    $(document).on('click', '.license-plus', function() {

        let _key = Math.random().toString(36).substr(2,11);

        let htm = '';
        htm += '<div class="input-group mb-3">';
        htm += '    <input type="text" class="form-control license" placeholder="자격증을 입력하세요." aria-describedby="'+ _key +'" value="">';
        htm += '        <span class="input-group-text license-minus" id="'+ _key +'">-</span>';
        htm += '</div>';

        $('#license').append(htm);
    });

    $(document).on('click', '.license-minus', function() {
        $(this).closest('div').remove();
    });

    $(document).on('click', '.delete-item', function() {
        if(confirm('삭제할까요?')){
            $(this).closest('span.btn').remove();
        }
    });

    $(document).on('change', '#myFamily', function() {
        let name = $('option:selected', $(this)).val();
        if(name){

            // TODO 선택된 값인지 체크 후 적용

            if(familyArray.includes(name) == false){
                familyArray.push(name);

                let htm = '';
                htm += '<span class="btn btn-outline-light department-span">';
                htm += name;
                htm += '    <input type="hidden" class="family" value="'+ name +'"/>';
                htm += '    <span class="delete-item">X</span>';
                htm += '</span>';

                $('#family-div').append(htm);
            }else{
                alert('이미 포함된 가족입니다.');
                return false;
            }


        }

    });

    $(document).on('click', '#addProvidence', function() {
        let name = $('#myProvidence option:selected').val();
        let providenceName = $('#providenceName').val();

        if(!name){
            alert('가족관계를 선택해주세요.');
            return false;
        }
        if(!providenceName){
            alert('가족이름을 입력해주세요.');
            return false;
        }


        // TODO 선택된 값인지 체크 후 적용
        if(providenceArray.includes(name+providenceName) == false) {
            familyArray.push(name+providenceName);

            let htm = '';
            htm += '<span class="btn btn-outline-light department-span">';
            htm += name + ' ' + providenceName;
            htm += '    <input type="hidden" class="providence" value="'+ name +'"/>';
            htm += '    <input type="hidden" class="providenceName" value="'+ providenceName +'"/>';
            htm += '    <span class="delete-item">X</span>';
            htm += '</span>';

            $('#providence-div').append(htm);

            $('#providenceName').val('');

        }else{
            alert('이미 포함된 가족입니다.');
            return false;
        }
    });

    $(document).on('click', '#deleteUser', function() {

        if(confirm('해당회원을 삭제할까요?')){

            let seqNo = $('#seqNo').val();

            $.ajax({
                type:'post',
                url: '/user/deleteUser',
                data:{seqNo:seqNo},
                beforeSend: function(){
                    $.run_waitMe($('body'));
                },
                complete: function(){
                    $('body').waitMe('hide');
                },
                success:function(data){

                    alert('해당회원을 삭제하였습니다.');
                    location.href = $('#back').attr('href');

                },
                error:function(){
                    alert('다시 시도해 주세요.');
                }
            });
        }
    });


    $(document).on('click','#save',function(){

        let seqNo = $('#seqNo').val();

        let file = $('#thumbnailFile').val();
        let thumbnailImage = $('#thumbnail').val();

        let realName = $('#realName').val();
        let birthday = $('#birthday').val();

        let gender = $('#gender option:selected').val();
        let blood = $('#blood').val();
        let department = $('#department').val();
        let completionDate = $('#completionDate').val();
        let phone = $('#phoneNumber').val();
        let mission = $('#mission option:selected').val();
        let religiousLevel = $('#religiousLevel option:selected').val();
        let secondGeneration = $('input[name=secondGeneration]:checked').val();
        let job = $('#job option:selected').val();
        let address = $('#address').val();

        if(part == 'supreme'){
            if(!$('#code option:selected').val()){
                alert('교회를 선택해 주세요.');
                return false;
            }
        }

        if(!seqNo && !file){
            alert('사진을 선택해 주세요.');
            return false;
        }

        if(!file && !thumbnailImage){
            alert('사진을 선택해 주세요.');
            return false;
        }

        if(!realName){
            alert('본명을 입력해주세요.');
            return false;
        }

        if(!birthday){
            alert('출생일을 입력해주세요.');
            return false;
        }

        if(!gender){
            alert('성별을 선택해주세요.');
            return false;
        }

		/*
        if(!blood){
            alert('혈액형을 선택해주세요.');
            return false;
        }
        */

        if(!department){
            alert('부서를 선택해주세요.');
            return false;
        }

		/*
        if(!completionDate){
            alert('수료일을 입력해주세요.');
            return false;
        }
        */

        if(!phone){
            alert('연락처를 입력해주세요.');
            return false;
        }

        if(!mission){
            alert('사명을 선택해주세요.');
            return false;
        }

        if(!religiousLevel){
            alert('신급을 선택해주세요.');
            return false;
        }

        if(!secondGeneration){
            alert('2세여부를 선택해주세요.');
            return false;
        }

        if(!job){
            alert('직업을 선택해주세요.');
            return false;
        }

        if(!address){
            alert('주소를 입력해주세요.');
            return false;
        }

        let no = 0;

        $('.license').each(function(i){
           if($(this).val()){
               $(this).attr('name','licenseList['+ no +']');
               no++;
           }
        });

        $('.family').each(function(i){
           $(this).attr('name','familyList['+ i +']');
        });

        $('.providence').each(function(i){
            $(this).attr('name','providenceList['+ i +'].title');
            $(this).next('.providenceName').attr('name','providenceList['+ i +'].name');
        });

        if(confirm('정보를 저장할까요?')){
            $('#form').submit();
        }

    });

});

function setThumbnail(event){
    let fileSize = $('#thumbnailFile')[0].files[0].size;

    if(fileSize > (4*1024*1024)){
        $.alertBox('4M 이하만 등록할 수 있습니다.');
        return false;
    }

    let reader = new FileReader();

    reader.onload = function(event) {
        let img = document.createElement("img");

        img.setAttribute("src", event.target.result);

        $("#view-thumbnail").attr('src', event.target.result);

    };

    reader.readAsDataURL(event.target.files[0]);
}

