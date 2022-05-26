window.addEventListener('scroll', function (){

    let value = window.scrollY;
    console.log(value);

    if (value > 400){
        document.getElementById("word").style.animation = "disappear 0.5s ease-out forwards";
        document.getElementById("serviceStartBtn").style.animation = "disappear 0.5s ease-out forwards";
    } else {
        document.getElementById("word").style.animation = "appear 0.5s ease-out forwards";
        document.getElementById("serviceStartBtn").style.animation = "appear 0.5s ease-out forwards";
    }

    if (value < 0 || value > 1150){
        document.getElementById("shareImages").style.animation = "leftSlideDisappear 1s ease-out forwards";
        document.getElementById("title2").style.animation = "rightSlideDisappear 1s ease-out forwards";
        document.getElementById("title2Sub1").style.animation = "rightSlideDisappear 1s ease-out forwards";
    } else {
        document.getElementById("shareImages").style.animation = "leftSlideAppear 1s ease-out forwards";
        document.getElementById("title2").style.animation = "rightSlideAppear 1s ease-out forwards";
        document.getElementById("title2Sub1").style.animation = "rightSlideAppear 1s ease-out forwards";
    }

    if (value < 800 || value > 1600){
        document.getElementById("searchPartyImage").style.animation = "leftSlideDisappear2 1s ease-out forwards";
        document.getElementById("title3").style.animation = "rightSlideDisappear 1s ease-out forwards";
        document.getElementById("title3Sub1").style.animation = "rightSlideDisappear 1s ease-out forwards";
        document.getElementById("serviceStartBtn2").style.animation = "disappear 1s ease-out forwards";
    } else {
        document.getElementById("searchPartyImage").style.animation = "leftSlideAppear2 1s ease-out forwards";
        document.getElementById("title3").style.animation = "rightSlideAppear 1s ease-out forwards";
        document.getElementById("title3Sub1").style.animation = "rightSlideAppear 1s ease-out forwards";
        document.getElementById("serviceStartBtn2").style.animation = "appear 1s ease-out forwards";
    }

    if (value > 1750){
        document.getElementById("sequenceCard1").style.animation = "appear 1s ease-out forwards";
        document.getElementById("line1").style.animation = "appear 1s ease-out forwards";
        document.getElementById("line1").style.animationDelay = "0.2s";
        document.getElementById("sequenceCard2").style.animation = "appear 1s ease-out forwards";
        document.getElementById("sequenceCard2").style.animationDelay = "0.2s";
        document.getElementById("line2").style.animation = "appear 1s ease-out forwards";
        document.getElementById("line2").style.animationDelay = "0.4s";
        document.getElementById("sequenceCard3").style.animation = "appear 1s ease-out forwards";
        document.getElementById("sequenceCard3").style.animationDelay = "0.4s";
        document.getElementById("sequenceCard4").style.animation = "appear 1s ease-out forwards";
        document.getElementById("sequenceCard4").style.animationDelay = "0.6s";

        document.getElementById("title4").style.animation = "rightSlideAppear 1s ease-out forwards";
        document.getElementById("title4").style.animationDelay = "0.8s";
        document.getElementById("title4Sub1").style.animation = "rightSlideAppear 1s ease-out forwards";
        document.getElementById("title4Sub1").style.animationDelay = "0.8s";

        document.getElementById("makePartyBtn").style.animation = "appear 0.5s ease-out forwards";
        document.getElementById("makePartyBtn").style.animationDelay = "1.5s";
    }

})