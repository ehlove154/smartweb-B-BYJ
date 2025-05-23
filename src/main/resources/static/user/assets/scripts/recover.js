const $loading = document.getElementById('loading');
const $recoverForm = document.getElementById("recoverForm");
const emailRegex = new RegExp('^(?=.{8,50}$)([\\da-z\\-_.]{4,})@([\\da-z][\\da-z\\-]*[\\da-z]\\.)?([\\da-z][\\da-z\\-]*[\\da-z])\\.([a-z]{2,15})(\\.[a-z]{2,3})?$');
const emailCodeRegex = new RegExp('^(\\d{6})$');
const passwordRegex = new RegExp('^([\\da-zA-Z`~!@#$%^&*()\\-_=+\\[{\\]}\\\\|;:\'",<.>/?]{8,50})$');
const nicknameRegex = new RegExp('^([\\da-zA-Z가-힣]{2,10})$');
const nameRegex = new RegExp('^([가-힣]{2,5})$');
const contactSecondRegex = new RegExp('^(\\d{3,4})$');
const contactThirdRegex = new RegExp('^(\\d{4})$');

$recoverForm['pEmailCodeSendButton'].addEventListener('click', () => {
    const $emailLabel = $recoverForm.querySelector('.--object-label:has(input[name="pEmail"])');
    if ($recoverForm['pEmail'] === '') {
        $emailLabel.setValid(false, '이메일을 입력해 주세요.');
        $recoverForm['pEmail'].focus();
        return;
    }
    if (!emailRegex.test($recoverForm['pEmail'].value)) {
        $emailLabel.setValid(false, '유효하지 않은 이메일입니다.');
        $recoverForm['pEmail'].focus();
        $recoverForm['pEmail'].select();
        return;
    }
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('email', $recoverForm['pEmail'].value);
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
            case 'failure_absent':
                dialog.showSimpleOk('인증번호 전송', `입력하신 이메일 '${$recoverForm['pEmail'].value}' 을/를 사용 중인 계정을 찾지 못하였습니다.`);
                break;
            case 'success':
                $recoverForm['pEmailSalt'].value = response.salt;
                $recoverForm['pEmail'].setDisabled(true);
                $recoverForm['pEmailCodeSendButton'].setDisabled(true);
                $recoverForm['pEmailCode'].setDisabled(false);
                $recoverForm['pEmailCodeVerifyButton'].setDisabled(false);
                $recoverForm['pEmailCode'].focus();
                dialog.showSimpleOk('인증번호 전송', '입력하신 이메일로 인증번호를 전송하였습니다.\n해당 인증 번호는 10분간만 유효하니 유의해 주세요.');
                break;
            default:
                dialog.showSimpleOk('인증번호 전송', '알 수 없는 이유로 인증번호 전송에 실패하였습니다.\n잠시 후 다시 시도해 주세요.');
        }
    };
    xhr.open('POST', '/user/recover-password-email');
    xhr.send(formData);
    $loading.show();
});

$recoverForm['pEmailCodeVerifyButton'].addEventListener('click', () => {
    const $emailLabel = $recoverForm.querySelector('.--object-label:has(input[name="pEmail"])');

    if ($recoverForm['pEmailCode'] === '') {
        $emailLabel.setValid(false, '인증번호를 입력해 주세요.');
        $recoverForm['pEmailCode'].focus();
        return;
    }
    if (!emailCodeRegex.test($recoverForm['pEmailCode'].value)) {
        $emailLabel.setValid(false, '올바른 인증번호를 입력해 주세요.');
        $recoverForm['pEmailCode'].focus();
        $recoverForm['pEmailCode'].select();
        return;
    }
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('email', $recoverForm['pEmail'].value);
    formData.append('code', $recoverForm['pEmailCode'].value);
    formData.append('salt', $recoverForm['pEmailSalt'].value);
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
                $recoverForm['pEmailSalt'].value = '';
                $recoverForm['pEmail'].setDisabled(false);
                $recoverForm['pEmailCodeSendButton'].setDisabled(false);
                $recoverForm['pEmailCode'].value = '';
                $recoverForm['pEmailCode'].setDisabled(true);
                $recoverForm['pEmailCodeVerifyButton'].setDisabled(true);
                $recoverForm['pEmail'].focus();
                dialog.showSimpleOk('인증번호 확인', '인증번호가 만료되었습니다.\n이메일 인증을 다시 진행해 주세요.');
                break;
            case 'success':
                $recoverForm['pEmailCode'].setDisabled(true);
                $recoverForm['pEmailCodeVerifyButton'].setDisabled(true);
                $recoverForm['pPassword'].setDisabled(false).focus();
                $recoverForm['pPasswordCheck'].setDisabled(false);
                dialog.showSimpleOk('인증번호 확인', '이메일이 정상적으로 인증되었습니다.');
                break;
            default:
                dialog.showSimpleOk('인증번호 확인', '인증번호가 올바르지 않습니다.\n다시 확인해 주세요.', () => {
                    $recoverForm['pEmailCode'].focus();
                    $recoverForm['pEmailCode'].select();
                });
        }
    };
    xhr.open('PATCH', '/user/recover-password-email',);
    xhr.send(formData);
});

