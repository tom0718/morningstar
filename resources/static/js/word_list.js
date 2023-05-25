$(function(){

    $(document).on('click', '.list-td', function() {
        let seqNo = $(this).closest('tr').data('seq');
        let word = $(this).closest('tr').data('word');

        let param = !word ? '' : '?word='+word;

        location.href = '/word/view/'+seqNo+param;
    });


});

function enterkey() {
    if (window.event.keyCode == 13) {
        $('form').submit();
    }
}