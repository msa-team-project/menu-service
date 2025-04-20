$(document).ready(() => {
    // 카카오 주소 API 호출
    $('#postcode,#address').click(() => {
        execDaumPostcode();
    });

    // 지점 등록 버튼 클릭 이벤트
    $('#storeRegister').click(async (event) => {
        event.preventDefault(); // 기본 동작 막기

        const button = $(event.target);
        button.prop('disabled', true); // 버튼 비활성화

        try {
            // 입력 데이터 수집
            const storeName = $('#store_name').val();
            const address = $('#address').val();
            const postcode = $('#postcode').val();
            const status = 'ACTIVE';

            if (!storeName || !address || !postcode) {
                alert('모든 항목을 입력해주세요.');
                return;
            }

            // 카카오 지도 API를 통해 주소 좌표 변환
            const coordinates = await getGeoCoding(address);

            if (!coordinates) {
                alert('주소를 변환하는 데 실패했습니다.');
                return;
            }

            // 서버로 전송할 데이터 생성
            const formData = {
                storeName,
                storeAddress: address,
                storePostcode: postcode,
                storeLatitude: coordinates.lat,
                storeLongitude: coordinates.lng,
                storeStatus: status,
            };

            console.log('전송 데이터: ', formData);

            // 서버로 데이터 전송
            await sendDataToServer(formData);

            alert('지점가입이 성공했습니다.');
            window.location.href = '/store/storelist'; // 페이지 이동
        } catch (error) {
            console.error('오류 발생: ', error);
            alert('처리 중 문제가 발생했습니다. 다시 시도해주세요.');
        } finally {
            button.prop('disabled', false); // 버튼 활성화
        }
    });

    // 카카오 주소 찾기 함수
    function execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function (data) {
                $('#postcode').val(data.zonecode);
                $('#address').val(data.address);

            },
        }).open();
    }


    // 주소 좌표 변환 함수 (카카오 지도 REST API 사용)
    const getGeoCoding = async (address) => {
        const url = `https://dapi.kakao.com/v2/local/search/address.json?query=${encodeURIComponent(
            address
        )}`;

        try {
            const response = await $.ajax({
                url: url,
                method: 'GET',
                headers: {
                    Authorization: `${kakaoApiKey}`,
                },
            });

            if (response.documents && response.documents.length > 0) {
                const { x, y } = response.documents[0]; // 경도(x), 위도(y) 추출
                return { lat: y, lng: x };
            } else {
                console.error('좌표를 찾을 수 없음: ', response);
                return null;
            }
        } catch (error) {
            console.error('API 요청 실패: ', error);
            throw error;
        }
    };

    // 서버 데이터 전송 함수
    const sendDataToServer = async (formData) => {
        return $.ajax({
            type: 'POST',
            url: '/stores',
            data: JSON.stringify(formData),
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
        });
    };
});