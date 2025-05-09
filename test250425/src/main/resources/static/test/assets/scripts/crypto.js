const $utilForm = document.getElementById('utilForm');
const $option = document.getElementById('option');
const $plaintext = document.getElementById('plaintext');
const $check = document.getElementById('check');
const $result = document.getElementById('result');

const xhr = new XMLHttpRequest();
const formData = new FormData();
$utilForm.onsubmit = (e) => {
    e.preventDefault();

    formData.append('op', $option.value);
    formData.append('plaintext', $plaintext.value);
    formData.append('check', $check.checked ? 'true' : 'false');

    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        $result.innerText = xhr.responseText;
        if (xhr.status < 200 || xhr.status >= 300) {
            // $result.innerText = xhr.responseText;
            alert(`[${xhr.status}] 오류가 발생했습니다.`);
        }
        // else {
        //         alert(`[${xhr.status}] 오류가 발생했습니다.`);
        // }

    };
    xhr.open('POST', '/util/crypto');
    xhr.send(formData);s
}