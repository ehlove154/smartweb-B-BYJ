const $registerForm = document.getElementById('registerForm');
const emailRegex = new RegExp('^(?=.{8,50}$)([\\da-z\\-_.]{4,})@([\\da-z][\\da-z\\-]*[\\da-z]\\.)?([\\da-z][\\da-z\\-]*[\\da-z])\\.([a-z]{2,15})(\\.[a-z]{2,3})?$');
const passwordRegex = new RegExp('^([\\da-zA-Z`~!@#$%^&*()\\-_=+\\[{\\]}\\\\|;:\'",<.>/?]{8,50})$');
const nicknameRegex = new RegExp('^([\\da-zA-Z가-힣]{2,10})$');

$registerForm.onsubmit = (e) => {
    e.preventDefault();
    $registerForm['email'].setValid(true);
    $registerForm['password'].setValid(true);
    $registerForm['passwordCheck'].setValid(true);
    $registerForm['nickname'].setValid(true);

    $registerForm['email'].style.filter = '';
    $registerForm['email'].nextElementSibling.style.filter = '';
    $registerForm['nickname'].style.filter = '';
    $registerForm['nickname'].nextElementSibling.style.filter = '';

    if ($registerForm['email'].value === '') {
        $registerForm['email'].focusAndSelect()
            .setValid(false)
            .nextElementSibling.innerText = '이메일을 입력해주세요.';
        return;
    }
    if (!emailRegex.test($registerForm['email'].value)) {
        // $registerForm['emil'].parentElement.querySelector(':scope > .warning').innerText = '올바르지 않은 이메일 형식입니다.';
        // $registerForm['email'].nextElementSibling.innerText = '올바르지 않은 이메일 형식입니다.';
        // $registerForm['email'].setValid(false);
        $registerForm['email']
            .focusAndSelect()
            .setValid(false)
            .nextElementSibling.innerText = '올바르지 않은 이메일 형식입니다.';
        return;
    }
    if ($registerForm['password'].value === '') {
        $registerForm['password'].focusAndSelect()
            .setValid(false)
            .nextElementSibling.innerText = '비밀번호를 입력해주세요.';
        return;
    }
    if (!passwordRegex.test($registerForm['password'].value)) {
        $registerForm['password'].focusAndSelect()
            .setValid(false)
            .nextElementSibling.innerText = '올바르지 않은 비밀번호 형식입니다.';
        return;
    }
    if ($registerForm['passwordCheck'].value === '') {
        $registerForm['passwordCheck'].focusAndSelect()
            .setValid(false)
            .nextElementSibling.innerText = '비밀번호를 한 번 더 입력해주세요.';
        return;
    }
    if ($registerForm['password'].value !== $registerForm['passwordCheck'].value) {
        $registerForm['passwordCheck'].focusAndSelect()
            .setValid(false)
            .nextElementSibling.innerText = '비밀번호가 일치하지 않습니다.';
        return;
    }
    if ($registerForm['nickname'].value === '') {
        $registerForm['nickname'].focusAndSelect()
            .setValid(false)
            .nextElementSibling.innerText = '닉네임을 입력해주세요.';
        return;
    }
    if (!nicknameRegex.test($registerForm['nickname'].value)) {
        $registerForm['nickname'].focusAndSelect()
            .setValid(false)
            .nextElementSibling.innerText = '올바르지 않은 닉네임 형식입니다.';
        return;
    }
    if (!$registerForm['agreeTerm'].checked) {
        dialog.showSimpleOk('회원가입', '서비스 이용약관을 읽고 동의해 주세요.', () => {
            $registerForm['agreeTerm'].parentElement.scrollIntoView({
                behavior: 'smooth',
                block: 'center' // 수직 가운데 이동
            });
        });
        return;
    }

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('email', $registerForm['email'].value);
    formData.append('password', $registerForm['password'].value);
    formData.append('nickname', $registerForm['nickname'].value);
    if ($registerForm['birth'].value !== '') {
        formData.append('birth', $registerForm['birth'].value);
    }
    formData.append('isMarketingChecked', $registerForm['agreeMarketing'].checked);

    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            dialog.showSimpleOk('회원가입', `[${xhr.status}]\n회원가입 도중 오류가 발생 하였습니다.\n잠시 후 다시 시도해 주세요.`)
            return;
        }
        const response = JSON.parse(xhr.responseText);
        switch (response['result']) {
            case 'failure_email_not_available':
                $registerForm['email'].setValid(false).nextElementSibling.innerText = '이미 사용 중인 이메일 입니다.';
                dialog.showSimpleOk("회원가입", `입력하신 이메일 "${$registerForm['email'].value}"은/는 이미 사용 중입니다.`, () => $registerForm['email'].scrollIntoView({
                    behavior: "smooth",
                    block: "center"
                }));
                break;
            case 'failure_nickname_not_available':
                $registerForm['nickname'].setValid(false).nextElementSibling.innerText = '이미 사용 중인 닉네임 입니다.';
                dialog.showSimpleOk("회원가입", `입력하신 닉네임 "${$registerForm['nickname'].value}"은/는 이미 사용 중입니다.`, () => $registerForm['nickname'].scrollIntoView({
                    behavior: "smooth",
                    block: "center"
                }));
                break;
            case 'success':
                dialog.showSimpleOk('회원가입', '회원가입해 주셔서 감사합니다.\n확인 버튼을 클릭하면 로그인 페이지로 이동합니다.', () => location.href = '/user/login');
                break;
            default:
                dialog.showSimpleOk('회원가입', '알 수 없는 이유로 회원가입에 실패하였습니다.\n잠시 후 다시 시도해 주세요.');

        }
    };
    xhr.open('POST', '/user/register');
    xhr.send(formData);
};

