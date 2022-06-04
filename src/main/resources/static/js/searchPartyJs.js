window.onload = function (){
    document.getElementById("currentPage").value = 1;

    firstCardSetting();
}







// 초기 card 출력
function firstCardSetting() {
    var totalPageNum = document.getElementById("totalCardNum").innerText;

    if (totalPageNum >= 6){
        for (var i = 0; i < 6; i++){
            document.getElementById("partyNum" + i).style.display = '';
        }
    } else {
        var i = 0;
        while (i < totalPageNum){
            document.getElementById("partyNum" + i).style.display = '';
            i++;
        }
    }

    if (Math.ceil(totalPageNum / 6) > 1){
        document.getElementById("toNextPage").disabled = false;
    } else{
        document.getElementById("toNextPage").disabled = true;
    }
    document.getElementById("toPreviewPage").disabled = true;
}






function searchByWord() {
    var httpRequest;

    var searchWord = document.getElementById("searchWord").value;
    var reqJson = new Object();
    reqJson.getSearchWord = searchWord;
    console.log(reqJson.getSearchWord);
    httpRequest = new XMLHttpRequest();

    httpRequest.onreadystatechange = () => {
        //readyState가 Done이고 응답 값이 200일 때
        if (httpRequest.readyState === XMLHttpRequest.DONE) {
            if (httpRequest.status === 200) {
                var result = httpRequest.response;
                document.getElementById("cards").innerHTML = result;

                document.getElementById("totalCardNum").innerText = document.getElementById("totalResultNum").innerText;
                document.getElementById("searchNum").innerText = document.getElementById("totalResultNum").innerText;
                document.getElementById("currentPage").value = 1;
                document.getElementById("currentPage").innerText = 1;
                document.getElementById("totalPage").innerText = Math.ceil(document.getElementById("totalResultNum").innerText / 6);
                document.getElementById("totalPage2").innerText = Math.ceil(document.getElementById("totalResultNum").innerText / 6);
                firstCardSetting();

                // 검색 결과가 없을 때
                if (document.getElementById("isNull").innerText == "true"){
                    document.getElementById("cards").innerHTML = "<div style='text-align: center'><span>검색 결과가 없습니다.</span></div>";
                    document.getElementById("totalCardNum").innerText = 0;
                    document.getElementById("searchNum").innerText = 0;
                }
            } else {
                alert('ajax Request Error!');
            }
        }
    };
    //Post 방식, 응답은 json, 요청헤더 json
    httpRequest.open('POST', '/searchParty/search', true);
    httpRequest.responseType = "text";
    httpRequest.setRequestHeader('Content-Type', 'application/json');
    httpRequest.send(JSON.stringify(reqJson));
}






function changePageCards(whichBtn){

    var httpRequest;

    var reqJson = new Object();
    reqJson.getCurrentPageNum = document.getElementById("currentPage").innerText;
    reqJson.getTotalPageNum = document.getElementById("totalCardNum").innerText;
    reqJson.whichBtn = whichBtn;
    httpRequest = new XMLHttpRequest();

    httpRequest.onreadystatechange = () => {
        //readyState가 Done이고 응답 값이 200일 때
        if (httpRequest.readyState === XMLHttpRequest.DONE) {
            if (httpRequest.status === 200) {
                var result = httpRequest.response;

                if (whichBtn == "right"){
                    for (var i = 0; i < 6; i++){
                        document.getElementById("partyNum" + String(((reqJson.getCurrentPageNum - 1) * 6) - i - 1)).style.display = 'none';
                    }
                    for (i = 0; i < result.printCardNum; i++){
                        document.getElementById("partyNum" + String(((reqJson.getCurrentPageNum - 1) * 6) + i)).style.display = '';
                    }
                } else {
                    for (var i = 0; i < result.printCardNum; i++){
                        document.getElementById("partyNum" + String(((reqJson.getCurrentPageNum - 1) * 6) + i)).style.display = '';
                    }
                    if ((parseInt(reqJson.getCurrentPageNum) + 1) * 6 >= parseInt(reqJson.getTotalPageNum)){
                        for (i = 0; i < parseInt(reqJson.getTotalPageNum) - (parseInt(reqJson.getCurrentPageNum) * 6); i++){
                            document.getElementById("partyNum" + String((reqJson.getCurrentPageNum * 6) + i)).style.display = 'none';
                        }
                    } else{
                        for (var i = 0; i < 6; i++){
                            document.getElementById("partyNum" + String((reqJson.getCurrentPageNum * 6) + i)).style.display = 'none';
                        }
                    }
                }
                window.scrollTo(0, 0);

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
    var totalpage = parseInt(document.getElementById("totalPage").innerText);
    var currentPage = parseInt(document.getElementById("currentPage").value);

    if (currentPage > 0 && currentPage < totalpage){
        document.getElementById("toPreviewPage").disabled = false;
        document.getElementById("currentPage").value = currentPage + 1;
        document.getElementById("currentPage").innerText = currentPage + 1;
        currentPage++;
    }

    if (currentPage >= totalpage){
        document.getElementById("toNextPage").disabled = true;
    }
    changePageCards("right");
}








function previewPage(){
    var currentPage = document.getElementById("currentPage").value;

    if (currentPage > 1){
        document.getElementById("toNextPage").disabled = false;
        document.getElementById("currentPage").value = currentPage - 1;
        document.getElementById("currentPage").innerText = currentPage - 1;
        currentPage--;
    }

    if(currentPage <= 1){
        document.getElementById("toPreviewPage").disabled = true;
    }
    changePageCards("left");
}