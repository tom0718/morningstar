$(document).ready(function () {

    $('.card-user').css('background-color', color);

    $(document).on('change', '#image', function () {

        let form = $('#fileUploadForm')[0];
        let formData = new FormData(form);

        $.ajax({
            type: 'post',
            url: '/changeProfileImage',
            data: formData,
            enctype: 'multipart/form-data',
            processData: false,
            contentType: false,
            cache: false,
            beforeSend: function () {
                $.run_waitMe($('body'));
            },
            complete: function () {
                $('body').waitMe('hide');
            },
            success: function (data) {
                if(data.result == 'success'){
                    const img = data.img;
                    $('#profile-image').attr('src', img);
                }

            },
            error: function () {
                alert('다시 시도해 주세요.');
            }
        });
    });



});