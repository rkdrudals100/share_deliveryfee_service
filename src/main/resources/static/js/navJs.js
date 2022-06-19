function profileDropdown() {
    DropdownSwitch("profileDropdown")
    document.getElementById("profileDropdown").classList.toggle("showDropdown");
}



function alertDropdown() {
    DropdownSwitch("alertDropdown")
    document.getElementById("alertDropdown").classList.toggle("showDropdown");
}



function DropdownSwitch(pressedDropdownBtn){
    var dropdowns = document.getElementsByClassName("customDropdown");
    var clickedElement = document.getElementById(pressedDropdownBtn);
    var i = 0;
    for(i = 0; i < dropdowns.length; i++){
        var openDropdown = dropdowns[i];
        if (openDropdown != clickedElement){
            openDropdown.classList.remove('showDropdown');
        }
    }
}



function notificationReadStatusChange(notificationLogId, url) {
    console.log('hihi');
    var httpRequest;

    var reqJson = new Object();
    reqJson.getNotificationLogId = notificationLogId.split('_')[1];

    httpRequest = new XMLHttpRequest();

    httpRequest.onreadystatechange = () => {
        //readyState가 Done이고 응답 값이 200일 때
        if (httpRequest.readyState === XMLHttpRequest.DONE) {
            if (httpRequest.status === 200) {
                var result = httpRequest.response;

                location.href = url;
            } else {
                alert('ajax Request Error!');
            }
        }
    };
    //Post 방식, 응답은 json, 요청헤더 json
    httpRequest.open('POST', '/notificationAlert', true);
    httpRequest.responseType = "json";
    httpRequest.setRequestHeader('Content-Type', 'application/json');
    httpRequest.send(JSON.stringify(reqJson));
}





window.onclick = function(event) {
    if (!event.target.matches(['.dropdownBtn', '.dropdownArea'])) {

        var dropdowns = document.getElementsByClassName("customDropdown");
        var i;
        for (i = 0; i < dropdowns.length; i++) {
            var openDropdown = dropdowns[i];
            if (openDropdown.classList.contains('showDropdown')) {
                openDropdown.classList.remove('showDropdown');
            }
        }
    }
}