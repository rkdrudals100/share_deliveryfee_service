function requestPay(whichOrder) {

    var partyId = document.getElementById('partyId').innerText;
    var memberId = document.getElementById("navMemberId").innerText;
    var memberEmail = document.getElementById("navMemberEmail").innerText;
    var payment = document.getElementById("payment").value;
    var deliveryfee = document.getElementById("deliveryfee").value;
    var orderName = "none";

    // 결제 종류 판별
    if (whichOrder == "requestToJoin") {
        orderName = "파티원(" + memberId + ")주문 요청";
    } else {
        orderName = "none";
    }

    var IMP = window.IMP;
    IMP.init("imp55139997");

    // IMP.request_pay(param, callback) 결제창 호출
    IMP.request_pay({ // param
        pg: "html5_inicis",
        pay_method: "card",
        merchant_uid: "order_" + new Date().getTime(), //관리할 식별 번호
        name: orderName,
        amount: parseInt(payment) + parseInt(deliveryfee),
        buyer_email: memberEmail,
        buyer_name: "",
        buyer_tel: "",
        buyer_addr: "",
        buyer_postcode: ""
    }, function (rsp) { // callback
        if (rsp.success) {  // 결제 성공 시 로직
            var msg = '결제가 완료되었습니다.';
            msg += '고유ID : ' + rsp.imp_uid + '\n';
            msg += '상점 거래ID : ' + rsp.merchant_uid + '\n';
            msg += '결제 금액 : ' + rsp.paid_amount+ '\n';
            msg += '카드 승인번호 : ' + rsp.apply_num+ '\n';
            msg += '결제 이름 : ' + rsp.name+ '\n';
            msg += '결제 이름 : ' + rsp.buyer_email+ '\n';

            console.log(msg);
            sendJoinPartyMessageAjax(rsp.merchant_uid, rsp.paid_amount, memberId, partyId);
        } else {    // 결제 실패 시 로직
            var msg = '결제에 실패하였습니다.';
            msg += '에러내용 : ' + rsp.error_msg;
            console.log(msg);
        }
    });
}


// function testfuc() {
//     sendJoinPartyMessageAjax();
// }



function sendJoinPartyMessageAjax(merchant_uid, amount, payerName, partyId) {
    var httpRequest;

    // var partyId = document.getElementById('partyId').innerText;

    console.log(merchant_uid + ' ' + amount + ' ' + payerName + ' ' + partyId);
    var reqJson = new Object();
    reqJson.getMessageBody = document.getElementById('orderRequests').value;
    reqJson.getServiceFee = document.getElementById('payment').value;
    reqJson.getDeliveryFee = document.getElementById('deliveryfee').value;
    reqJson.getMerchant_uid = merchant_uid;
    reqJson.getAmount = amount;
    reqJson.getPayerName = payerName;
    reqJson.getPartyId = partyId;

    console.log('body: ' + reqJson.getMessageBody);
    httpRequest = new XMLHttpRequest();

    httpRequest.onreadystatechange = () => {
        //readyState가 Done이고 응답 값이 200일 때
        if (httpRequest.readyState === XMLHttpRequest.DONE) {
            if (httpRequest.status === 200) {
                var result = httpRequest.response;
                document.getElementById('modalClose').click();
            } else {
                alert('ajax Request Error!');
            }
        }
    };
    //Post 방식, 응답은 json, 요청헤더 json
    // httpRequest.open('POST', '/partyDetails/' + partyId + '/paymentSuccess', true);
    httpRequest.open('POST', '/payment/partyjoin', true);
    httpRequest.responseType = "json";
    httpRequest.setRequestHeader('Content-Type', 'application/json');
    httpRequest.send(JSON.stringify(reqJson));
}
