function aboutjoinPartyResponse(messageId, choice) {
    var httpRequest;

    var partyId = document.getElementById('partyId').innerText;
    var reqJson = new Object();
    reqJson.getPartyMessageId = messageId;
    reqJson.getChoice = choice;

    httpRequest = new XMLHttpRequest();

    httpRequest.onreadystatechange = () => {
        //readyState가 Done이고 응답 값이 200일 때
        if (httpRequest.readyState === XMLHttpRequest.DONE) {
            if (httpRequest.status === 200) {
                var result = httpRequest.response;

                window.location.replace('/party/' + partyId);
            } else {
                alert('ajax Request Error!');
            }
        }
    };
    //Post 방식, 응답은 json, 요청헤더 json
    httpRequest.open('POST', '/party/' + partyId +'/joined/user', true);
    httpRequest.responseType = "json";
    httpRequest.setRequestHeader('Content-Type', 'application/json');
    httpRequest.send(JSON.stringify(reqJson));
}





function partyStatusBtnClicked(text, btnNum){
    document.getElementById('partyStatusChangeModalLabel').innerText = "'" + text + "'" + '로 변경하기';
    document.getElementById('partyStatusBtn_' + btnNum).classList.toggle('btn-outline-primary');

    var i = 0;
    for (i; i < 4; i++){
        document.getElementById('partyStatusBtn_' + i).classList.remove('btn-primary');
        document.getElementById('partyStatusBtn_' + i).classList.remove('btn-outline-primary');
        if (i == btnNum){
            document.getElementById('partyStatusBtn_' + i).classList.add('btn-primary');
        } else {
            document.getElementById('partyStatusBtn_' + i).classList.add('btn-outline-primary');
        }
    }
}




function partyStatusChangeApply() {

    var httpRequest;

    var partyId = document.getElementById('partyId').innerText;
    var clickedBtn = document.getElementsByClassName('statusBtn btn-primary');
    var reqJson = new Object();
    reqJson.getClickedBtn = clickedBtn.item(0).innerText;

    httpRequest = new XMLHttpRequest();

    httpRequest.onreadystatechange = () => {
        //readyState가 Done이고 응답 값이 200일 때
        if (httpRequest.readyState === XMLHttpRequest.DONE) {
            if (httpRequest.status === 200) {
                var result = httpRequest.response;

                document.getElementById('partyStatusText').innerText = result.partyStatusDescription;

            } else {
                alert('ajax Request Error!');
            }
        }
    };
    //Post 방식, 응답은 json, 요청헤더 json
    httpRequest.open('PATCH', '/party/' + partyId +'/partyStatus', true);
    httpRequest.responseType = "json";
    httpRequest.setRequestHeader('Content-Type', 'application/json');
    httpRequest.send(JSON.stringify(reqJson));
}