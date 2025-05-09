/* static은 /에 맵핑
* > static이 http://localhostL8080/ 부터 출발
*
* study/assets/scripts/calc-xhr.js
* > http://localhostL8080/study/assets/scripts/calc-xhr.js
*
* http://localhost:8080/study/calc-xhr
* http://localhostL8080/study/assets/scripts/calc-xhr.js
* > 상대경로 : ./assets/scripts/calc-xhr.js
* > 절대경로 : /study/assets/scripts/calc-xhr.js */

const $form = document.body.querySelector(`form`);
const $intro = document.getElementById('intro');
const $result = document.getElementById('result');
const $error = document.getElementById('error');

$form.onsubmit = (e) => {
    e.preventDefault(); // 폼 기본 제출 방지

    const xhr = new XMLHttpRequest(); // 비동기 요청 객체 생성
    const formData = new FormData(); // 폼 입력값 담기
    formData.append('a', $form['a'].value);
    formData.append('b', $form['b'].value);
    formData.append('op', $form['op'].value);

    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            alert(`[${xhr.status}] 오류`);
            return;
        }
        $intro.style.display = 'none';
        $result.style.display = 'none';
        $error.style.display = 'none';
        const response = JSON.parse(xhr.responseText);
        if (response['error'] == null) {
            $result.innerText = `${$form['a'].value} ${response['symbol']} ${$form['b'].value} = ${response['value']}`;
            $result.style.display = 'block';
        } else {
            $error.innerText = response['error'];
            $error.style.display = 'block';
        }

        // switch (response['result']) {
        //     case 'FAILURE_DIVIDE_BY_ZERO':
        //         $error.innerText = `0으로 나눌 수 없습니다.`;
        //         $error.style.display = 'block';
        //         break;
        //     case 'FAILURE_INVALID_NUMBER':
        //         $error.innerText = `올바르지 않은 숫자입니다.`;
        //         $error.style.display = 'block';
        //         break;
        //     case 'FAILURE_INVALID_OPERATOR':
        //         $error.innerText = `올바르지 않은 연산자입니다.`;
        //         $error.style.display = 'block';
        //         break;
        //     case 'SUCCESS':
        //         const symbol = {
        //             plus: '+',
        //             minus: '-',
        //             multiply: '*',
        //             divide: '/',
        //             modulo: '%',
        //             power: '**'
        //         }[$form['op'].value];
        //         $result.innerText = `${$form['a'].value} ${symbol} ${$form['b'].value} = ${response['value']}`;
        //         $result.style.display = 'block';
        //         break;
        // }
    };
    xhr.open('POST', location.href);
    xhr.send(formData);
};