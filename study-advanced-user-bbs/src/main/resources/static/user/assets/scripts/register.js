const $loading = document.getElementById('loading');
const $registerForm = document.getElementById('registerForm');
const $step = $registerForm.querySelector(`:scope > .step`);
const $stepItems = Array.from($step.querySelectorAll(`:scope > .item`));
const $termContainer = $registerForm.querySelector(`:scope > .container.term`);
const $infoContainer = $registerForm.querySelector(`:scope > .container.info`);
const $completeContainer = $registerForm.querySelector(`:scope > .container.complete`);
const $containers = [$termContainer, $infoContainer, $completeContainer];
let currentStep = 0;
const emailRegex = new RegExp('^(?=.{8,50}$)([\\da-z\\-_.]{4,})@([\\da-z][\\da-z\\-]*[\\da-z]\\.)?([\\da-z][\\da-z\\-]*[\\da-z])\\.([a-z]{2,15})(\\.[a-z]{2,3})?$');
const emailCodeRegex = new RegExp('^(\\d{6})$');
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

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('email', $registerForm['email'].value);
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        $loading.hide();
        if (xhr.status < 200 || xhr.status >= 300) {
            dialog.showSimpleOk('인증번호 전송', '요청을 처리하는 도중 오류가 발생하였습니다.\n잠시 후 다시 시도해 주세요.');
            return;
        }
        const response = JSON.parse(xhr.responseText);
        switch (response.result) {
            case 'failure_duplicate':
                dialog.showSimpleOk('인증번호 전송', `입력하신 이메일 '${$registerForm['email'].value}' 은/는 이미 사용 중입니다.`, {
                    onOkCallback: () => {
                        $registerForm['email'].focus();
                        $registerForm['email'].select();
                    }
                });
                break;
            case 'success':
                $registerForm['emailSalt'].value = response.salt;
                $registerForm['email'].setDisabled(true);
                $registerForm['emailCodeSendButton'].setDisabled(true);
                $registerForm['emailCode'].setDisabled(false);
                $registerForm['emailCodeVerifyButton'].setDisabled(false);
                $registerForm['emailCode'].focus();
                dialog.showSimpleOk('인증번호 전송', '입력하신 이메일로 인증번호를 전송하였습니다.\n해당 인증번호는 10분간만 유효하니 유의해 주세요.');
                break;
            default:
                dialog.showSimpleOk('인증번호 전송', '알 수 없는 이유로 인증번호를 전송하지 못하였습니다.\n잠시 후 다시 시도해 주세요.');
        }

    };
    xhr.open('POST', '/user/register-email');
    xhr.send(formData);
    $loading.show();
});

$registerForm['emailCodeVerifyButton'].addEventListener('click', () => {
    const $emailLabel = $registerForm.querySelector('.--object-label:has(input[name="emailCode"])');

    if ($registerForm['emailCode'].value === '') {
        $emailLabel.setValid(false, '인증번호를 입력해 주세요.')
        $registerForm['emailCode'].focus();
        return;
    }
    if (!emailCodeRegex.test($registerForm['emailCode'].value)) {
        $emailLabel.setValid(false, '올바른 인증번호를 입력해 주세요.')
        $registerForm['emailCode'].focus();
        $registerForm['emailCode'].select();
        return;
    }
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('email', $registerForm['email'].value);
    formData.append('code', $registerForm['emailCode'].value);
    formData.append('salt', $registerForm['emailSalt'].value);
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            dialog.showSimpleOk('인증번호 확인', '요청을 처리하는 도중 오류가 발생하였습니다.\n잠시 후 다시 시도해 주세요.');
            return;
        }
        const response = JSON.parse(xhr.responseText);
        switch (response.result) {
            case 'failure_expired':
                $registerForm['emailSalt'].value = '';
                $registerForm['email'].setDisabled(false);
                $registerForm['emailCodeSendButton'].setDisabled(false);
                $registerForm['emailCode'].value = '';
                $registerForm['emailCode'].setDisabled(true);
                $registerForm['emailCodeVerifyButton'].setDisabled(true);
                $registerForm['email'].focus();
                dialog.showSimpleOk('인증번호 확인', '인증 정보가 만료되었습니다.\n이메일 인증을 다시 진행해 주세요.');
                break;
            case 'success':
                $registerForm['emailCode'].setDisabled(true);
                $registerForm['emailCodeVerifyButton'].setDisabled(true);
                dialog.showSimpleOk('인증번호 확인', '이메일이 정상적으로 인증되었습니다.');
                break;
            default:
                dialog.showSimpleOk('인증번호 확인', '인증번호가 올바르지 않습니다.\n다시 확인해 주세요.', () => {
                    $registerForm['emailCode'].focus();
                    $registerForm['emailCode'].select();
                });
        }
    };
    xhr.open('PATCH', '/user/register-email');
    xhr.send(formData);
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