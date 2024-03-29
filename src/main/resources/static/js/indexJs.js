window.onpageshow = function (){
    setTimeout(function() {
        window.scrollTo({left: 0, behavior: "instant"});
    }, 0);
}




window.addEventListener('scroll', function (){

    let value = window.scrollY;
    console.log(value);
    // window.scrollTo({left: 0, behavior: "instant"});

    // section animation
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

    if (value < 800 || value > 1900){
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

    // downArrow 화살표 없애기
    if (value > 100){
        document.getElementById("downArrows1").disabled = true;
        document.getElementById("downArrows1").style.display = "none";
    }
    if (value > 800){
        document.getElementById("downArrows2").disabled = true;
        document.getElementById("downArrows2").style.display = "none";
    }
    if (value > 1500){
        document.getElementById("downArrows3").disabled = true;
        document.getElementById("downArrows3").style.display = "none";
    }
})




function downArrowActive(sectionNum) {
    if (sectionNum == 1){
        window.scrollTo({left: 0, top: 700});
    } else if (sectionNum == 2){
        window.scrollTo({left: 0, top: 1400});
    } else if (sectionNum == 3){
        window.scrollTo({left: 0, top: 2100});
    }
}

















