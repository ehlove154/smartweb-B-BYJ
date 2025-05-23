const $writeForm = document.getElementById('writeForm');
const titleReg = new RegExp('^(.{1,100})$');
const contentReg = new RegExp('^(.{1,100000})$');

$writeForm.onsubmit = (e) => {
    e.preventDefault();
    if ($writeForm['title'].value === '') {
        dialog.showSimpleOk('게시글 작성', '제목을 입력해 주세요.', {
            onOkCallback: () => $writeForm['title'].focus()
        });
        return;
    }
    if (!titleReg.test($writeForm['title'].value)) {
        dialog.showSimpleOk('게시글 작성', '유효하지 않은 제목 형식입니다.', {
            onOkCallback: () => $writeForm['title'].focus()
        });
        return;
    }
    if (editor.getData() === '') {
        dialog.showSimpleOk('게시글 작성', '내용을 입력해 주세요.', {
            onOkCallback: () => $writeForm['content'].focus()
        });
        return;
    }
    if (!contentReg.test(editor.getData())) {
        dialog.showSimpleOk('게시글 작성', '유효하지 않은 내용 형식입니다.', {
            onOkCallback: () => $writeForm['content'].focus()
        });
        return;
    }
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('boardId', $writeForm['boardId'].value);
    formData.append('title', $writeForm['title'].value);
    formData.append('content', editor.getData());
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            dialog.showSimpleOk('게시글 작성', '요청을 처리하는 도중 오류가 발생하였습니다.\n잠시 후 다시 시도해 주세요.');
            return;
        }
        const response = JSON.parse(xhr.responseText);
        switch (response.status) {
            case 'failure_session_expired':
                dialog.showSimpleOk('게시글 작성', '세션이 만료되었거나 게시글을 작성 할 권한이 없습니다.\n관리자에게 문의해 주세요.');
                break;
            case 'success':
                location.href = `/article/?id=${response.id}`;
                break;
            default:
                dialog.showSimpleOk('게시글 작성', '알 수 없는 이유로 게시글을 작성하지 못하였습니다.\n잠시 후 다시 시도해 주세요.');
        }
    };
    xhr.open('POST', '/article/');
    xhr.send(formData);

};