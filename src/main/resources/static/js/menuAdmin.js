$(document).ready(function () {
    // checkToken();
    // setupAjax();

    $("#submitBtn").on("click", function (event) {
        event.preventDefault(); // 기본 제출 막기

        const fileInput = $("#img")[0].files[0];
        if (!fileInput) {
            alert("이미지를 업로드해야 합니다.");
            return;
        }

        const getValue = (name, allowEmpty = false) => {
            const val = $(`[name='${name}']`).val();
            return allowEmpty ? val : val || null;
        };

        const price = parseInt(getValue("price"), 10);
        const calorie = parseFloat(getValue("calorie"));

        if (isNaN(price) || price <= 0) {
            alert("가격은 0보다 큰 숫자여야 합니다.");
            return;
        }

        if (isNaN(calorie) || calorie < 0) {
            alert("칼로리는 0 이상 숫자여야 합니다.");
            return;
        }

        const formData = new FormData();
        formData.append("file", fileInput);

        const menuData = {
            menuName: getValue("menuName"),
            price: price,
            calorie: calorie,
            bread: getValue("bread"),
            material1: getValue("material1"),
            material2: getValue("material2"),
            material3: getValue("material3"),
            cheese: getValue("cheese"),
            vegetable1: getValue("vegetable1"),
            vegetable2: getValue("vegetable2"),
            vegetable3: getValue("vegetable3"),
            vegetable4: getValue("vegetable4"),
            vegetable5: getValue("vegetable5"),
            vegetable6: getValue("vegetable6"),
            vegetable7: getValue("vegetable7"),
            vegetable8: getValue("vegetable8"),
            sauce1: getValue("sauce1"),
            sauce2: getValue("sauce2"),
            sauce3: getValue("sauce3"),
            status: getValue("status") === "active" ? "ACTIVE" : "DELETED"
        };

        const jsonBlob = new Blob([JSON.stringify(menuData)], { type: "application/json" });
        formData.append("menu", jsonBlob);

        $.ajax({
            url: "/menus",
            type: "POST",
            data: formData,
            enctype: "multipart/form-data",
            processData: false,
            contentType: false,
            success: function () {
                alert("메뉴가 성공적으로 등록되었습니다!");
                window.location.href = "/menus/list";
            },
            error: function (xhr) {
                console.error("등록 실패:", xhr.status, xhr.responseText);
                alert("메뉴 등록 중 오류가 발생했습니다:\n" + xhr.responseText);
            }
        });
    });
});
