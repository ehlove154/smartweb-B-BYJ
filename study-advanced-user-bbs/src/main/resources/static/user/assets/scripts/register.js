const $registerForm = document.getElementById('registerForm');
const $step = $registerForm.querySelector(`:scope > .step`);
const $stepItems = Array.from($step.querySelectorAll(`:scope > .item`));
const $termContainer = $registerForm.querySelector(`:scope > .container.term`);
const $infoContainer = $registerForm.querySelector(`:scope > .container.info`);
const $completeContainer = $registerForm.querySelector(`:scope > .container.complete`);
const $containers = [$termContainer, $infoContainer, $completeContainer];
let currentStep = 0;
const emailRegex = new RegExp('^(?=.{8,50}$)([\\da-z\\-_.]{4,})@([\\da-z][\\da-z\\-]*[\\da-z]\\.)?([\\da-z][\\da-z\\-]*[\\da-z])\\.([a-z]{2,15})(\\.[a-z]{2,3})?$');
const passwordRegex = new RegExp('^([\\da-zA-Z`~!@#$%^&*()\\-_=+\\[{\\]}\\\\|;:\'",<.>/?]{8,50})$');
const nicknameRegex = new RegExp('^([\\da-zA-Z가-힣]{2,10})$');

$registerForm['emailCodeSendButton'].addEventListener('click', () => {
    const $emailLabel = $registerForm.querySelector('.--object-label:has(input[name="email"])');
    if ($registerForm['email'].value === '') {
        $emailLabel.setValid(false, '이메일을 입력해 주세요.');
        $registerForm['email'].focus();
        return;
    }
    if (!emailRegex.test($registerForm['email'].value)) {
        $emailLabel.setValid(false, '올바른 이메일을 입력해 주세요.');
        $registerForm['email'].focus();
        $registerForm['email'].select();
        return;
    }
});

$registerForm['previous'].onclick = () => {
    currentStep -= 2;
    $registerForm.dispatchEvent(new Event('submit'));
};

$registerForm.onsubmit = (e) => {
    e.preventDefault();

    if (currentStep === 0) {
        if (!$registerForm['agreeServiceTerm'].checked) {
            dialog.showSimpleOk('회원가입', '서비스 이용약관에 동의해 주세요.');
            return;
        }
        if (!$registerForm['agreePrivacyTerm'].checked) {
            dialog.showSimpleOk('회원가입', '개인정보 처리방침에 동의해 주세요.');
            return;
        }
    } else if (currentStep === 1) {

    }

    currentStep++; // 1
    $registerForm['previous'].setAttribute('disabled', '');
    switch (currentStep) {
        case 0:
            break;
        case 1:
            $registerForm['previous'].removeAttribute('disabled');
            break;
        case 2:
            $registerForm.querySelector(`a[href="/user/login"]`).style.display = 'none';
            $registerForm['submit'].querySelector(':scope > .---caption').innerText = '로그인';
            break;
        default:
            location.href = '/user/login';
            return;
    }
    $stepItems.forEach(($item) => $item.classList
        .remove('-selected'));
    $stepItems[currentStep].classList.add('-selected');
    $containers.forEach(($container) => $container.classList.remove('-visible'));
    $containers[currentStep].classList.add('-visible');
};