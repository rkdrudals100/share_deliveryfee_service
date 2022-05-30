window.onload = function (){
    document.getElementById("currentPage").value = 1;
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