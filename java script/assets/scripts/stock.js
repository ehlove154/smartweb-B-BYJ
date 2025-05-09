const $loading = document.body.querySelector(':scope > .loading');
const $searchForm = document.body.querySelector(`:scope > .header >.search-form`);
const $chart = document.body.querySelector(`:scope > .chart`);

const hideLoading = () => {
    $loading.classList.remove('visible');
};

const showLoading = () => {
    $loading.classList.add('visible');
};

const closeChart = () => {
    $chart.classList.remove('visible');
};

const openChart = (code) => {
    const xhr = new XMLHttpRequest();
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        hideLoading();
        if (xhr.status < 200 || xhr.status >= 400) {
            alert('차트 데이터를 불러오지 못했습니다.');
            return;
        }
        const response = JSON.parse(xhr.responseText);
        const rawArray = response[`response`][`body`][`items`][`item`];
        const ohlcArray = [];
        const volumeArray = [];
        for (const rawObject of rawArray) {
            const dateString = rawObject[`basDt`];
            const year = parseInt(dateString.substring(0, 4));
            const month = parseInt(dateString.substring(4, 6));
            const day = parseInt(dateString.substring(6, 8));
            const date = new Date(year, month, day);
            const open = parseFloat(rawObject[`mkp`]);
            const hipr = parseFloat(rawObject[`hipr`]);
            const lopr = parseFloat(rawObject[`lopr`]);
            const dpr = parseFloat(rawObject[`dpr`]);
            const trqu = parseFloat(rawObject[`trqu`]);
            const close = parseFloat(rawObject['clpr']);
            const volume = parseFloat(rawObject['trqu']);
            ohlcArray.push({
                x: date,
                y: [open, hipr, lopr, close]
            });
            volumeArray.push({
                x: date,
                y: volume
            });
        }
        const $ohlcRender = $chart.querySelector(`:scope > .body > .chart.ohlc`);
        const ohlcOption = {
            series: [{data: ohlcArray}],
            chart: {
                id: `ohlc`,
                type: `candlestick`,
                height: 400,
                toolbar: {
                    autoSelect: `pan`,
                    show: false
                },
                zoom: {
                    enabled: false
                }
            },
            plotOptions: {
                candlestick: {
                    colors: {
                        upward: `#e74c3c`,
                        downward: `#2980b9`
                    }
                }
            },
            xaxis: {
                type: `datetime`
            }
        };
        $ohlcRender.innerHTML = '';
        const ohlcChart = new ApexCharts($ohlcRender, ohlcOption);
        ohlcChart.render();

        const $volumeRender = $chart.querySelector(`:scope > .body > .chart.volume`);
        const volumeOption = {
            series: [{
                name: `거래량`,
                data: volumeArray
            }],
            chart: {
                type: `bar`,
                height: 160,
                brush: {
                    enabled: true,
                    target: `ohlc`
                },
                selection: {
                    enabled: true,
                    xaxis: {
                        max: volumeArray[0].x.getTime(),
                        min: volumeArray[volumeArray.length - 1 < 250 ? volumeArray.length - 1 : 250].x.getTime()
                    },
                    fill: {
                        color: `#cccccc`,
                        opacity: 0.4
                    },
                    stroke: {
                        color: `#0b47a1`
                    }
                }
            },
            dataLabels: {
                enabled: false
            },
            plotOptions: {
                bar: {
                    columnWidth: `80%`,
                    colors: {
                        upward: `#e74c3c`,
                        downward: `#2980b9`
                    }
                }
            },
            stroke: {
                width: 0
            },
            xaxis: {
                type: `datetime`,
                axisBorder: {
                    offset: 13
                }
            },
            yaxis: {
                labels: {
                    show: false
                }
            }
        };

        $volumeRender.innerHTML = ``;
        const volumeChart = new ApexCharts($volumeRender, volumeOption);
        volumeChart.render();

        $chart.classList.add('visible');
    };
    const date = new Date();
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, `0`);
    const day = date.getDate().toString().padStart(2, `0`);
    const beginBasDt = `${year - 2}${month}${day}`;
    const endBasDt = `${year}${month}${day}`;
    const likeSrtnCd = code.replace(`A`, ``);
    xhr.open('Get', `https://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService/getStockPriceInfo?serviceKey=HHNbHnGxswdYzf2oDjiFwz4jioDDIts%2FghRBfiM8TW8yXUbbnpgv%2BN7AaeOXEaUV%2F18BD8%2FkATlKRlIzrUAKPg%3D%3D&numOfRows=5000&pageNo=1&resultType=json&beginBasDt=${beginBasDt}&endBasDt=${endBasDt}&likeSrtnCd=${likeSrtnCd}`);
    xhr.send();
    showLoading();
};

$chart.querySelector(`:scope > .header > [name='close']`).onclick = () => {
    closeChart();
};

$searchForm.onsubmit = (e) => {
    e.preventDefault();
    const keyword = $searchForm['keyword'].value;
    const $table = document.body.querySelector(':scope > .table');
    const $trs = document.body.querySelectorAll(':scope > .table > tbody > tr');
    $table.style.display = 'none';

    for (const $tr of $trs) {
        $tr.style.display = $tr.innerText.includes(keyword) === true ? 'table-row' : 'none';
    }
    $table.style.display = 'table';
};

document.body.querySelector(':scope > .header > .button[name="refresh"]').onclick = () => {
    const xhr = new XMLHttpRequest();
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        hideLoading();
        if (xhr.status < 200 || xhr.status >= 400) {
            alert(`요청을 전송하는 도중 오류가 발생하였습니다. (${xhr.status})`);
            return;
        }
        const response = JSON.parse(xhr.responseText);
        const stocks = response['response']['body']['items']['item'];
        const $tbody = document.body.querySelector(`:scope > .table > tbody`);
        let tbodyHtml = '';
        for (const stock of stocks) {
            const trHtml = `
                <tr data-code="${stock['srtnCd']}">
                    <th scope="row">${stock['srtnCd']}</th>
                    <td>${stock['isinCd']}</td>
                    <td>${stock['mrktCtg']}</td>
                    <td>${stock['itmsNm']}</td>
                    <td>${stock['crno']}</td>
                    <td>${stock['corpNm']}</td>
                    <td>
                        <div class="button-container">
                            <button class="button" type="button">재무재표</button>
                            <button class="button" type="button" name="chart">차트</button>
                        </div>
                    </td>
                </tr>`;
            tbodyHtml += trHtml;
        }
        $tbody.innerHTML = tbodyHtml;
        $tbody.querySelectorAll(`:scope > tr`).forEach($tr => {
            const $chartButton = $tr.querySelector(`button[name='chart']`);
            $chartButton.onclick = () => {
                const code = $tr.dataset['code'];
                openChart(code);
            };
        });
    };
    xhr.open('GET', 'https://apis.data.go.kr/1160100/service/GetKrxListedInfoService/getItemInfo?serviceKey=HHNbHnGxswdYzf2oDjiFwz4jioDDIts%2FghRBfiM8TW8yXUbbnpgv%2BN7AaeOXEaUV%2F18BD8%2FkATlKRlIzrUAKPg%3D%3D&numOfRows=5000&pageNo=1&resultType=json');
    xhr.send();
    showLoading();
}