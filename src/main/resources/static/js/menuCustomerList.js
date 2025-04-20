$(document).ready(function() {
    $(".add-cart-form").submit(function(e) {
        e.preventDefault(); // 기본 폼 전송 막기

        const form = $(this);
        const menuId = form.data("menu-id");
        const amount = form.find("input[name='amount']").val();

        $.ajax({
            type: "POST",
            url: "/api/cart/add",
            data: { menuId: menuId, amount: amount },
            success: function(response) {
                // alert("ENJOY");
                window.location.href = "/cart";
            },
            error: function(xhr) {
                alert("주문 오류: " + xhr.responseText);
            }
        });
    });
});
