function profileDropdown() {
    DropdownSwitch("profileDropdown")
    document.getElementById("profileDropdown").classList.toggle("showDropdown");
}



function alertDropdown() {
    DropdownSwitch("alertDropdown")
    document.getElementById("alertDropdown").classList.toggle("showDropdown");
}



function DropdownSwitch(pressedDropdownBtn){
    if (pressedDropdownBtn == "profileDropdown"){
        document.getElementById("alertDropdown").classList.remove("showDropdown");
    } else if(pressedDropdownBtn == "alertDropdown"){
        document.getElementById("profileDropdown").classList.remove("showDropdown");
    }
}



window.onclick = function(event) {
    if (!event.target.matches(['.dropdownBtn', '.dropdownArea'])) {
        console.log("here");

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