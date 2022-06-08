// 네비게이션 window.onclick 함수 뒤집어 씀
window.onload = function () {





    window.onclick = function(event) {
        if (!event.target.matches(['.dropdownBtn', '.dropdownArea'])) {
            console.log("here2");
            console.log(event.target);

            var dropdowns = document.getElementsByClassName("customDropdown");
            var i;
            for (i = 0; i < dropdowns.length; i++) {
                var openDropdown = dropdowns[i];
                if (openDropdown.classList.contains('showDropdown')) {
                    openDropdown.classList.remove('showDropdown');
                }
            }

            if (event.target.matches('.sidebarItem')){
                console.log('sidebarItemClicked');
                sidebarItemClick(event.target);
            }
        }
    }
}






function sidebarItemClick(clickedEle) {
    var sidebarItems = document.getElementsByClassName("sidebarItem");
    var i;

    for(i = 0; i < sidebarItems.length; i++){
        var item = sidebarItems[i];
        var content = document.getElementById(item.id.split('-')[0]);
        item.classList.remove('active');
        console.log(item.id.split('-')[0]);
        if (content.id == clickedEle.id.split('-')[0]){
            console.log("find");
            content.style.display = 'block';
            console.log(content);
        } else {
            content.style.display = 'none';
        }
    }

    clickedEle.classList.toggle('active');
}






function contentSetting(selectedTab) {
    if (selectedTab == 'myParties'){
        document.getElementById('myParties').style.display = 'block';
    } else if (selectedTab == 'notification'){
        document.getElementById('myParties-tab').classList.toggle('active');
        document.getElementById('notification-tab').classList.toggle('active');
        document.getElementById('myParties').style.display = 'none';
        document.getElementById('notification').style.display = 'block';
    } else if (selectedTab == 'profile'){
        document.getElementById('myParties-tab').classList.toggle('active');
        document.getElementById('profile-tab').classList.toggle('active');
        document.getElementById('myParties').style.display = 'none';
        document.getElementById('profile').style.display = 'block';
    }
}