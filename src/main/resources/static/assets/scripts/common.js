/* HTMLElement -> HTML 모든 태그 */


/**
 *
 * @returns {HTMLElement}
 */
HTMLElement.prototype.hide = function () {
    this.classList.remove('-visible');
    return this;
}

/**
 *
 * @param {boolean} b
 * @return {HTMLElement}
 */
HTMLElement.prototype.setValid = function (b) {
    if (b === true) {
        this.classList.remove('-invalid');
    } else if (b === false) {
        this.classList.add('-invalid');
    }
    return this;
}

/**
 *
 * @param {boolean} b
 * @return {HTMLElement}
 */
HTMLInputElement.prototype.setVisible = function (b) {
    if (b === true) {
        this.classList.add('-visible');
    } else if (b === false) {
        this.classList.remove('-visible');
    }
    return this;
}

/**
 *
 * @returns {HTMLElement}
 */
HTMLElement.prototype.show = function () {
    this.classList.add('-visible');
    return this;
}


/**
 *
 * @returns {HTMLInputElement}
 */
HTMLInputElement.prototype.focusAndSelect = function () {
    this.focus();
    this.select();
    return this;
}