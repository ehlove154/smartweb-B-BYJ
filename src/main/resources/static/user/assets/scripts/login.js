const $loginForm = document.getElementById('loginForm');
const emailRegex = new RegExp('^(?=.{8,50}$)([\\da-z\\-_.]{4,})@([\\da-z][\\da-z\\-]*[\\da-z]\\.)?([\\da-z][\\da-z\\-]*[\\da-z])\\.([a-z]{2,15})(\\.[a-z]{2,3})?$');
const passwordRegex = new RegExp('^([\\da-zA-Z`~!@#$%^&*()\\-_=+\\[{\\]}\\\\|;:\'",<.>/?]{8,50})$');

$loginForm.onsubmit =(e) => {
    e.preventDefault();
    const $emailLabel = $loginForm.querySelector('.--object-label:has(input[name="email"])');
    const $passwordLabel = $loginForm.querySelector('.--object-label:has(input[name="password"])');
    $emailLabel.setValid(true);
    $passwordLabel.setValid(true);
    if ($loginForm['email'].value === '') {
        $emailLabel.setValid(false, '이메일을 입력해 주세요.');
        $loginForm['email'].focus();
        return;
    }
    if (!emailRegex.test($loginForm['email'].value)) {
        $emailLabel.setValid(false, '올바른 이메일을 입력해 주세요.');
        $loginForm['email'].focus();
        $loginForm['email'].select();
        return;
    }
    if ($loginForm['password'].value === '') {
        $passwordLabel.setValid(false, '비밀번호를 입력해 주세요.');
        $loginForm['password'].focus();
        return;
    }
    if (!passwordRegex.test($loginForm['password'].value)) {
        $passwordLabel.setValid(false, '올바른 비밀번호를 입력해 주세요.');
        $loginForm['password'].focus();
        $loginForm['password'].select();
        return;
    }
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('email', $loginForm['email'].value);
    formData.append('password', $loginForm['password'].value);
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            dialog.showSimpleOk('로그인', '요청을 처리하는 도중 오류가 발생하였습니다.\n잠시 후 다시 시도해 주세요.');
            return;
        }
        const response = JSON.parse(xhr.responseText);
        switch (response.result) {
            case 'failure_suspended':
                dialog.showSimpleOk('로그인', '해당 계정은 이용이 정지된 상태입니다.\n관리자에게 문의래 주세요.');
                break;
            case 'success':
                if ($loginForm['remember'].checked) {
                    localStorage.setItem('loginEmail', $loginForm['email'].value);
                } else {
                    localStorage.removeItem('loginEmail')
;                }
                location.href = '/';
                break;
            default:
                dialog.showSimpleOk('로그인', '이메일 혹은 비밀번호가 올바르지 않습니다.\n다시 확인해 주세요.');
        }
    };
    xhr.open('POST', '/user/login');
    xhr.send(formData);
};

$loginForm['email'].value = localStorage.getItem('loginEmail') ?? '';