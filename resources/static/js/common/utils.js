(function($) {

    $.passphrase = "mms!@#";
    $.passRegex = /^[A-Za-z0-9]{6,12}$/;

    $.comma = function(x){
        return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    }


    $.alertAndGo = function(txt, url){
        modal({
            title:'알림',
            type: 'alert',
            text: txt,
            buttonText: {
                ok: "확인",
            },
            callback: function(result){
                if(!result){
                    if(url == 'location.reload'){
                        location.reload();
                    }else{
                        location.href = url;
                    }
                }
            }
        });
    }

    $.alertBox = function(txt){
        modal({
            title:'알림',
            type: 'alert',
            text: txt,
            buttonText: {
                ok: "확인",
            },
            callback: function(result){
            }
        });
    }


    $.alertTitleBox = function(title, txt){
        modal({
            title:title,
            type: 'alert',
            text: txt,
            buttonText: {
                ok: "확인",
            },
            callback: function(result){
            }
        });
    }

    $.comfirmTitleBox = function(title, txt, yes, cancel, callbackMethod){
        modal({
            title: title,
            type: 'confirm',
            text: txt,
            buttonText: {
                yes: yes,
                cancel: cancel
            },
            callback: function(result) {
                if(result){
                    callbackMethod();
                }
            }
        });
    }

    $.confirmBox = function(txt, yes, cancel, callbackMethod){
        modal({
            title:'알림',
            type: 'confirm',
            text: txt,
            buttonText: {
                yes: yes,
                cancel: cancel
            },
            callback: function(result) {
                if(result){
                    callbackMethod();
                }
            }
        });
    }

    $.confirmBoxParam = function(txt, yes, cancel, callbackMethod, param){
        modal({
            title:'알림',
            type: 'confirm',
            text: txt,
            buttonText: {
                yes: yes,
                cancel: cancel
            },
            callback: function(result) {
                if(result){
                    callbackMethod(param);
                }

            }
        });
    }

    $.confirmBoxParams = function(txt, yes, cancel, callbackMethod, params){
        modal({
            title:'알림',
            type: 'confirm',
            text: txt,
            buttonText: {
                yes: yes,
                cancel: cancel
            },
            callback: function(result) {
                if(result){
                    callbackMethod(params);
                }

            }
        });
    }

    $.run_waitMe = function(el){
        let text = 'Please wait...';
        let fontSize = '';
        let maxSize = '';
        let textPos = 'vertical';

        el.waitMe({
            effect: 'roundBounce',
            text: text,
            bg: 'rgba(255,255,255,0.7)',
            color: '#000',
            maxSize: maxSize,
            waitTime: -1,
            source: 'img.svg',
            textPos: textPos,
            fontSize: fontSize,
            onClose: function(el) {}
        });
    }


})(jQuery);
