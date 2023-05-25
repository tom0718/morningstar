$(document).ready(function () {

    initCalendar();

    $(document).on('click','#requestRoom',function(){


        const department = $('#department option:selected').val();
        const room = $('#roomName option:selected').val();
        const rentDate = $('#rentDate').val();
        const startTime = $('#rentStartTime option:selected').val();
        const endTime = $('#rentEndTime option:selected').val();
        const purpose = $('#purpose').val();
        const male = $('#male').is(':checked');
        const female = $('#female').is(':checked');

        if(_auth !== 'staff'){
            if(male === false && female === false){
                alert('부서 성별을 선택해 주세요.');
                return false;
            }
        }

        if(!room){
            alert('신청룸을 선택해 주세요.');
            return false;
        }

        if(!rentDate){
            alert('신청일을 선택해 주세요.');
            return false;
        }

        const start = new Date(rentDate+' '+startTime);
        const end = new Date(rentDate+' '+endTime);

        if(start >= end){
            alert('사용 종료시간이 시작시간보다 커야합니다.');
            return false;
        }

        if(!purpose){
            alert('사용목적을 입력해 주세요.');
            return false;
        }

        const param = {
            department: department,
            male: male,
            female: female,
            room: room,
            rentDate: rentDate,
            startTime: startTime,
            endTime: endTime,
            purpose: purpose
        }

        if(confirm('룸대여를 신청하시겠습니까?')){
            $.ajax({
                type: 'post',
                url: '/room/rentRoom',
                data: param,
                beforeSend: function () {
                    $.run_waitMe($('body'));
                },
                complete: function () {
                    $('body').waitMe('hide');
                },
                success: function (data) {
                    if(data.result === 'success'){
                        location.reload();
                    }else if(data.result === 'fail'){
                        alert('다시 시도해 주세요.');
                    }

                },
                error: function () {
                    alert('다시 시도해 주세요.');
                }
            });
        }
    });



});

function initCalendar(){

    let calendarEl = document.getElementById('calendar');

    $.ajax({
        type: 'post',
        url: '/room/getBookRoomList',
        //data: param,
        beforeSend: function () {
            $.run_waitMe($('body'));
        },
        complete: function () {
            $('body').waitMe('hide');
        },
        success: function (data) {

            const rentRoomList = data.rentRoomList;
            const startDate = data.startDate;
            const endDate = data.endData;
            const today = data.today;

            let eventList = [];
            /*
            {
                title: 'Meeting',
                start: '2023-01-12T10:30:00',
                end: '2023-01-12T12:30:00',
                color: '#ff0059', // 동그라미 색깔
                //textColor: '#000000'
            }
            */

            $.each(rentRoomList,function(index, item){

                console.log('item', item);

                let event = {
                    seqNo: item.seqNo,
                    date: item.rentDate,
                    title: item.room + '-' +item.depart.group3,
                    start: item.rentDate + 'T' + item.startTime,
                    end: item.rentDate + 'T' + item.endTime,

                    //color: '',
                }

                eventList.push(event);

            });

            let calendar = new FullCalendar.Calendar(calendarEl, {
                /*
                initialDate: '2023-01-12',
                validRange: {
                    start: '2023-01-01',
                    end: '2023-02-28'
                },
                */
                initialDate: today,
                validRange: {
                    start: startDate,
                    end: endDate
                },
                initialView: 'dayGridMonth',
                headerToolbar: {
                    left: 'prev,next today',
                    center: 'title',
                    right: 'dayGridMonth,timeGridWeek',
                },
                titleFormat: function (date) {
                    // YYYY년 MM월
                    if ( date.date.month < 9){
                        return `${date.date.year}. ${"0"+(date.date.month + 1)}`;
                    }
                    return `${date.date.year}. ${date.date.month + 1}`;
                },
                height: 'auto',
                navLinks: true, // can click day/week names to navigate views
                editable: false,
                selectable: true,
                selectMirror: true,
                nowIndicator: true,
                dateClick: function(info){
                    //alert(info.dateStr);
                    initRentModal('date', info.dateStr, '');
                },
                eventClick: function(event, jsEvent, view){
                  console.log('event', event);
                  const seqNo = event.event._def.extendedProps.seqNo;
                  console.log('seqNo', seqNo);

                    initRentModal('event','', seqNo);

                },
                events: eventList
                    /*
                    [
                    {
                        title: 'All Day Event',
                        start: '2023-01-01'
                    },
                    {
                        title: 'Long Event',
                        start: '2023-01-07',
                        end: '2023-01-10'
                    },
                    {
                        groupId: 999,
                        title: 'Repeating Event',
                        start: '2023-01-09T16:00:00'
                    },
                    {
                        groupId: 999,
                        title: 'Repeating Event',
                        start: '2023-01-16T16:00:00'
                    },
                    {
                        title: 'Conference',
                        start: '2023-01-11',
                        end: '2023-01-13',

                    },
                    {
                        title: 'Meeting',
                        start: '2023-01-12T10:30:00',
                        end: '2023-01-12T12:30:00',
                        color: '#ff0059', // 동그라미 색깔
                        //textColor: '#000000'
                    },
                    {
                        title: 'Lunch',
                        start: '2023-01-12T12:00:00'
                    },
                    {
                        title: 'Meeting',
                        start: '2023-01-12T14:30:00'
                    },
                    {
                        title: 'Happy Hour',
                        start: '2023-01-12T17:30:00'
                    },
                    {
                        title: 'Dinner',
                        start: '2023-01-12T20:00:00'
                    },
                    {
                        title: 'Birthday Party',
                        start: '2023-01-13T07:00:00'
                    },
                    {
                        title: 'Click for Google',
                        url: 'http://google.com/',
                        start: '2023-01-28'
                    }
                ]

                     */

            });

            calendar.render();

        },
        error: function () {
            alert('다시 시도해 주세요.');
        }
    });
}

