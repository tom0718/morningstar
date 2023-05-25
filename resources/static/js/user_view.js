$(function(){

    counseling();

    $(document).on('click','#connectModalPop',function(){
        $('#connectModal').find('input').val('');
        $('#connection-department').val('0').prop('selected', true);
        $('input[name=connection-gender]').prop('checked', true);
        $('#connectModal').modal('show');
        connection();

    });

    $(document).on('click','.click-div',function(){
        let _radio = $(this).closest('.row').find('.checked-radio');

        if(_radio.is(':checked')){
            _radio.prop('checked', false);
        }else{
            _radio.prop('checked', true);
        }

    });

    $(document).on('click','#counseling',function(){
        $('#counselingModal').find('input').val('');
        $('#counselingModal').find('textarea').val('');
        $('#counselingModal').modal('show');
    });

    $(document).on('click','.counseling-title',function(){
        let seqNo = $(this).data('seq');

        console.log('seqNo', seqNo);

        $.ajax({
            type:'post',
            url: '/user/getCounseling',
            data:{seqNo:seqNo},
            beforeSend: function(){
                $.run_waitMe($('body'));
            },
            complete: function(){
                $('body').waitMe('hide');
            },
            success:function(data){

                let counseling = data.counseling;

                $('#seqNo').val(counseling.seqNo);
                $('#title').val(counseling.title);
                $('#content').val(counseling.content);
                $('#handling').val(counseling.handling);

                $('#counselingModal').modal('show');

            },
            error:function(){
                alert('다시 시도해 주세요.');
            }
        });


    });

    $(document).on('click','#saveCounseling',function(){
        let seqNo = $('#seqNo').val();
        let userSeqNo = $('#userSeqNo').val();
        let title = $('#title').val();
        let content = $('#content').val();
        let handling = $('#handling').val();

        if(!title){
            alert('상담제목을 입력하세요.');
            return false;
        }

        if(!content){
            alert('상담내용을 입력하세요.');
            return false;
        }

        if(confirm('상담을 저장할까요?')){
            $.ajax({
                type:'post',
                url: '/user/saveCounseling',
                data:{title:title, content:content, handling:handling, userSeqNo:userSeqNo, seqNo:seqNo},
                beforeSend: function(){
                    $.run_waitMe($('body'));
                },
                complete: function(){
                    $('body').waitMe('hide');
                },
                success:function(data){

                    alert('저장하였습니다.');
                    counseling();
                    $('#counselingModal').modal('hide');

                },
                error:function(){
                    alert('다시 시도해 주세요.');
                }
            });
        }

    });

    $(document).on('click','#counseling-div .page-item',function(){
        if($(this).hasClass('active')){
            let url = $(this).find('a').data('url');
            $('#counseling-div').load(url, function(){});
        }
    });

    $(document).on('click','#connection-div .page-item',function(){
        if($(this).hasClass('active')){
            let url = $(this).find('a').data('url');
            $('#connection-div').load(url, function(){});
        }
    });

    $(document).on('click','#saveRelationship',function(){
        let seqNo = $('.checked-radio:checked').val();
        let relationName = $('#relationName option:selected').val();
        let me = $('#userSeqNo').val();

        if(!seqNo){
            alert('설정할 가족을 체크해 주세요.');
            return false;
        }

        if(!relationName){
            alert('나와의 관계를 선택해주세요.');
            return false;
        }


        if(confirm('가족으로 설정할까요?')){
            $.ajax({
                type:'post',
                url: '/user/saveRelationship',
                data:{me:me, userSeqNo:seqNo, relationName:relationName},
                beforeSend: function(){
                    $.run_waitMe($('body'));
                },
                complete: function(){
                    $('body').waitMe('hide');
                },
                success:function(data){

                    alert('가족으로 설정하였습니다.');
                    location.reload();

                },
                error:function(){
                    alert('다시 시도해 주세요.');
                }
            });
        }

    });

    $(document).on('click','#searchConnect',function(){
        connection();
    });

    $(document).on('click','.remove-connect',function(){

        let me = $(this).data('me');
        let family = $(this).data('family');

        if(confirm('가족관계를 해제 할까요?')){
            $.ajax({
                type:'post',
                url: '/user/removeRelationship',
                data:{me:me, family:family},
                beforeSend: function(){
                    $.run_waitMe($('body'));
                },
                complete: function(){
                    $('body').waitMe('hide');
                },
                success:function(data){

                    alert('가족관계를 해제하였습니다.');
                    location.reload();

                },
                error:function(){
                    alert('다시 시도해 주세요.');
                }
            });
        }

    });

    $(document).on('click','.delete-counseling',function(){

        let seq = $(this).data('seq');

        if(confirm('상담내용을 삭제할까요?')){
            $.ajax({
                type:'post',
                url: '/user/deleteCounseling',
                data:{seqNo:seq},
                beforeSend: function(){
                    $.run_waitMe($('body'));
                },
                complete: function(){
                    $('body').waitMe('hide');
                },
                success:function(data){

                    alert('상담내용을 삭제하였습니다.');
                    counseling();

                },
                error:function(){
                    alert('다시 시도해 주세요.');
                }
            });
        }

    });

});

function counseling(){
    let userSeqNo = $('#userSeqNo').val();

    $('#counseling-div').load('/user/counseling/'+ userSeqNo +'/list', function(){});
}

function connection(){

    let userSeqNo = $('#userSeqNo').val();
    let department = $('#connection-department option:selected').val();
    let gender = $('input[name=connection-gender]:checked').val();
    let name = $('#connection-name').val();

    $('#relation-name').text($('#userName').val());

    $('#connection-div').load('/user/connection/list?me='+userSeqNo+'&department='+department+'&gender='+gender+'&user='+name, function(){});
}