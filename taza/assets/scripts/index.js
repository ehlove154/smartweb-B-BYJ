const $addressDialog = document.getElementById(`addressDialog`);

const $dialogCover = document.getElementById ('dialogCover'); //결제 완료 후 확인창
const $dialog = document.getElementById(`dialog`);

const hideDialog = () => {
    $dialogCover.classList.remove('visible');
    $dialog.classList.remove('visible');
}

const showDialog = (content, onclick) => {
    $dialogCover.classList.add('visible');
    $dialog.querySelector(`:scope > .content`).innerText = content;
    $dialog.querySelector(`:scope > .ok`).onclick = () => {
        if (typeof onclick === `function`) {
            onclick();
        }
        hideDialog();
    };
    $dialog.classList.add(`visible`);
}

const hideAddressDialog = () => $addressDialog.classList.remove(`visible`);

const showAddressDialog = (callback) => { //주소찾기 창 불러오기
    const $content = $addressDialog.querySelector(`:scope > .content`);
    $content.innerHTML = ``;
    new daum.Postcode({
        width: `100%`,
        height: `100%`,
        oncomplete: (data) => {
            // console.log(data);
            if (typeof callback === `function`) {
                callback(data);
            }
        }
    }).embed($content);

    $addressDialog.classList.add(`visible`);
}
const $payDialog = document.getElementById(`payDialog`);
const hidePayDialog = () => $payDialog.classList.remove(`visible`);
const showPayDialog = (args) => {
    $payDialog.querySelector(`:scope > .content > .spec > .item > .value.depart`).innerText = args[`depart`];
    $payDialog.querySelector(`:scope > .content > .spec > .item > .value.destination`).innerText = args[`destination`];
    $payDialog.querySelector(`:scope > .content > .spec > .item > .value.car-type`).innerText = args[`carType`];
    $payDialog.querySelector(`:scope > .content > .spec > .item > .value.charge`).innerText = args[`charge`].toLocaleString() + `원`;
    $payDialog.querySelector(`:scope > .content > .button-container > .button.cancel`).onclick = () => hidePayDialog();
    $payDialog.querySelector(`:scope > .content > .button-container > .button.confirm`).onclick = () => {
        const date = new Date();
        const imp = window.IMP;
        imp.init(`imp36544176`);
        imp.request_pay({
            pg: `kakaopay.TC0ONETIME`,
            pay_method: `card`,
            merchant_uid: `IMP-${date.getTime()}`,
            name: `TAZA Taxi`,
            amount: args[`charge`],
            buyer_email: `ehlove154@naver.com`,
            buyer_name: `백유정`
        }, (resp) => {
            if (resp.success === true) {
                const history = {
                    timestamp: date.getTime(),
                    depart: args[`depart`],
                    destination: args[`destination`],
                    charge: args[`charge`]
                };
                const histories = JSON.parse(localStorage.getItem(`histories`)) ?? [];
                histories.push(history);
                localStorage.setItem(`histories`, JSON.stringify(histories));
                showDialog(`결제가 완료되었습니다.\n출발지로 택시가 곧 도착할 예정입니다.`, () => hidePayDialog());
            } else {
                showDialog(`결제에 실패하셨습니다.\n(${resp['error_msg']})`);
            }
        });

    };
    $payDialog.classList.add(`visible`);
};

const $nav = document.getElementById(`nav`);
$nav.querySelector(`:scope > .history`).onclick = () => showHistoryDialog();

