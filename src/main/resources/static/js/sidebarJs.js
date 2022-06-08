// 네비게이션 window.onclick 함수 뒤집어 씀
window.onload = function () {
    window.onclick = function(event) {
        if (!event.target.matches(['.dropdownBtn', '.dropdownArea'])) {
            console.log("here2");


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
                sidebarItemClick();
            }
        }
    }
}

function sidebarItemClick() {
    var sidebarItems = document.getElementsByClassName("sidebarItem");
    var i;
    for(i = 0; i < sidebarItems.length; i++){
        var item = sidebarItems[i];
        item.classList.remove('active');
    }

    event.target.classList.toggle('active');
}
