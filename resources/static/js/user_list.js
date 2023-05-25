$(function(){

    $(document).on('click', '.user-item', function() {
        location.href = '/user/view/'+$(this).data('secure');
    });

});