const $historyDialog = document.getElementById(`historyDialog`);
const hideHistoryDialog = () => $historyDialog.classList.remove(`visible`);
const showHistoryDialog = (args) => {
    const $list = $historyDialog.querySelector(`:scope > .content > .list`); //ul 태그
    $list.querySelectorAll(`.item`).forEach(($list) => $list.remove());
    const histories = JSON.parse(localStorage.getItem(`histories`)) ?? []; // a ?? b 꼴에서 ?? 연산자는 a가 null 혹은 undefined라면 b로 대체한다.

    const $empty = $list.querySelector(`:scope > .message.empty`);
    if (histories.length === 0) {
        $empty.classList.add(`visible`);
        return;
    }
    $empty.classList.remove(`visible`);
    for (const history of histories) {
        const date = new Date(history[`timestamp`]); //(1741916447549);
        const year = date.getFullYear();
        const month = (date.getMonth() + 1).toString().padStart(2, `0`); //date.toISOString(); =>> `2025-03-14T06:27:12.408z`
        //'대구 중구 중앙대로'.split(` `); =>> [`대구`, `중구`, `중앙대로`] 배열로 쪼개줌
        //date.toISOString().split(`T`).map((s) => s.split(`.`)[0];
        //date.toISOString().split(`T`).map((s) => s.split(`.`)[0].join(` `);
        // =>>`2025-03-14 06:27:12`
        const day = date.getDate().toString().padStart(2, `0`);
        const hour = date.getHours().toString().padStart(2, `0`);
        const minute = date.getMinutes().toString().padStart(2, `0`);
        const second = date.getSeconds().toString().padStart(2, `0`);
        const timestamp = `${year}-${month}-${day} ${hour}:${minute}:${second}`;
        const html =`
        <li class="item">
                <span class="timestamp">${timestamp}</span>
                <span class="detail">
                    <span class="depart">${history[`depart`]}</span>
                    <span class="arrow">&#x2192;</span> <!-- 오른쪽 화살표 -->
                    <span class="destination">${history[`destination`]}</span>
                    <span class="stretch"></span>
                    <span class="charge">${history[`charge`].toLocaleString()}원</span>
                </span>
        </li>`;
        $list.innerHTML += html;
    }
    $historyDialog.classList.add(`visible`);
};
$historyDialog.querySelector(`:scope > .content > .button-container > .close`).onclick = () => hideHistoryDialog();

const $console = document.getElementById(`console`);
const $locationForm = $console.querySelector(`:scope > .location-form`);
const departInfoWindow = new kakao.maps.InfoWindow({ //출발지 maker 위 출발지 info 창
    content: `<div style="width: 150px; padding: 0.5rem 0; text-align: center;">출발지</div>`
});

const destinationInfoWindow = new kakao.maps.InfoWindow({ //도착지 maker 위 도착지 info 창
    content: `<div style="width: 150px; padding: 0.5rem 0; text-align: center;">도착지</div>`
});

let departMarker; //출발지 maker
let departCoordinates; //출발지 주소 좌표
let destinationMarker; //도착지 maker
let destinationCoordinates; //도착지 주소 좌표

$locationForm[`findDepart`].addEventListener(`click`, () => { //출발지 주소 찾기
    showAddressDialog((data) => {
        $locationForm[`addressDepart`].value = data[`roadAddress`];
        hideAddressDialog();
        convertAddressToCoordinates(data[`roadAddress`], (coordinates) => { // 출발지 주소 검색과 지도에서의 위치 맵핑하기
            departCoordinates = coordinates;
            departMarker?.setMap(null);
            departMarker = new kakao.maps.Marker({
                map: mapInstance,
                position: coordinates
            });
            departInfoWindow.open(mapInstance, departMarker);
            if (departCoordinates == null || destinationCoordinates == null) {
                mapInstance.setCenter(coordinates);
                mapInstance.setLevel(3); // 확대 수준
            } else {
                findPath();
            }

            // mapInstance.setCenter(coordinates);// 화면가운데로 지정
        });
    });
});
$locationForm[`findDestination`].addEventListener(`click`, () => { //도착지 주소 찾기
    showAddressDialog((data) => {
        $locationForm[`addressDestination`].value = data[`roadAddress`];
        hideAddressDialog();
        convertAddressToCoordinates(data[`roadAddress`], (coordinates) => { // 도착지 주소 검색과 지도에서의 위치 맵핑하기
            destinationCoordinates = coordinates;
            destinationMarker?.setMap(null);
            destinationMarker = new kakao.maps.Marker({
                map: mapInstance,
                position: coordinates
            });
            destinationInfoWindow.open(mapInstance, destinationMarker);
            if (departCoordinates == null || destinationCoordinates == null) {
                mapInstance.setCenter(coordinates);
                mapInstance.setLevel(3); // 확대 수준
            } else {
                findPath();
            }
            // mapInstance.setCenter(coordinates);// 화면가운데로 지정
        });
    });
});


