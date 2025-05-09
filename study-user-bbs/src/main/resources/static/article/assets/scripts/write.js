const $writeForm = document.getElementById('writeForm');

$writeForm.onsubmit = (e) => {
    e.preventDefault();
    if ($writeForm['title'].value.length === 0) {
        dialog.showSimpleOk('게시글 작성', '제목을 입력해 주세요.', () => $writeForm['title'].focus());
        return;
    }
    if ($writeForm['content'].value.length === 0) {
        dialog.showSimpleOk('게시글 작성', '내용을 입력해 주세요.', () => $writeForm['content'].focus());
        return;
    }
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('boardId', new URL(location.href). searchParams.get('boardId'));
    formData.append('title', $writeForm['title'].value);
    formData.append('content', $writeForm['content'].value);
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            dialog.showSimpleOk('게시글 작성', `[${xhr.status}]\n요청을 처리하는 도중 오류가 발생 하였습니다.\n잠시 후 다시 시도해 주세요.`)
            return;
        }
        const response = JSON.parse(xhr.responseText);
        switch (response['result']) {
            case 'failure_session_expired':
                dialog.showSimpleOk('게시글 작성', '세션이 만료되었습니다.\n로그인 후 다시 시도해 주세요.', () => location.href = '/user/login');
                break;
            case 'success':
                location.href = `/article/?index=${response['index']}`;
                break;
            default:
                dialog.showSimpleOk('게시글 작성', '알 수 없는 이유로 게시글을 작성하지 못하였습니다.\n잠시 후 다시 시도해 주세요.');
                break;
        }

    };
    xhr.open('POST', '/article/write');
    xhr.send(formData);
};