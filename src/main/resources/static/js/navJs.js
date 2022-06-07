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