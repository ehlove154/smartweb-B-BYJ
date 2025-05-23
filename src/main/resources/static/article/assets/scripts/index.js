const $container = document.getElementById("container");
const $commentForm = document.getElementById("commentForm");
const $commentContainer = document.getElementById("commentContainer");

const deleteArticle = () => {
    const url = new URL(location.href);
    const id = url.searchParams.get("id");

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append("id", id);

    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            dialog.showSimpleOk('게시글 삭제', '요청을 처리하는 도중 오류가 발생하였습니다.\n잠시 후 다시 시도해 주세요.');
            return;
        }
        const response = JSON.parse(xhr.responseText);
        switch (response.result) {
            case 'failure_absent':
                dialog.showSimpleOk('게시글 삭제', '게시글이 존재하지 않습니다.', {
                    onOkCallback: () => $container.querySelector('.to-list').click()
                });
                break;
            case 'failure_session_expired':
                dialog.showSimpleOk('게시글 삭제', '세션이 만료 되었거나 게시글을 삭제할 권한이 없습니다.\n관리자에게 문의해 주세요.');
                break;
            case 'success':
                dialog.showSimpleOk('게시글 삭제', '게시글을 성공적으로 삭제하였습니다.', {
                    onOkCallback: () => $container.querySelector('.to-list').click()
                });
                break;
            default:
                dialog.showSimpleOk('게시글 삭제', '알 수 없는 이유로 게시글을 삭제하지 못하였습니다.\n잠시후 다시 시도해 주세요.');
        }

    };
    xhr.open('DELETE', '/article/');
    xhr.send(formData);
};

const loadComments = () => {
    const $title = $commentForm.querySelector(':scope > .title');
    $title.innerHTML = '댓글 (불러오는 중)';
    $commentContainer.innerHTML = '';
    const xhr = new XMLHttpRequest();

    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            $commentContainer.innerText = '댓글을 불러오지 못하였습니다.\n잠시 후 다시 시도해 주세요.';
            return;
        }
        const comments = JSON.parse(xhr.responseText);
        let html = '';
        if (comments.length === 0) {
            html = '등록 된 댓글이 없습니다.';
        }
        for (const comment of comments) {
            html += `
            <div class="comment">
            <div class="head">
                <span class="writer">${comment['userEmail']}</span>
                <span class="timestamp">${comment['createdAt'].split('T').join(' ')}</span>
                <span class="-flex-stretch"></span>
                <a class="action">수정</a>
                <a class="action">삭제</a>
            </div>
            <label class="body">
                <input hidden name="replyCheck" type="checkbox">
                <span class="content">${comment['content']}</span>
            </label>
            <form class="reply-form">
                <input hidden name="articleId" type="hidden" value="${comment['articleId']}">
                <label class="--object-label -flex-stretch">
                    <textarea required class="--object-field ---field -flex-stretch" minlength="1" maxlength="1000" name="content" placeholder="댓글을 입력해 주세요." rows="4"></textarea>
                </label>
                <button class="--object-button -color-green" type="submit">댓글 등록하기</button>
            </form>
        </div>`;
        }
        $commentContainer.innerHTML = html;
    };
    xhr.open('GET', `${origin}/article/comment?id=` + new URL(location.href).searchParams.get('id'));
    xhr.send();
}

$container.querySelector(`[name="delete"]`)?.addEventListener("click", () => {
    dialog.show({
        title: "게시글 삭제",
        content: "게시글을 삭제하시겠습니까?\n삭제된 게시글은 복구가 어렵습니다.",
        buttons: [
            {caption: '취소', onclick: ($modal) => dialog.hide($modal)},
            {
                caption: '삭제',
                color: 'red',
                onclick: ($modal) => {
                    dialog.hide($modal);
                    deleteArticle();
                }
            }
        ]
    });
});

$commentForm.addEventListener("submit", (e) => {
    e.preventDefault();

    if ($commentForm['content'].value === '') {
        dialog.showSimpleOk('댓글 작성', '내용을 작성해 주세요.');
        return;
    }
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append("articleId", new URL(location.href).searchParams.get("id"));
    formData.append("content", $commentForm['content'].value);
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            dialog.showSimpleOk('댓글 작성', '요청을 처리하는 도중 오류가 발생하였습니다.\n잠시 후 다시 시도해 주세요.');
            return;
        }
        const response = JSON.parse(xhr.responseText);
        switch (response.result) {
            case 'failure_session_expired':
                dialog.showSimpleOk('댓글 작성', '세션이 만료되었거나 댓글을 작성할 권한이 없습니다.\n관리자에게 문의해 주세요.');
                break;
            case 'success':
                $commentForm['content'].value = '';
                $commentForm['content'].focus();
                loadComments();
                break;
            default:
                dialog.showSimpleOk('댓글 작성', '알 수 없는 이유로 댓글을 작성하지 못하였습니다.\n잠시 후 다시 시도해 주세요.');
        }
    };
    xhr.open('POST', '/article/comment');
    xhr.send(formData);
});

loadComments();