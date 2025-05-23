const $modifyForm = document.getElementById('modifyForm');
const titleReg = new RegExp('^(.{1,100})$');
const contentReg = new RegExp('^(.{1,100000})$');

$modifyForm.onsubmit = (e) => {
    e.preventDefault();
    if ($modifyForm['title'].value === '') {
        dialog.showSimpleOk('게시글 수정', '제목을 입력해 주세요.', {
            onOkCallback: () => $modifyForm['title'].focus()
        });
        return;
    }
    if (!titleReg.test($modifyForm['title'].value)) {
        dialog.showSimpleOk('게시글 수정', '유효하지 않은 제목 형식입니다.', {
            onOkCallback: () => $modifyForm['title'].focus()
        });
        return;
    }
    if (editor.getData() === '') {
        dialog.showSimpleOk('게시글 수정', '내용을 입력해 주세요.', {
            onOkCallback: () => $modifyForm['content'].focus()
        });
        return;
    }
    if (!contentReg.test(editor.getData())) {
        dialog.showSimpleOk('게시글 수정', '유효하지 않은 내용 형식입니다.', {
            onOkCallback: () => $modifyForm['content'].focus()
        });
        return;
    }
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('id', $modifyForm['id'].value);
    formData.append('title', $modifyForm['title'].value);
    formData.append('content', editor.getData());
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            dialog.showSimpleOk('게시글 수정', '요청을 처리하는 도중 오류가 발생하였습니다.\n잠시 후 다시 시도해 주세요.');
            return;
        }
        const response = JSON.parse(xhr.responseText);
        switch (response.result) {
            case 'failure_absent':
                dialog.showSimpleOk('게시글 수정', '존재하지 않는 게시글입니다.', {
                    onOkCallback: () => {
                        if (history.length > 1) {
                            history.back();
                        } else {
                            close();
                        }
                    }
                });
                break;
            case 'failure_session_expired':
                dialog.showSimpleOk('게시글 수정', '세션이 만료되었거나 게시글을 수정 할 권한이 없습니다.\n관리자에게 문의해 주세요.');
                break;
            case 'success':
                location.href = `/article/?id=${$modifyForm['id'].value}`;
                break;
            default:
                dialog.showSimpleOk('게시글 수정', '알 수 없는 이유로 게시글을 수정하지 못하였습니다.\n잠시 후 다시 시도해 주세요.');
        }
    };
    xhr.open('PATCH', '/article/');
    xhr.send(formData);

};