function initRentModal(type, eventDate, seqNo){

    $.ajax({
        type: 'post',
        url: '/room/getRoomCondition',
        data: {eventDate:eventDate, seqNo:seqNo},
        beforeSend: function () {
            $.run_waitMe($('body'));
        },
        complete: function () {
            $('body').waitMe('hide');
        },
        success: function (data) {

            const rentDate = data.rentDate;
            const rentRoom = data.rentRoom;
            const rentRoomList = data.rentRoomList;

            $('#rentDate').val(rentDate);

            console.log('rentRoomList', rentRoomList);

            $('#rentStartTime option').prop('disabled', false);
            $('#rentEndTime option').prop('disabled', false);

            if(type === 'event'){

                if(_id === rentRoom.register) { // 수정화면
                    setData(type, rentRoom, rentRoomList);
                }else{
                    // TODO view 화면

                }
            }else if(type === 'date'){ // 등록화면
                setData(type, rentRoom, rentRoomList);
            }
        },
        error: function () {
            alert('다시 시도해 주세요.');
        }
    });

}

function setData(type, rentRoom, rentRoomList){

    console.log('type', type);
    console.log('rentRoom', rentRoom);

    const _startTime = $('#rentStartTime');
    const _endTime = $('#rentEndTime');

    let roomName = '';

    if(type === 'event'){ // 신청한것 수정화면
        $('#purpose').val(rentRoom.purpose);

        let _time1 = rentRoom.startTime; // 12:00:00
        let _time2 = rentRoom.endTime; // 12:00:00

        _startTime.val(_time1).prop('selected', true);
        _endTime.val(_time2).prop('selected', true);

        roomName = rentRoom.room;
    }else{ // 등록화면
        roomName = 'S1';
    }

    $('#roomName').val(roomName).prop('selected', true);

    if(rentRoomList.length > 0){

        $.each(rentRoomList, function(index, item){

            _startTime.find('option').each(function(i){
                let _start = $(this);
                $.each(item.startTimeList, function(j, time){
                    if(_start.val() === time.rentTime && rentRoom.seqNo !== time.rentRoomSeqNo){ //  && _id === rentRoom.register
                        _start.prop('disabled', true);
                    }
                });
            });

            _endTime.find('option').each(function(i){
                let _end = $(this);
                $.each(item.endTimeList, function(j, time){
                    if(_end.val() === time.rentTime && rentRoom.seqNo !== time.rentRoomSeqNo){ //  && _id === rentRoom.register
                        _end.prop('disabled', true);
                    }
                });
            });
        });
    }

    $('#event-modal').modal('show');
}