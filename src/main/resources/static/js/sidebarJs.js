// 네비게이션 window.onclick 함수 뒤집어 씀
window.onload = function () {





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

            if (event.target.matches('.sidebarItem')){
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

        if (content.id == clickedEle.id.split('-')[0]){
            content.style.display = 'block';
            console.log('sidebarItemClicked ' + item.id.split('-')[0]);
        } else {
            content.style.display = 'none';
        }
    }

    clickedEle.classList.toggle('active');
}






function contentSetting(selectedTab) {
    document.getElementById('myParties-tab').classList.toggle('active');
    document.getElementById(selectedTab + '-tab').classList.toggle('active');
    document.getElementById(selectedTab).style.display = 'block';
}