$registerForm['email'].addEventListener('focusout', () => {
    $registerForm['email'].style.filter = ''; // color reset (빨간색으로)
    $registerForm['email'].nextElementSibling.style.filter = ''; // color reset (빨간색으로)
    $registerForm['email'].setValid(true);

    if ($registerForm['email'].value === '') {
        $registerForm['email']
            .setValid(false)
            .nextElementSibling.innerText = '이메일을 입력해주세요.';
        return;
    }
    if (!emailRegex.test($registerForm['email'].value)) {
        $registerForm['email']
            .setValid(false)
            .nextElementSibling.innerText = '올바르지 않은 이메일 형식입니다.';
        return;
    }

    const xhr = new XMLHttpRequest();

    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            return;
        }
        const response = JSON.parse(xhr.responseText);
        if (response['result'] === true) {
            $registerForm['email']
                .setValid(false)
                .nextElementSibling.innerText = '사용 가능한 이메일입니다.';
            $registerForm['email'].style.filter = 'hue-rotate(135deg)';
            $registerForm['email'].nextElementSibling.style.filter = 'hue-rotate(135deg)';
        } else {
            $registerForm['email']
                .setValid(false)
                .nextElementSibling.innerText = '이미 사용 중인 이메일입니다.';
        }
    };
    xhr.open('GET', `/user/email-check?email=${$registerForm['email'].value}`);
    xhr.send();
});

$registerForm['nickname'].addEventListener('focusout', () => {
    $registerForm['nickname'].style.filter = '';
    $registerForm['nickname'].nextElementSibling.style.filter = '';
    $registerForm['nickname'].setValid(true);
    if ($registerForm['nickname'].value === '') {
        $registerForm['nickname']
            .setValid(false)
            .nextElementSibling.innerText = '닉네임을 입력해주세요.';
        return;
    }
    if (!nicknameRegex.test($registerForm['nickname'].value)) {
        $registerForm['nickname']
            .setValid(false)
            .nextElementSibling.innerText = '올바르지 않은 닉네임 형식입니다.';
        return;
    }
    const xhr = new XMLHttpRequest();

    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {

            return;
        }
        const response = JSON.parse(xhr.responseText);
        if (response['result'] === true) {
            $registerForm['nickname']
                .setValid(false)
                .nextElementSibling.innerText = '사용 가능한 닉네임입니다.';
            $registerForm['nickname']
                .style.filter = 'hue-rotate(135deg)';
            $registerForm['nickname']
                .nextElementSibling.style.filter = 'hue-rotate(135deg)';
        } else {
            $registerForm['nickname']
                .setValid(false)
                .nextElementSibling.innerText = '이미 사용 중인 닉네임입니다.';
        }
    };
    xhr.open('GET', `/user/nickname-check?nickname=${$registerForm['nickname'].value}`);
    xhr.send();
});
