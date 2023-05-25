$(function(){

    $('.card-header').css('background-color', color);

    $(document).on('click', '.user-item', function() {
        location.href = '/userView/'+$(this).data('secure');
    });

});

