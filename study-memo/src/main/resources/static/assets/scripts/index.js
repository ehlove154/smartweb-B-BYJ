const $memoForm = document.getElementById('memoForm');
const $memoTable = document.getElementById('memoTable');

const loadMemos = () => {
    const $tbody = $memoTable.querySelector(':scope > tbody');
    $tbody.innerHTML = '';
    const xhr = new XMLHttpRequest();
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
           alert(`[${xhr.status}]\n메모를 불러오지 못했습니다.\n잠시 후 다시 시도해 주세요/`)
            return;
        }
        /* [{"index":2,"writer":"가나달아","content":"운동하러 가고 싶다","createdAt":"2025-04-21T14:28:32"},
        {"index":1,"writer":"sff","content":"fasasfag","createdAt":"2025-04-21T12:44:14"}] */
        const memos = JSON.parse(xhr.responseText);
        let tbodyHtml = '';
        for (const memo of memos) {
            const createdArray = memo['createdAt'].split('T');
            tbodyHtml += `
            <tr>
                <th scope="row">${memo['index']}</th>
                <td>${memo['writer']}</td>
                <td>${memo['content']}</td>
                <td>${createdArray[0]}</td>
                <td>${createdArray[1]}</td>
                <td>
                     <button name="delete" type="button" data-index="${memo['index']}">삭제</button>
                
                </td>
             </tr>`;
        }
        $tbody.innerHTML = tbodyHtml;
        $tbody.querySelectorAll('button[name="delete"]').forEach(($button) => {
            $button.addEventListener('click', () => {
                if (confirm('정말로 해당 메모를 삭제할까요?') === true) {
                    deleteMemo($button.dataset['index']);
                }
            })
        });
    };
    xhr.open('GET', '/memos');
    xhr.send();
}
const deleteMemo = (index) => {
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('index', index);
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
        alert(`[${xhr.status}]\n메모를 삭제하지 못했습니다.\n잠시 후 다시 시도해 주세요.`)
            return;
        }
        const response = JSON.parse(xhr.responseText);
        switch (response['result']) {
            case 'success':
                alert('메모를 성공적으로 삭제하였습니다.');
                loadMemos();
                break;
            default:
                alert('알 수 없는 이유로 메모를 삭제하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
        }
    };
    xhr.open('DELETE', '/');
    xhr.send(formData);
}


$memoForm.onsubmit = (e) => {
    e.preventDefault(); // form 태그 본연의 이벤트 방지
    const writer = $memoForm['writer'].value;
    const content = $memoForm['content'].value;

    if (writer.length === 0) {
        alert("작성자를 입력해 주세요.");
        $memoForm['writer'].focus();
        return;
    }
    if (writer.length < 2 || writer.length > 10) {
        alert("작성자의 길이는 2자 이상, 10자 이하여야 합니다.");
        $memoForm['writer'].focus();
        return;
    }
    if (content.length === 0) {
        alert("내용을 입력해 주세요.");
        $memoForm['content'].focus();
        return;
    }
    if (content.length > 100) {
        alert("내용의 길이는 100자 이하여야 합니다.");
        $memoForm['content'].focus();
        return;
    }

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('writer', writer);
    formData.append('content', content);
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            alert(`[${xhr.status}]\n메모를 작성하지 못하였습니다.\n잠시 후 다시 시도해 주세요.`)
            return;
        }
        const response = JSON.parse(xhr.responseText);
        switch (response['result']) {
            case 'success':
                $memoForm['writer'].value = '';
                $memoForm['content'].focus();
                loadMemos();
                break;
            default:
                alert('알 수 없는 이유로 메모를 작성하지 못했습니다. 잠시 후 다시 시도해 주세요.')
        }
    };
    xhr.open('POST', '/');
    xhr.send(formData);
};

loadMemos();