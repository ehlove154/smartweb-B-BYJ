const $tickerForm = document.getElementById('tickerForm');
const $table = document.getElementById('table');
const idRegex = new RegExp("^([A-Z]{1,5})$");
const nameRegex = new RegExp("^([\\da-zA-Z\\-,.\\s]{1,100})$");


const loadTickers = () => {
    const $tbody = $table.querySelector(':scope > tbody');
    $tbody.innerHTML = '';
    const xhr = new XMLHttpRequest();

    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            alert(`[${xhr.status}]\n티커를 불러오지 못했습니다.\n잠시 후 다시 시도해 주세요.`)
            return;
        }
        const tickers = JSON.parse(xhr.responseText);
        let tbodyHtml = '';
        for (const ticker of tickers) {
            tbodyHtml += `
            <tr>
                <td class="td_id">${ticker['id']}</td>
                <td class="td_name">${ticker['name']}</td>
                <td>
                    <button name="delete" type="button" data-id="${ticker['id']}">삭제</button>
                </td>
            </tr>`;
        }
        $tbody.innerHTML = tbodyHtml;
        $tbody.querySelectorAll(`button[name="delete"]`).forEach(($button) => {
            $button.addEventListener('click', () => {
                if (confirm('정말로 해당 티커를 삭제할까요?') === true) {
                    deleteTicker($button.dataset['id']);
                }
            })
        })
    };
    xhr.open('GET', '/tickers');
    xhr.send();
}
const deleteTicker = (id) => {
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append(`id`, id);
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            alert(`[${xhr.status}]\n티커를 삭제하지 못했습니다.\n잠시 후 다시 시도해 주세요.`)
            return;
        }
        const response = JSON.parse(xhr.responseText);
        switch (response['result']) {
            case `failUre`:
                alert('알 수 없는 이유로 삭제에 실패하였습니다.\n잠시 후 다시 시도해 주세요.');
                break;
            case `success`:
                alert(`성공적으로 삭제하였습니다.`);
                loadTickers();
                break;
        }
    };
    xhr.open('DELETE', '/ticker');
    xhr.send(formData);
}

$tickerForm.onsubmit = (e) => {
    e.preventDefault();
    const id = $tickerForm['id'].value;
    const name = $tickerForm['name'].value;

    if (id.length === 0 || id.length === '' ){
        alert('ID를 입력해 주세요.');
        $tickerForm['id'].focus();
        return;
    }
    if (id.length < 1 || id.length > 5) {
        alert('ID의 길이는 1자 이상, 5자 이하여야 합니다.');
        $tickerForm['id'].focus();
        return;
    }
    if (!idRegex.test(id)) {
        alert('올바른 ID를 입력해 주세요.');
        $tickerForm['id'].focus();
        return;
    }
    if (name.length === 0 || name.length === ''){
        alert('이름을 입력해 주세요.');
        $tickerForm['name'].focus();
        return;
    }
    if (name.length < 1 || name.length > 100) {
        alert('이름의 길이는 1자 이상, 100자 이하여야 합니다.');
        $tickerForm['name'].focus();
        return;
    }
    if (!nameRegex.test(name)) {
        alert('올바른 이름을 입력해 주세요.');
        $tickerForm['name'].focus();
        return;
    }

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('id', id);
    formData.append('name', name);
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            alert(`[${xhr.status}]\n티커를 작성하지 못하였습니다.\n잠시 후 다시 시도해 주세요.`)
            return;
        }
        const response = JSON.parse(xhr.responseText);
        switch (response['result']) {
            case 'success':
                $tickerForm['id'].value = '';
                $tickerForm['name'].focus();
                loadTickers();
                break;
            case 'failure':
                alert('알 수 없는 이유로 티커를 작성하지 못했습니다.\n잠시 후 다시 시도해 주세요.');
                break;
            case 'failure_duplicate_id':
                alert('이미 등록된 ID입니다.\n다른 ID로 다시 시도해 주세요.')
                break;
        }
    };
    xhr.open('POST', '/ticker');
    xhr.send(formData);
}

loadTickers();