$(function(){

    $(document).on('click','.list-tr',function(){

        let id = $(this).data('id');

        location.href = '/account/modify/'+id;

    });

});
