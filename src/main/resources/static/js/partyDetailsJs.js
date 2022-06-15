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
                console.log(messageId);
                console.log(choice);

            } else {
                alert('ajax Request Error!');
            }
        }
    };
    //Post 방식, 응답은 json, 요청헤더 json
    httpRequest.open('POST', '/partyDetails/' + partyId +'/PartyJoin', true);
    httpRequest.responseType = "json";
    httpRequest.setRequestHeader('Content-Type', 'application/json');
    httpRequest.send(JSON.stringify(reqJson));
}