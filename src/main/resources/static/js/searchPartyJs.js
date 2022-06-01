window.onload = function (){
    document.getElementById("currentPage").value = 1;


    
    // 초기 card 출력
    var totalPageNum = document.getElementById("totalCardNum").innerText;

    if (totalPageNum >= 6){
        for (var i = 0; i < 6; i++){
            document.getElementById("partyNum" + i).style.display = '';
        }
    } else {
        var i = 0;
        while (i < totalPageNum){
            document.getElementById("partyNum" + i).style.display = '';
        }
    }
}


function changePageCards(whichBtn){

    var httpRequest;

    console.log("listenerClicked");

    var reqJson = new Object();
    reqJson.getCurrentPageNum = document.getElementById("currentPage").innerText;
    reqJson.getTotalPageNum = totalPageNum;
    reqJson.whichBtn = whichBtn;
    httpRequest = new XMLHttpRequest();

    httpRequest.onreadystatechange = () => {
        //readyState가 Done이고 응답 값이 200일 때
        if (httpRequest.readyState === XMLHttpRequest.DONE) {
            if (httpRequest.status === 200) {
                var result = httpRequest.response;

                console.log(result.printCardNum);



            } else {
                alert('ajax Request Error!');
            }
        }
    };
    //Post 방식, 응답은 json, 요청헤더 json
    httpRequest.open('POST', 'changePageCards', true);
    httpRequest.responseType = "json";
    httpRequest.setRequestHeader('Content-Type', 'application/json');
    httpRequest.send(JSON.stringify(reqJson));
}




function nextPage(){
    var totalpage = document.getElementById("totalPage").innerText;
    var currentPage = document.getElementById("currentPage").value;

    if (currentPage > 0 && currentPage < totalpage){
        document.getElementById("toPreviewPage").disabled = false;
        document.getElementById("currentPage").value = currentPage + 1;
        document.getElementById("currentPage").innerText = currentPage + 1;
    }
    if (currentPage <= totalpage){
        document.getElementById("toNextPage").disabled = true;
    }
    // console.log(currentPage + " " + totalpage);
    var whichBtn = "right";
    changePageCards(whichBtn);
}





function previewPage(){
    var currentPage = document.getElementById("currentPage").value;

    if (currentPage > 1){
        document.getElementById("toNextPage").disabled = false;
        document.getElementById("currentPage").value = currentPage - 1;
        document.getElementById("currentPage").innerText = currentPage - 1;
    }
    if(currentPage <= 1){
        document.getElementById("toPreviewPage").disabled = true;
    }
    // console.log(currentPage);
}