$recoverForm.onsubmit = (e) => {
    e.preventDefault();

    if ($recoverForm['type'].value === 'email') {
        alert("email");
    } else if ($recoverForm['type'].value === 'password') {
        const $emailLabel = $recoverForm.querySelector('.--object-label:has(input[name="pEmail"])');
        const $passwordLabel = $recoverForm.querySelector('.--object-label:has(input[name="pPassword"])');
        const $labels = [$emailLabel, $passwordLabel];

        $labels.forEach(($label) => $label.setValid(true));
        if (!$recoverForm['pEmailCodeSendButton'].hasAttribute('disabled') ||
            !$recoverForm['pEmailCodeVerifyButton'].hasAttribute('disabled')) {
            $emailLabel.setValid(false, '이메일 인증을 완료해 주세요.');
            return;
        }
        if ($recoverForm['pPassword'].value === '') {
            $passwordLabel.setValid(false, '새로운 비밀번호를 입력해 주세요.');
            $recoverForm['pPassword'].focus();
            return;
        }
        if (!passwordRegex.test($recoverForm['pPassword'].value)) {
            $passwordLabel.setValid(false, '유효하지 않은 이메일 형식입니다.');
            $recoverForm['pPassword'].focus();
            $recoverForm['pPassword'].select();
            return;
        }
        if ($recoverForm['pPasswordCheck'].value === '') {
            $passwordLabel.setValid(false, '비밀번호를 한 번 더 입력해 주세요.');
            $recoverForm['pPasswordCheck'].focus();
            return;
        }
        if ($recoverForm['pPasswordCheck'].value !== $recoverForm['pPasswordCheck'].value) {
            $passwordLabel.setValid(false, '비밀번호가 일치하지 않습니다.');
            $recoverForm['pPasswordCheck'].focus();
            $recoverForm['pPasswordCheck'].select();
            return;
        }
        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        formData.append('email', $recoverForm['pEmail'].value);
        formData.append('code', $recoverForm['pEmailCode'].value);
        formData.append('salt', $recoverForm['pEmailSalt'].value);
        formData.append('password', $recoverForm['pPassword'].value);
        xhr.onreadystatechange = () => {
            if (xhr.readyState !== XMLHttpRequest.DONE) {
                return;
            }
            if (xhr.status < 200 || xhr.status >= 300) {
                dialog.showSimpleOk('비밀번호 재설정', '요청을 처리하는 도중 오류가 발생하였습니다.\n잠시 후 다시 시도해 주세요.');
                return;
            }
            const response = JSON.parse(xhr.responseText);
            switch (response.result) {
                case 'success':
                    dialog.showSimpleOk('비밀번호 재설정', '비밀번호를 재설정하였습니다.\n확인 버튼을 클릭하면 로그인 페이지로 이동합니다', {
                        onOkCallback: () => location.href = '/user/login'
                    });
                    break;
                    default:
                        dialog.showSimpleOk('비밀번호 재설정', '알 수 없는 이유로 비밀번호를 재설정 하지 못했습니다.\n잠시 후 다시 시도해 주세요.');
            }
        };
        xhr.open('POST', '/user/recover-password',);
        xhr.send(formData);
    }
}