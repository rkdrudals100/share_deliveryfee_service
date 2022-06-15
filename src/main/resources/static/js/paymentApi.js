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
            location.href='/partyDetails/' + partyId + '/paymentSuccess';
        } else {    // 결제 실패 시 로직
            var msg = '결제에 실패하였습니다.';
            msg += '에러내용 : ' + rsp.error_msg;
            console.log(msg);
        }
    });
}


function testfuc() {
    var partyId = document.getElementById('partyId').innerText;
    location.href='/partyDetails/' + partyId + '/paymentSuccess';
}

