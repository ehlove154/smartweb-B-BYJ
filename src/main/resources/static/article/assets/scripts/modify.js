const $modifyForm = document.getElementById('modifyForm');

$modifyForm.onsubmit = (e) => {
    e.preventDefault();
    if ($modifyForm['title'].value.length === 0) {
        dialog.showSimpleOk('게시글 수정', '제목을 입력해 주세요.', () => $modifyForm['title'].focus());
        return;
    }
    if ($modifyForm['content'].value.length === 0) {
        dialog.showSimpleOk('게시글 수정', '내용을 입력해 주세요.', () => $modifyForm['content'].focus());
        return;
    }
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('index', $modifyForm['index'].value);
    formData.append('title', $modifyForm['title'].value);
    formData.append('content', $modifyForm['content'].value);
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            dialog.showSimpleOk('게시글 수정', `[${xhr.status}]\n요청을 처리하는 도중 오류가 발생 하였습니다.\n잠시 후 다시 시도해 주세요.`)
            return;
        }
        const response = JSON.parse(xhr.responseText);
        switch (response['result']) {
            case 'failure_absent':
                dialog.showSimpleOk('게시글 수정', '게시글을 수정에 살패하였습니다.\n게시글이 존재하지 않습니다.\n게시글이 삭제되었을 수도 있습니다.');
                break;
            case 'failure_session_expired':
                dialog.showSimpleOk('게시글 수정', '게시글을 수정에 살패하였습니다.\n권한이 없거나 세션이 만료되었습니다.\n로그인 후 다시 시도해 주세요.', () => location.href = '/user/login');
                break;
            case 'success':
                location.href = `/article/?index=${$modifyForm['index'].value}`;
                break;
            default:
                dialog.showSimpleOk('게시글 수정', '알 수 없는 이유로 게시글을 수정하지 못하였습니다.\n잠시 후 다시 시도해 주세요.');
        }

    };
    xhr.open('PATCH', '/article/modify');
    xhr.send(formData);
};