class Dialog {
    #$element;
    #$modals = []; /* # = private */

    constructor() {
        this.#$element = document.body.querySelector('.-object-dialog');
        if (this.#$element === null) {
            this.#$element = document.createElement('div');
            this.#$element.classList.add('-object-dialog');
            document.body.insertAdjacentElement('afterbegin', this.#$element);
        }
    }

    /**
     *
     * @param {HTMLElement} $modal
     */
    hide($modal) {
        const index = this.#$modals.indexOf($modal);
        if (index === -1) {
            return false;
        }
        $modal.hide();
        this.#$modals.splice(index, 1);
        if (this.#$modals.length === 0) {
            this.#$element.hide();
        }
        setTimeout(() => $modal.remove(), 1000); // 1000 === 1 second
        return true;
    }

    /**
     *
     * @param {{title: string, content: string, buttons?: {caption: string, color?: 'gray'|'green'|'blue'|'red', onclick: function(HTMLElement?)}[], isContentHtml?: boolean|undefined}} args
     * @return {HTMLElement}
     */
    show(args) {
        args.isContentHtml ??= false;
        
        // 제목, 내용, 버튼 등을 감쌀 용소 생성
        const $modal = document.createElement('div');
        $modal.classList.add('--modal');
        
        // 제목을 담을 요소 생성
        const $title = document.createElement('div');
        $title.classList.add('--title');
        $title.innerText = args.title;
        
        // 내용을 담을 요소 생성
        const $content = document.createElement('div');
        $content.classList.add('--content');

        if (args.isContentHtml === true) {
            // 내용을 HTML로 적용
            $content.innerHTML = args.content;
        } else {
            // 내용을 텍스트로 적용
            $content.innerText = args.content;
        }
        // $modal, $title, $content를 사짓으로 추가
        $modal.append($title, $content);
        // arg.buttons가 존재하고 길이가 0보다 크면
        if (args.buttons != null && args.buttons.length > 0) {
            // $buttonContainer 생성
            const $buttonContainer = document.createElement('div');
            $buttonContainer.classList.add('--button-container');
            
            // args.button를 반복하여
            for (const button of args.buttons) {
                // args.button인자 하나 당 $button 요소 생성
                const $button = document.createElement('button');
                // -object-button이 공통 버튼 클래스이고 색상은 --color-? 형식임으로
                $button.classList.add('--button', '-object-button', `--color-${button.color ?? 'gray'}`);
                $button.setAttribute('type', 'button');
                $button.innerText = button.caption;
                // 버튼 클릭시 button 오브젝틀르 통해 전달 된 onclick 함수 실행. 이때 생성한 $modal 요소를 인자로 전달. 여기서 전달하는 $modal 요소는 호출자가 임의로 사용할 수 있으며 주 목적은 모달을 다시 숨기는데에 (dialog.hide) 있음.
                $button.addEventListener('click', () => button.onclick($modal));
                $buttonContainer.append($button);
            }
            $modal.append($buttonContainer);
        }
        this.#$element.append($modal);
        this.#$element.show();
        this.#$modals.push($modal);
        // $modal을 show하는데 50ms 딜레이를 주는 이유는 append하자마자 show하면 transition이 무시되기 때문, (10ms부터 괜찮긴하지만 50ms 안전함)
        setTimeout(() => $modal.show(), 50); // transition 때문에
    }

    /**
     *
     * @param {string} title
     * @param {string} content
     * @param {function(HTMLElement?)|undefined} onclick
     *
     * @return {HTMLElement}
     */
    showSimpleOk(title, content, onclick = undefined) {
        return this.show({
            title: title,
            content: content,
            buttons: [
                {caption: '확인',
                    onclick: ($modal) => {
                    if (typeof onclick === 'function') {
                        onclick($modal);
                    }
                    this.hide($modal);
                    }}
            ]
        });
    }
}

const dialog = new Dialog();