$(document).ready(function () {
    $("#menuForm").on("submit", function (event) {
        event.preventDefault(); // 기본 제출 방지

        let fileInput = $("#img")[0].files[0];

        if (!fileInput) {
            alert("이미지를 업로드해야 합니다.");
            return;
        }

        let formData = new FormData();
        formData.append("file", fileInput); // 이미지 파일 추가

        let menuData = {
            menuName: $("input[name='menuName']").val(),
            price: parseInt($("input[name='price']").val(), 10),
            calorie: parseFloat($("input[name='calorie']").val()),
            bread: $("select[name='bread']").val(),
            material1: $("select[name='material1']").val(),
            material2: $("select[name='material2']").val() || null,
            material3: $("select[name='material3']").val() || null,
            cheese: $("select[name='cheese']").val() || null,
            vegetable1: $("select[name='vegetable1']").val(),
            vegetable2: $("select[name='vegetable2']").val() || null,
            vegetable3: $("select[name='vegetable3']").val() || null,
            vegetable4: $("select[name='vegetable4']").val() || null,
            vegetable5: $("select[name='vegetable5']").val() || null,
            vegetable6: $("select[name='vegetable6']").val() || null,
            vegetable7: $("select[name='vegetable7']").val() || null,
            vegetable8: $("select[name='vegetable8']").val() || null,
            sauce1: $("select[name='sauce1']").val(),
            sauce2: $("select[name='sauce2']").val() || null,
            sauce3: $("select[name='sauce3']").val() || null,
            status: $("select[name='status']").val() === "active" ? "ACTIVE" : "DELETED"
        };

        // ✅ JSON 데이터를 Blob으로 변환하여 FormData에 추가
        let jsonBlob = new Blob([JSON.stringify(menuData)], { type: "application/json" });
        formData.append("menu", jsonBlob); // 백엔드에서 @RequestPart("menu")와 일치

        // ✅ Ajax 요청
        $.ajax({
            url: "/menus",
            type: "POST",
            data: formData,
            enctype: "multipart/form-data",
            processData: false,
            contentType: false,
            success: function () {
                alert("메뉴가 성공적으로 등록되었습니다!");
                // window.location.href = "/menus/list";
            },
            error: function (xhr) {
                console.error("Error:", xhr.responseText);
                alert("메뉴 등록 중 오류가 발생했습니다.");
            }
        });
    });
});
