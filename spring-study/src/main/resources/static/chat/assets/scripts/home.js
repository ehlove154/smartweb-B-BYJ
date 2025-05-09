const $history = document.getElementById('history');
const $refreshButton = $history.querySelector(':scope > button[name="refresh"]');
const $list = $history.querySelector(':scope > .list');
const $messageForm = document.getElementById('messageForm');

$refreshButton.addEventListener('click', () => {
    const xhr = new XMLHttpRequest();
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            alert('대화 내역을 불러오지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            return;
        }
        const existingLength = $list.querySelectorAll(':scope > .item').length; // 현재 존재하는 대화 내역의 길이
        const messages = JSON.parse(xhr.responseText); // 서버에서 받아온 대화 내역 배열
        // i에 대해 (현재 존재하는 대화 내역의 길이 - 1)부터 (전체 대화 내역의 길이 - 1)까지 반복하면 기존의 대화 내역을 삭제하지 않고 새로운 내역만 append하면 되기 때문에 화면 깜빡임을 제거할 수 있음
        // Math.max 처리를 하는 이유는 기존의 개수가 0개일 경우 -1이 되기 때문.
        for (let i = Math.max(existingLength - 1, 0); i < messages.length; i++) {
            const message = messages[i];
            const $item = document.createElement('li');
            $item.classList.add('item');
            $item.innerText = message;
            $list.append($item);
        }
    };
    xhr.open('GET', `${window.origin}/chat/message`);
    xhr.send();
});

$messageForm.onsubmit = (e) => {
    e.preventDefault();
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('message', $messageForm['message'].value);
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            alert('메세지 전송에 실패했습니다. 잠시 후 다시 시도해 주세요.');
            return;
        }
        $refreshButton.dispatchEvent(new Event('click'));
        $messageForm['message'].value = '';
        $messageForm['message'].focus();
    };
    xhr.open('POST', `${window.origin}/chat/message`);
    xhr.send(formData);
};

// 1초(1000밀리초)마다 $refreshButton을 click하는 로직
setInterval(() => {
    $refreshButton.dispatchEvent(new Event('click'));
}, 1000);

// 페이지 들어오자마자 대화내역을 불러오기 위한 처리
$refreshButton.dispatchEvent(new Event('click'));