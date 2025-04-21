$(document).ready(function () {

    // ✅ 전체 선택/해제
    $("#selectAll").on("change", function () {
        $(".item-checkbox").prop("checked", this.checked);
    });

    // ✅ 항목 체크 시 전체 선택 체크박스 상태 동기화
    $(document).on("change", ".item-checkbox", function () {
        const allChecked = $(".item-checkbox").length === $(".item-checkbox:checked").length;
        $("#selectAll").prop("checked", allChecked);
    });

    // ✅ 수량 변경 버튼 처리
    $(".update-btn").on("click", function () {
        const row = $(this).closest("tr");
        const id = row.data("id");
        const newAmount = parseInt(row.find(".amount-input").val());

        if (!newAmount || newAmount < 1) {
            alert("수량은 1 이상이어야 합니다.");
            return;
        }

        updateCartItemAmount(id, newAmount);
    });

    // ✅ 개별 삭제 버튼 처리
    $(".delete-btn").on("click", function () {
        const row = $(this).closest("tr");
        const id = row.data("id");

        if (confirm("정말 삭제하시겠습니까?")) {
            deleteCartItem(id, row);
        }
    });

    // ✅ 선택 항목 삭제
    $("#deleteSelected").click(function (e) {
        e.preventDefault();

        const selectedIds = $(".item-checkbox:checked").map(function () {
            return $(this).val();
        }).get();

        if (selectedIds.length === 0) {
            alert("삭제할 항목을 선택하세요.");
            return;
        }

        if (!confirm(`${selectedIds.length}개 항목을 삭제할까요?`)) return;

        $.post(`/menus/cart/delete-selected`, $.param({ selectedIds }))
            .done(response => {
                updateCartSummary(response.cartItems, response.totalQuantity, response.totalPrice);
            })
            .fail(() => alert("선택 삭제 실패"));
    });

    // ✅ 결제하기
    $("#checkout").click(function () {
        const $btn = $(this).prop("disabled", true).text("처리 중...");
        $.post(`/menus/cart/order/checkout`)
            .done(() => {
                alert("결제 완료!");
                location.reload();
            })
            .fail(response => {
                alert(response.responseText);
            })
            .always(() => $btn.prop("disabled", false).text("결제하기"));
    });

    // ✅ 페이지 로딩 시 총합 갱신
    updateCartSummary();
});

// ✅ 장바구니 항목 수량 업데이트
function updateCartItemAmount(id, newAmount) {
    $.ajax({
        url: `/menus/cart/update/${id}`,
        type: "POST",
        data: { amount: newAmount },
        success: function (response) {
            updateCartSummary(response.cartItems, response.totalQuantity, response.totalPrice); // 서버에서 계산된 값으로 갱신
        },
        error: function () {
            alert("수량 변경 실패");
        }
    });
}

// ✅ 장바구니 항목 삭제
function deleteCartItem(id, row) {
    $.ajax({
        url: `/menus/cart/delete/${id}`,
        type: "POST",
        success: function (response) {
            row.remove();  // DOM에서 제거
            updateCartSummary(response.cartItems, response.totalQuantity, response.totalPrice);  // 서버에서 갱신된 값으로 반영
        },
        error: function () {
            alert("삭제 실패");
        }
    });
}

// ✅ 장바구니 요약 정보 실시간 업데이트
function updateCartSummary(cartItems = null, totalQuantity = null, totalPrice = null) {
    if (cartItems) {
        // 서버에서 받은 데이터로 갱신
        let totalQuantity = 0;
        let totalPrice = 0;

        cartItems.forEach(item => {
            totalQuantity += item.amount;
            totalPrice += item.price * item.amount;
        });

        $("#totalQuantity").text(totalQuantity);
        $("#totalPrice").text(totalPrice.toLocaleString());
    } else {
        // 클라이언트에서 계산
        let totalQuantity = 0;
        let totalPrice = 0;

        $(".amount-input").each(function () {
            const $row = $(this).closest("tr");
            const amount = parseInt($(this).val()) || 0;
            const unitPrice = parseInt($row.find(".item-price").text()) || 0;
            const rowTotal = amount * unitPrice;

            // 각 행의 총액도 갱신
            $row.find(".total-cell").text(rowTotal.toLocaleString());

            totalQuantity += amount;
            totalPrice += rowTotal;
        });

        $("#totalQuantity").text(totalQuantity);
        $("#totalPrice").text(totalPrice.toLocaleString());
    }

    $("#backToHome").click(function () {
        window.location.href = "/";  // 홈 페이지로 리디렉션
    });
}
