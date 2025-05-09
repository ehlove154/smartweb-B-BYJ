document.addEventListener(`DOMContentLoaded`, function () {

    const citySelect = document.getElementById(`city-select`);
    const title = document.querySelector(`h2.title`);
    const indexDiv = document.getElementById(`index`);
    const indexButton = indexDiv.querySelectorAll(`:scope > .index-form > .list > .item > .button`);
    const weatherContainer = document.getElementById(`weather-container`);
    const placesContainer = document.getElementById(`place-container`);
    let places = [];
    let currentIndex = 0;

    indexButton.forEach(button => {
        button.addEventListener(`click`, () => {
            const selectedCity = button.value;

            title.textContent = selectedCity;
            indexDiv.classList.remove(`visible`);

            fetchWeather(selectedCity);
            getWeeklyForecast(selectedCity);
            fetchPlaces(selectedCity);
        });
    });

    citySelect.addEventListener(`change`, function () {
        const selectedCity = citySelect.value;

        if (selectedCity) {
            title.textContent = selectedCity;
            // indexDiv.classList.remove(`visible`);

            fetchWeather(selectedCity);
            getWeeklyForecast(selectedCity);
            fetchPlaces(selectedCity);
        }
    });

    function fetchWeather(city) {
        const weatherApiKey = `HHNbHnGxswdYzf2oDjiFwz4jioDDIts%2FghRBfiM8TW8yXUbbnpgv%2BN7AaeOXEaUV%2F18BD8%2FkATlKRlIzrUAKPg%3D%3D`;
        const baseDate = getTodayDate();
        const baseTime = `0500`;
        const [nx, ny] = getCityCoords(city);

        const weatherApiUrl = `https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=${weatherApiKey}&numOfRows=10&pageNo=1&dataType=JSON&base_date=${baseDate}&base_time=${baseTime}&nx=${nx}&ny=${ny}`;

        fetch(weatherApiUrl)
            .then(response => response.json())
            .then(data => {

                const items = data.response.body.items.item;
                const sky = items.find(item => item.category === `SKY`);
                const temp = items.find(item => item.category === `TMP`).fcstValue;
                const pop = items.find(item => item.category === `POP`).fcstValue;
                const VEC = items.find(item => item.category === `VEC`).fcstValue;
                const WSD = items.find(item => item.category === `WSD`).fcstValue;

                const $wetherNow = weatherContainer.querySelector(`:scope > .weather-now`);

                const weatherText = sky.fcstValue === `1` ? `맑음` : `흐림`;

                const html = `
                <h2 class="title">${city}</h2>
                <ul class="list">
                <li class="item">
                    <img src="${getWeatherIcon(weatherText)}" alt="${weatherText}" class="icon">
                    <span class="content tmp">${temp} ℃</span>
                </li>
                <li class="item">
                    <span class="content">${weatherText}</span>
                    <span class="content">강수확률 ${pop} %</span>
                </li>
                <li class="item">
                    <span class="content">풍향 ${VEC} deg</span>
                    <span class="content">풍속 ${WSD} m/s</span>
                </li>
            </ul>`
                $wetherNow.innerHTML += html;           })
            .catch(error => console.error(`날씨 데이터를 불러오지 못했습니다.`, error));
    }

    function getWeeklyForecast(city) {
        const regionCode = getCityCords(city);

        const weatherApiUrl = `http://dataservice.accuweather.com/forecasts/v1/daily/5day/${regionCode}?apikey=UkLW1SGJofLchyllC9BgnmkAt4zmVkDQ&language=ko-kr`;

        fetch(weatherApiUrl)
            .then(response => response.json())
            .then(data => {
                const items = data.DailyForecasts;
                const $weatherWeek =  weatherContainer.querySelector(`.weather-week > .list`);

                $weatherWeek.innerHTML=``;

                items.forEach((item) => {
                    const dateParts = item.Date.split(`T`)[0].split(`-`);
                    const month = dateParts[1];
                    const date = dateParts[2];

                    const day = item.Day.IconPhrase;
                    const night = item.Night.IconPhrase;


                    const minTemp = ((item.Temperature.Minimum.Value - 32) / 1.8).toFixed(1);
                    const maxTemp = ((item.Temperature.Maximum.Value - 32) / 1.8).toFixed(1);

                    const dayIcon = `https://developer.accuweather.com/sites/default/files/${String(item.Day.Icon).padStart(2, '0')}-s.png`;
                    const nightIcon = `https://developer.accuweather.com/sites/default/files/${String(item.Night.Icon).padStart(2, '0')}-s.png`;

                    const listItem = document.createElement(`li`);
                    listItem.classList.add(`item`);

                    listItem.innerHTML = `
                <li class="item">
                    <div class="content">
                        <span class="date">${month}/${date}</span>
                    </div>
                    <div class="content">
                        <img class="icon" src="${dayIcon}" alt="${day}">
                    </div>
                    <div class="content">
                        <img class="icon" src="${nightIcon}" alt="${night}">
                    </div>
                    <span class="content low">${minTemp} °C</span>
                    <span class="content high">${maxTemp} °C</span>
                </li>
                    `;

                    $weatherWeek.appendChild(listItem);
                })

            })
            .catch(error => console.error(`날씨 데이터를 불러오는 중 오류 발생:`, error));
    }

    function getWeatherIcon(weather) {
        if (weather.includes(`맑음`)) return `./assets/images/weather/index.main.weather-container.sun.png`;
        if (weather.includes(`흐림`)) return `./assets/images/weather/index.main.weather-container.clouds.png`;
        if (weather.includes(`소나기`, `비`)) return `./assets/images/weather/index.main.weather-container.rain.png`;
        if (weather.includes(`눈`)) return `./assets/images/weather/index.main.weather-container.snow.png`;
        if (weather.includes(`부분 맑음`)) return `./assets/images/weather/index.main.weather-container.partly-cloudy-day.png`;
        if (weather.includes(`안개`)) return `./assets/images/weather/index.main.weather-container.haze.png`;
        if (weather.includes(`천둥번개`)) return `./assets/images/weather/index.main.weather-container.storms.png`;
        return `./assets/images/index.airplane.png`;
    }

    function getCityCords(city) {
        const cords = {
            "서울": `226081`,
            "대전": `223352`,
            "인천": `224032`,
            "대구": `223347`,
            "울산": `226451`,
            "부산": `222888`,
            "광주": `223627`,
            "제주": `224209`,
            "경기도": `223671`,
            "강원도": `223556`,
            "충청북도": `223117`,
            "충청남도": `223143`,
            "전라북도": `223078`,
            "전라남도": `224257`,
            "경상북도": `2330417`,
            "경상남도": `223804`
        };
        return cords[city] || `11B20201`;
    }

    function getCityCoords(city) {
        const coords = {
            "서울": [60, 127],
            "대전": [67, 100],
            "인천": [55, 124],
            "대구": [89, 90],
            "울산": [102, 84],
            "부산": [98, 76],
            "광주": [58, 74],
            "제주": [52, 38],
            "경기도": [60, 120],
            "강원도": [73, 134],
            "충청북도": [69, 107],
            "충청남도": [68, 100],
            "전라북도": [63, 89],
            "전라남도": [51, 67],
            "경상북도": [87, 106],
            "경상남도": [91, 77]
        };
        return coords[city] || [60, 127];
    }

    function getTodayDate() {
        const today = new Date();
        const year = today.getFullYear();
        const month = String(today.getMonth() + 1).padStart(2, `0`);
        const day = String(today.getDate()).padStart(2, `0`);
        return `${year}${month}${day}`;
    }

    function fetchPlaces(city) {
        const kakaoApiKey = `ba863ba8034f3a1f63c3cc6cc02ae1ba`;
        const categories = [`관광지`, `음식점`, `숙소`];

        indexButton.forEach(button => {
            button.addEventListener(`click`, () => {
                const selectedCity = button.getAttribute("value");
                updateCity(selectedCity);
            })
        })

        const fetchPromises = categories.map(category => {
            const kakaoApiUrl = `https://dapi.kakao.com/v2/local/search/keyword.json?query=${city} ${category}`;

            return fetch(kakaoApiUrl, {
                headers: {Authorization: `KakaoAK ${kakaoApiKey}`}
            })
                .then(response => response.json())
                .then(data => data.documents.slice(0, 4));
        });

        Promise.all(fetchPromises)
            .then(results => {
                places = results.flat();
                currentIndex = 0;
                updatePlaces();
            })
            .catch(error => console.error(`장소 데이터를 불러오지 못했습니다.`, error));
    }

    let placeOverlay = new kakao.maps.CustomOverlay({zIndex:1}), contentNode = document.createElement(`div`), markers = [], currCategory = ``;

    const mapContainer = document.getElementById(`map`), mapOption = {
        center: new kakao.maps.LatLng(37.566826, 126.9786567),
        level: 3
    };

    const map = new kakao.maps.Map(mapContainer, mapOption);

    const ps = new kakao.maps.services.Places(map);

    kakao.maps.event.addListener(map, `idle`, searchPlaces);

    contentNode.className = `placeinfo_wrap`;

    addEventHandle(contentNode, `mousedown`, kakao.maps.event.preventMap);
    addEventHandle(contentNode, `touchstart`, kakao.maps.event.preventMap);
    // document.addEventListener("wheel", someFunction, { passive: true });
    // document.addEventListener("touchstart", someFunction, { passive: true });

    placeOverlay.setContent(contentNode);

    addCategoryClickEvent();

    function addEventHandle (target, type, callback) {
        if (target.addEventListener) {
            target.addEventListener(type, callback);
        } else {
            target.attachEvent(`on` + type, callback);
        }
    }

    function searchPlaces() {
        if (!currCategory) {
            return;
        }

        placeOverlay.setMap(null);

        removeMarker();

        ps.categorySearch(currCategory, placesSearchCB, {useMapBounds:true});
    }

    function placesSearchCB(data, status, pagination) {
        if (status === kakao.maps.services.Status.OK){
            displayPlaces(data);
        } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
            alert(`검색결과가 없습니다.`)
        } else if (status === kakao.maps.services.Status.ERROR) {
            alert(`Error`)
        }
    }

    function displayPlaces(places) {
        const order = document.getElementById(currCategory).getAttribute(`data-order`);

        for (let i = 0; i < places.length; i++) {
            const marker = addMarker(new kakao.maps.LatLng(places[i].y, places[i].x), order);

            (function (marker, place) {
                kakao.maps.event.addEventListener(marker, `click`, function () {
                    displayPlaceInfo(place);
                });
            })(marker, places[i]);
        }
    }

    function addMarker(position, order) {
        const imageSrc = `https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/places_category.png`,
            imageSize = new kakao.maps.Size(27, 28),
            imgOptions = {
                spriteSize : new kakao.maps.Size(72,208),
                spriteOrigin : new kakao.maps.Point(46, (order+36)),
                offset : new kakao.maps.Point(11, 28)
            },
            markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions),
            marker = new kakao.maps.Marker({
                position: position,
                image: markerImage
            });
        marker.setMap(map);
        markers.push(marker);

        return marker;
    }

    function removeMarker() {
        for (let i = 0; i < markers.length; i++) {
            markers[i].setMap(null);
        }
        markers = [];
    }

    function displayPlaceInfo (place) {
        let content = `<div class="placeinfo">` +
            `<a class="title" href="` + place.place_url + `" target="_blank" title="` + place.place_name + `">`;

        if (place.road_address_name) {
            content += `<span title="'+ place.road_address_name +'">` + place.road_address_name + `</span>` + '<span class="Jibun" title="' + place.address_name +'">(지번 : ' + place.address_name + ')</span>';
        } else {
            content += '<span title="' + place.address_name +'">' + place.address_name + '</span>'
        }
        content += place.phone ? `<span class="tel">` + place.phone + `</span>` : ``;
        content += `</div><div class="after"></div>`;

        contentNode.innerHTML = content;
        placeOverlay.setPosition(new kakao.maps.LatLng(place.y, place.x));
        placeOverlay.setMap(map);
    }

    function addCategoryClickEvent() {
        const category = document.getElementById(`category`), children = category.children;

        for (let i=0; i<children.length; i++) {
            children[i].onclick = onClickCategory;
        }
    }

    function onClickCategory() {
        const id = this.id,
            className = this.className;

        placeOverlay.setMap(null);

        if (className === `on`) {
            currCategory = ``;
            changeCategoryClass();
            removeMarker();
        } else {
            currCategory = id;
            changeCategoryClass(this);
            searchPlaces();
        }
    }

    function changeCategoryClass(el) {
        let category = document.getElementById(`category`),
            children = category.children,
            i;

        for (i=0; i<children.length; i++) {
            children[i].className = ``;
        }
        if (el) {
            el.className = `on`;
        }
    }
});