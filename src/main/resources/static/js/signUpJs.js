window.onload = function() {

    //2022-0423 강경민
    //서버 글로벌 에러 3초만 출력되게 변경
    setTimeout(function (){
        document.getElementById("globalErr").style.display = "none";
    }, 3000);








    //2022-0422 강경민
    //문자 전송 버튼 작동되게 작업
    var httpRequest;

    document.getElementById("sendSMS").addEventListener('click', () => {

        var memberPhoneNum = document.getElementById("phoneCertification").value;
        var reqJson = new Object();
        reqJson.getPhoneNum = memberPhoneNum;
        httpRequest = new XMLHttpRequest();




        //2022-0424 강경민
        //문자 전송 대기 시간 함수 작업
        function checkRemainSendMessageTime(){
            if (num > 0){
                num--;
                document.getElementById("sendSMS").textContent = num + "초 후 사용가능";
            } else{
                clearInterval(tid);
            }
        }

        document.getElementById("sendSMS").disabled = true;

        var num = 10;
        document.getElementById("sendSMS").textContent = num + "초 후 사용가능";

        tid = setInterval(function() {
            document.getElementById("sendSMS").textContent = num + "초 후 사용가능";
            checkRemainSendMessageTime();
        }, 1000);


        httpRequest.onreadystatechange = () => {
            //readyState가 Done이고 응답 값이 200일 때
            if (httpRequest.readyState === XMLHttpRequest.DONE) {

                setTimeout(function (){
                    document.getElementById("sendSMS").disabled = false;
                    clearInterval(tid);
                    document.getElementById("sendSMS").textContent = "문자전송";
                }, 10000);

                if (httpRequest.status === 200) {
                    var result = httpRequest.response;
                    // document.getElementById("phoneCertificationNum").value = result.authenticationNum; // jwt 완성 후에 제거할 것

                } else {
                    alert('ajax Request Error');
                }
            }
        };
        //Post 방식, 응답은 json, 요청헤더 json
        httpRequest.open('POST', 'signUp/sendAuthenticationNum', true);
        httpRequest.responseType = "json";
        httpRequest.setRequestHeader('Content-Type', 'application/json');
        httpRequest.send(JSON.stringify(reqJson));
    });










    // 2022-0423 강경민
    // 아이디 중복 검사 버튼 작동되게 작업
    // 프론트 1차 검증 추가 및 에러 3초만 뜨도록 설정
    var httpRequest2;

    document.getElementById("idDupCheck").addEventListener('click', () => {

        var memberId = document.getElementById("memberId").value;
        var reqJson = new Object();
        reqJson.getMemberId = memberId;
        httpRequest2 = new XMLHttpRequest();

        httpRequest2.onreadystatechange = () => {
            //readyState가 Done이고 응답 값이 200일 때
            if (httpRequest2.readyState === XMLHttpRequest.DONE) {
                if (httpRequest2.status === 200) {
                    var result = httpRequest2.response;

                    // 아이디 형식 검증 및 중복 검증
                    var regExp = /^([A-Z|a-z|0-9]){4,20}$/;

                    if(result.beforeMemberId.length == 0){
                        document.getElementById("idFormErr").style.display = "none";
                        document.getElementById("idBlankErr").style.display = "block";

                        setTimeout(function (){
                            document.getElementById("idBlankErr").style.display = "none";
                        }, 3000);

                    } else if(!regExp.test(result.beforeMemberId)){
                        document.getElementById("idBlankErr").style.display = "none";
                        document.getElementById("idFormErr").style.display = "block";

                        setTimeout(function (){
                            document.getElementById("idFormErr").style.display = "none";
                        }, 3000);

                    }else{
                        document.getElementById("idBlankErr").style.display = "none";
                        document.getElementById("idFormErr").style.display = "none";

                        if (result.dupCheckId == "false"){
                            document.getElementById("idDupCheckFail").style.display = "block";
                            document.getElementById("idDupCheckSuccess").style.display = "none";

                            setTimeout(function (){
                                document.getElementById("idDupCheckFail").style.display = "none";
                            }, 3000);
                        } else{
                            document.getElementById("idDupCheckFail").style.display = "none";
                            document.getElementById("idDupCheckSuccess").style.display = "block";

                            setTimeout(function (){
                                document.getElementById("idDupCheckSuccess").style.display = "none";
                            }, 3000);
                        }
                    }
                } else {
                    alert('ajax Request Error!');
                }
            }
        };
        //Post 방식, 응답은 json, 요청헤더 json
        httpRequest2.open('POST', 'signUp/dupCheckId', true);
        httpRequest2.responseType = "json";
        httpRequest2.setRequestHeader('Content-Type', 'application/json');
        httpRequest2.send(JSON.stringify(reqJson));
    });







    // 2022-0513 강경민
    // 인증하기 버튼 작동되게 작업
    var httpRequest3;

    document.getElementById("authNumCorrect").addEventListener('click', () => {

        var memberPhoneNum = document.getElementById("phoneCertification").value;
        var phoneCertificationNum = document.getElementById("phoneCertificationNum").value;
        var reqJson = new Object();
        reqJson.getPhoneNum = memberPhoneNum;
        reqJson.getphoneCertificationNum = phoneCertificationNum;
        httpRequest3 = new XMLHttpRequest();

        httpRequest3.onreadystatechange = () => {
            //readyState가 Done이고 응답 값이 200일 때
            if (httpRequest3.readyState === XMLHttpRequest.DONE) {
                if (httpRequest3.status === 200) {
                    var result = httpRequest3.response;

                    if(result.isCertificationNumMatch == "true") {
                        document.getElementById("CertificationNumUnMatch").style.display = "none";
                        document.getElementById("CertificationNumMatch").style.display = "block";

                        setTimeout(function () {
                            document.getElementById("CertificationNumMatch").style.display = "none";
                        }, 3000);
                    } else {
                        document.getElementById("CertificationNumMatch").style.display = "none";
                        document.getElementById("CertificationNumUnMatch").style.display = "block";

                        setTimeout(function () {
                            document.getElementById("CertificationNumUnMatch").style.display = "none";
                        }, 3000);
                    }
                } else {
                    alert('ajax Request Error!');
                }
            }
        };
        //Post 방식, 응답은 json, 요청헤더 json
        httpRequest3.open('POST', 'signUp/authNumCorrect', true);
        httpRequest3.responseType = "json";
        httpRequest3.setRequestHeader('Content-Type', 'application/json');
        httpRequest3.send(JSON.stringify(reqJson));
    });
}




function idDupCheckAndValidate(){
    var copyMemberId = document.getElementById("memberId").value;

    document.getElementById("memberId2").value = copyMemberId;
}