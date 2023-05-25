$(function(){

    changeType();

    $(document).on('change', 'select[name=type]', function() {
        changeType();
    });

    $(document).on('click', '.keyword-plus', function() {

        let _key = Math.random().toString(36).substr(2,11);

        let htm = '';
        htm += '<div class="input-group mb-3">';
        htm += '    <input type="text" class="form-control keyword" placeholder="키워드를 입력하세요." aria-describedby="'+ _key +'" value="">';
        htm += '        <span class="input-group-text keyword-minus" id="'+ _key +'">-</span>';
        htm += '</div>';

        $('#keyword').append(htm);
    });

    $(document).on('click', '.keyword-minus', function() {
        $(this).closest('div').remove();
    });

    $(document).on('click', '.close_i', function() {
        $(this).closest('li').remove();
    });

    $(document).on('click', '#delete', function() {

        if(confirm('말씀을 삭제할까요?')){

            let seqNo = $('input[name=seqNo]').val();

            $.ajax({
                type:'post',
                url: '/word/deleteWord',
                data:{seqNo:seqNo},
                beforeSend: function(){
                    $.run_waitMe($('body'));
                },
                complete: function(){
                    $('body').waitMe('hide');
                },
                success:function(data){

                    alert('삭제하였습니다.');
                    location.href = _href;

                },
                error:function(){
                    alert('다시 시도해 주세요.');
                }
            });
        }

    });

    $(document).on('click','#save',function(){



        let type = $('select[name=type] option:selected').val();
        let title = $('#title').val();

        if(!type){
            alert('말씀 구분을 선택해 주세요.');
            return false;
        }

        if(!title){
            alert('제목을 입력해 주세요.');
            return false;
        }

        if(type == '사진자료'){
            let picture = $('#pictureUl li');
            if(picture.length < 1){
                alert('사진자료를 입력해 주세요.');
                return false;
            }

            picture.each(function(i){
                $(this).find('.imageList').attr('name','fileList['+ i +'].file');
                $(this).find('.imageNameList').attr('name','fileList['+ i +'].image');
                $(this).find('.originList').attr('name','fileList['+ i +'].origin');
            });

        }else{
            let contents = $('#contents').val();
            if(!contents){
                alert('내용을 입력해 주세요.');
                return false;
            }
        }

        let no = 0;
        $('.keyword').each(function(i){
           if($(this).val()){
               $(this).attr('name','keywordList['+ no +']');
               no++;
           }
        });



        if(confirm('말씀을 저장할까요?')){
            $('#form').submit();
        }

    });



});

function setPicture(event){

    let fileList = event.target.files;

    Array.from(fileList).forEach((file) => {

        let fileName = file.name.trim().replace(/(.png|.jpg|.jpeg|.gif)$/,'');

        let reader = new FileReader();
        reader.onload = function(event) {

            let div = '';

            div += '<li class="items">';
            div += '    <div class="close_btn close_item" ><i class="nc-icon nc-simple-remove close_i"></i></div>';
            div += '    <input type="hidden" class="imageList" value="'+ event.target.result +'"/>';
            div += '    <input type="hidden" class="imageNameList" value=""/>';
            div += '    <input type="hidden" class="originList" value="'+ fileName +'"/>';
            div += '    <img src="'+ event.target.result +'" class="img" alt="">';
            div += '</li>';

            $("#pictureUl").append(div);

        };

        reader.readAsDataURL(file);

    });

}

function changeType(){
    let _type = $('select[name=type] option:selected').val();
    if(_type){
        let pictureRow = $('.picture-row');
        let contentsRow = $('.contents-row');
        if(_type == '사진자료'){
            pictureRow.show();
            contentsRow.hide();
        }else{
            pictureRow.hide();
            contentsRow.show();
        }
    }
}