let polyline;
const findPath = () => {
    const bounds = new kakao.maps.LatLngBounds();//출도착지를 한 화면에 들어오게
    bounds.extend(departCoordinates);
    bounds.extend(destinationCoordinates);
    mapInstance.setBounds(bounds);

    const xhr = new XMLHttpRequest(); // 경로 표시
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 400) {
            alert(`경로를 찾을 수 없습니다.`);
            return;
        }
        const response = JSON.parse(xhr.responseText);
        const roads = response[`routes`][0][`sections`][0][`roads`];
        const paths = [];
        for (const road of roads) {
            road[`vertexes`].forEach((v, i, a) => {
                if (i % 2 === 0) {
                    paths.push(new kakao.maps.LatLng(a[i + 1], a[i]));
                }
            });
        }

        polyline?.setMap(null);
        polyline = new kakao.maps.Polyline({
            path: paths,
            strokeWeight: 6,
            strokeColor: `#3498db`,
            strokeOpacity: 0.8,
            strokeStyle: `solid`
        });
        polyline.setMap(mapInstance);

        const guides = response[`routes`][0][`sections`][0][`guides`]; // 거리
        const $duration = $console.querySelector(`:scope > .estimate > .text-container > .text > .value.duration`); // 예상 시간
        const $charge = $console.querySelector(`:scope > .estimate > .text-container > .text > .value.charge`); // 예상 요금
        const $route = $console.querySelector(`:scope > .route`);
        let distance = 0;
        let duration = 0;
        $route.querySelectorAll(`:scope > .item`).forEach(($item) => $item.remove());
        $route.querySelector(`:scope > .message.empty`).classList.remove(`visible`);
        for (const guide of guides) {
            distance += guide[`distance`];
            duration += guide[`duration`];
            if (guide[`name`].length > 0) {
                const  $name = document.createElement(`span`);
                $name.classList.add(`name`);
                $name.innerHTML = guide[`name`];
                const $guidance = document.createElement(`span`);
                $guidance.classList.add(`guidance`);
                $guidance.innerHTML = guide[`guidance`];
                const $item = document.createElement('li');
                $item.classList.add(`item`);
                $item.append($name, $guidance);
                $item.addEventListener(`click`, () => {
                   const pos = new kakao.maps.LatLng(guide[`y`], guide[`x`]);
                   mapInstance.setCenter(pos);
                   mapInstance.setLevel(3);
                });
                $route.append($item);
            }
        }

        const charge = 4500 + Math.trunc(distance / 131) * 100 + Math.trunc(duration / 30 ) * 100;
        const $estimate = $console.querySelector(`:scope > .estimate`);
        const $pay = $console.querySelector(`:scope > .pay`);
        $duration.innerText = Math.trunc(duration / 60) +`분`;
        $charge.innerText = charge.toLocaleString() + `원`;
        $estimate.classList.add(`visible`);
        $console.querySelector(`:scope > .estimate`).classList.add(`visible`);
        $pay.innerText = `${charge.toLocaleString()}원 결제하기`;
        $pay.classList.add(`visible`);
        $pay.onclick = () => {
            showPayDialog({
                depart: $locationForm[`addressDepart`].value,
                destination: $locationForm[`addressDestination`].value,
                carType: $estimate.querySelector(`:scope > .car`)[`type`].value,
                charge: charge
            });
        }

        const $car = $console.querySelector(`:scope > .estimate > .car`);
        $car[`type`].value = `basic`;

    };
    const url = new URL(`https://apis-navi.kakaomobility.com/v1/directions`);
    url.searchParams.set(`origin`, `${departCoordinates.getLng()},${departCoordinates.getLat()}`);
    url.searchParams.set(`destination`, `${destinationCoordinates.getLng()},${destinationCoordinates.getLat()}`);
    xhr.open(`GET`, url.toString());
    xhr.setRequestHeader(`Authorization`, `KakaoAK 035fada9db6e80df1217b56055f46b17`);
    xhr.setRequestHeader(`Content-Type`, `application/json`);
    xhr.send();
}



const $map = document.getElementById(`map`); //지도 불러오기
const mapInstance = new kakao.maps.Map($map, {
    center: new kakao.maps.LatLng(35.8655753, 128.59339),
    level: 3,
});

const convertAddressToCoordinates = (address, callback) => {
    const geocoder = new kakao.maps.services.Geocoder();
    geocoder.addressSearch(address, (result, status) => {
        if (status === kakao.maps.services.Status.OK) {
            const coordinates = new kakao.maps.LatLng(result[0].y, result[0].x);
            if (typeof callback === `function`) {
                callback(coordinates);
            }
        }
    })
}