const $wrapper = document.getElementById('wrapper');
const $deleteButton = $wrapper.querySelector(`:scope > .button-container > button[name='delete']`);

if ($deleteButton != null) {
    $deleteButton.addEventListener(`click`, () => {
        dialog.show({
            title: `게시글 삭제`,
            content: `정말로 해당 게시글을 삭제할까요?\n삭제한 게시글은 되돌릴 수 없습니다.`,
            buttons: [
                {
                    caption: `취소`, color: `gray`, onclick: ($dialog) => {
                        dialog.hide($dialog);
                    }
                },
                {
                    caption: `삭제`, color: `red`, onclick: ($dialog) => {
                        const xhr = new XMLHttpRequest();
                        const formData = new FormData();
                        formData.append(`index`, new URL(location.href).searchParams.get(`index`));
                        xhr.onreadystatechange = () => {
                            if (xhr.readyState !== XMLHttpRequest.DONE) {
                                return;
                            }
                            dialog.hide($dialog);
                            if (xhr.status < 200 || xhr.status >= 300) {
                                dialog.showSimpleOk('게시글 삭제', `[${xhr.status}]\n요청을 처리하는 도중 오류가 발생 하였습니다.\n잠시 후 다시 시도해 주세요.`)
                                return;
                            }
                            const response = JSON.parse(xhr.responseText);
                            switch (response[`result`]) {
                                case `failure_absent`:
                                    dialog.showSimpleOk('게시글 삭제', `게시글을 삭제하지 못하였습니다.\n삭제하려는 게시글이 존재하지 않습니다.\n이미 삭제되었을 수도 있습니다.`);
                                    break;
                                case `failure_session_expired`:
                                    dialog.showSimpleOk('게시글 삭제', `게시글을 삭제하지 못하였습니다.\n권한이 없거나 세션이 만료되었을 수도 있습니다.`);
                                    break;
                                case  `success`:
                                    dialog.showSimpleOk('게시글 삭제', `게시글을 성공적으로 삭제하였습니다.`, () => {
                                        $wrapper.querySelector(`:scope > .button-container > a.to-list`).click();
                                    });
                                    break;
                                default:
                                    dialog.showSimpleOk('게시글 삭제', `알 수 없는 이유로 게시글을 삭제하지 못하였습니다.\n잠시 후 다시 시도해 주세요`);
                            }
                        };
                        xhr.open('DELETE', '/article/');
                        xhr.send(formData);
                    }
                }
            ]
        });
    });
}
