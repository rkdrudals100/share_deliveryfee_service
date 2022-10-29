window.onload = function (){
    document.getElementById("currentPage").value = 1;

    firstCardSetting();
}







// 초기 card 출력
function firstCardSetting(isnull) {
    var totalPageNum = document.getElementById("totalCardNum").innerText;

    if (isnull != 'true') {
        if (totalPageNum >= 6) {
            for (var i = 0; i < 6; i++) {
                document.getElementById("partyNum" + i).style.display = '';
            }
        } else {
            var i = 0;
            while (i < totalPageNum) {
                document.getElementById("partyNum" + i).style.display = '';
                i++;
            }
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

    console.log('검색어: ' + searchWord);
    httpRequest = new XMLHttpRequest();

    httpRequest.onreadystatechange = () => {
        //readyState가 Done이고 응답 값이 200일 때
        if (httpRequest.readyState === XMLHttpRequest.DONE) {
            if (httpRequest.status === 200) {
                var result = httpRequest.response;
                document.getElementById("cards").innerHTML = result;

                var isNull = document.getElementById("isNull").innerText;

                document.getElementById("totalCardNum").innerText = document.getElementById("totalResultNum").innerText;
                document.getElementById("searchNum").innerText = document.getElementById("totalResultNum").innerText;
                document.getElementById("currentPage").value = 1;
                document.getElementById("currentPage").innerText = 1;
                document.getElementById("totalPage").innerText = Math.ceil(document.getElementById("totalResultNum").innerText / 6);
                document.getElementById("totalPage2").innerText = Math.ceil(document.getElementById("totalResultNum").innerText / 6);
                firstCardSetting(isNull);

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
    httpRequest.open('GET', '/search/party?keyWord=' + searchWord);
    httpRequest.responseType = "text";
    httpRequest.send();
}






function changePageCards(whichBtn){

    var httpRequest;

    var getCurrentPageNum = document.getElementById("currentPage").innerText;
    var getTotalPageNum = document.getElementById("totalCardNum").innerText;

    httpRequest = new XMLHttpRequest();

    httpRequest.onreadystatechange = () => {
        //readyState가 Done이고 응답 값이 200일 때
        if (httpRequest.readyState === XMLHttpRequest.DONE) {
            if (httpRequest.status === 200) {
                var result = httpRequest.response;

                if (whichBtn == "right"){
                    for (var i = 0; i < 6; i++){
                        document.getElementById("partyNum" + String(((getCurrentPageNum - 1) * 6) - i - 1)).style.display = 'none';
                    }
                    for (i = 0; i < result.printCardNum; i++){
                        document.getElementById("partyNum" + String(((getCurrentPageNum - 1) * 6) + i)).style.display = '';
                    }
                } else {
                    for (var i = 0; i < result.printCardNum; i++){
                        document.getElementById("partyNum" + String(((getCurrentPageNum - 1) * 6) + i)).style.display = '';
                    }
                    if ((getCurrentPageNum + 1) * 6 >= getTotalPageNum){
                        for (i = 0; i < getTotalPageNum - (getCurrentPageNum * 6); i++){
                            document.getElementById("partyNum" + String((getCurrentPageNum * 6) + i)).style.display = 'none';
                        }
                    } else{
                        for (var i = 0; i < 6; i++){
                            document.getElementById("partyNum" + String((getCurrentPageNum * 6) + i)).style.display = 'none';
                        }
                    }
                }
                window.scrollTo(0, 0);

            } else {
                alert('ajax Request Error!');
            }
        }
    };

    httpRequest.open('GET', 'search/party/page/' + getCurrentPageNum + '?totalPageNum=' + getTotalPageNum + '&whichBtn=' + whichBtn);
    httpRequest.responseType = "json";
    httpRequest.send();
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