document.querySelector('video').playbackRate = 0.2; // 배경화면 비디오

let tyInt1;
let tyInt2;

setTimeout(function () {
    let typingBool1 = false;
    let typingIdx1 = 0;
    let typingTxt1 = $(`#name1`).text();

    typingTxt1 = typingTxt1.split(``);

    if (typingBool1 === false) {
        typingBool1 = true;

        const tyInt = setInterval(typing1, 100);
    }

    function typing1() {
        if (typingIdx1 < typingTxt1.length) {
            $(`.name-txt1`).append(typingTxt1[typingIdx1]);
            typingIdx1++;
        } else {
            clearInterval(tyInt1);
        }
    }
}, 1000);

setTimeout( function () {
    let typingBool2 = false;
    let typingIdx2 = 0;
    let typingTxt2 = $(`#name2`).text();

    typingTxt2 = typingTxt2.split(``);

    if (typingBool2 === false) {
        typingBool2 = true;

        const tyInt = setInterval(typing2, 100);
    }

    function typing2() {
        if (typingIdx2 < typingTxt2.length) {
            $(`.name-txt2`).append(typingTxt2[typingIdx2]);
            typingIdx2++;
        } else {
            clearInterval(tyInt2);
        }
    }
}, 5500); //타이핑 텍스트 효과

const intersectionObserver = new IntersectionObserver((entries) => {
    entries.forEach((entry) => {
        if (entry.isIntersecting === true) {
            entry.target.classList.add(`visible`);
        } else {
            entry.target.classList.remove(`visible`);
        }
    });
}, {
    rootMargin: `-50px`
});

const $boxes = Array.from(document.body.querySelectorAll(`.box`));
$boxes.forEach(($box) => intersectionObserver.observe($box)); // 스크롤 시 element 하나씩 호출
