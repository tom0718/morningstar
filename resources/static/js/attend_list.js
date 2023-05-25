$(function(){

    $(document).on('click', '.attend-item', function() {
        location.href = '/attend/view/'+$(this).data('department');
    });

    $(document).on('change', 'input[name=attendDate]', function() {
        const strDate = $(this).val();
        console.log('strDate', strDate);

        const numberOfWeek = new Date(strDate).getDay();

        const attendDiv = $('.attend-type-div');

        if(numberOfWeek === 3){
            attendDiv.show();
            $('input[name=attendType]:input[value=wednesday]').prop('checked', true);
        }else{
            attendDiv.hide();
        }

    });

});

