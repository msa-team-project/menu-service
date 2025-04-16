$(document).ready(function () {

    const defaultExcludeTexts = [
        "선택 안 함", "빵을 선택하세요", "소스를 선택하세요", "재료를 선택하세요", "채소를 선택하세요"
    ];

    // 합산 함수
    function calculateTotal() {
        let totalPrice = 0;
        let totalCalorie = 0;
        let selectionText = [];

        $('select').each(function () {
            const selected = $(this).find(':selected');
            const price = parseInt(selected.data('price')) || 0;
            const calorie = parseFloat(selected.data('calorie')) || 0;
            const text = selected.text();

            totalPrice += price;
            totalCalorie += calorie;

            if (text && !defaultExcludeTexts.includes(text)) {
                selectionText.push(text);
            }
        });

        $('input[name="price"]').val(totalPrice);
        $('input[name="calorie"]').val(totalCalorie.toFixed(1));

        $('#totalPriceText').text(totalPrice);
        $('#totalCalorieText').text(totalCalorie.toFixed(1));
        $('#selectionText').text(selectionText.length ? selectionText.join(" / ") : "선택 항목이 없습니다.");
    }

    // 선택 항목 검증 함수
    function validateSelections() {
        return $('select[name="bread"]').val() && $('select[name="material1"]').val();
    }

    // 선택값 가져오는 헬퍼
    function getSelectValue(name) {
        return $(`select[name="${name}"]`).val();
    }

    // 커스텀 카트 추가 함수
    function addCustomCart() {
        const customCartDTO = {
            breadId: getSelectValue("bread"),
            material1Id: getSelectValue("material1"),
            material2Id: getSelectValue("material2"),
            material3Id: getSelectValue("material3"),
            cheeseId: getSelectValue("cheese"),
            sauce1Id: getSelectValue("sauce1"),
            sauce2Id: getSelectValue("sauce2"),
            sauce3Id: getSelectValue("sauce3"),
            price: parseInt($('input[name="price"]').val()) || 0,
            calorie: parseFloat($('input[name="calorie"]').val()) || 0,
            vegetableIds: []
        };

        for (let i = 1; i <= 8; i++) {
            const vegId = getSelectValue(`vegetable${i}`);
            if (vegId) {
                customCartDTO.vegetableIds.push(vegId);
            }
        }

        $.ajax({
            url: '/api/customCart',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(customCartDTO),
            headers: {
                'sessionId': sessionStorage.getItem("sessionId") || "guest"
            },
            success: function (response) {

                addToCartWithCustomCart(response.customCartId);
            },
            error: function () {
                alert('커스텀 카트에 저장하는데 실패했습니다. 다시 시도해 주세요.');
            }
        });
    }

    // 커스텀 카트를 장바구니에 추가
    function addToCartWithCustomCart(customCartId) {
        $.ajax({
            url: `/api/customCart/${customCartId}`,
            type: 'POST',
            success: function () {
                alert('장바구니에 담겼습니다!');
                location.href = '/cart';
            },
            error: function () {
                alert('카트에 추가하는데 실패했습니다. 다시 시도해 주세요.');
            }
        });
    }

    // 폼 제출 핸들링
    $('#menuForm').on('submit', function (e) {
        e.preventDefault();

        if (!validateSelections()) {
            $('#warningMessage').show();
            return;
        }

        $('#warningMessage').hide();
        addCustomCart();
    });

    // 초기 계산 및 변경 시 계산
    calculateTotal();
    $('select').on('change', calculateTotal);
});
