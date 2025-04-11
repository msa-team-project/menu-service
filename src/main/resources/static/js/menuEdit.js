$(document).ready(function () {
    // checkToken();
    // setupAjax();

    $("#updateBtn").on('click', function () {

        const formData = new FormData();
        const fileInput = $('#img')[0].files[0];

        if (fileInput) {
            formData.append("file", fileInput); // 새 이미지가 있을 때만
        }

        // 셀렉터에서 값 추출 (null 허용)
        const getValue = (name, allowEmpty = false) => {
            const val = $(`[name="${name}"]`).val();
            return allowEmpty ? val : val || null;
        };

        const menuData = {
            menuName: getValue('menuName'),
            price: parseInt(getValue('price'), 10),
            calorie: parseFloat(getValue('calorie')),

            bread: getValue('breadId'),
            material1: getValue('material1Id'),
            material2: getValue('material2Id'),
            material3: getValue('material3Id'),
            cheese: getValue('cheeseId'),

            vegetable1: getValue('vegetable1Id'),
            vegetable2: getValue('vegetable2Id'),
            vegetable3: getValue('vegetable3Id'),
            vegetable4: getValue('vegetable4Id'),
            vegetable5: getValue('vegetable5Id'),
            vegetable6: getValue('vegetable6Id'),
            vegetable7: getValue('vegetable7Id'),
            vegetable8: getValue('vegetable8Id'),

            sauce1: getValue('sauce1Id'),
            sauce2: getValue('sauce2Id'),
            sauce3: getValue('sauce3Id'),

            status: getValue('status') === "active" ? "ACTIVE" : "DELETED",
            img: $("#imgUrl").val() || null
        };

        const jsonBlob = new Blob([JSON.stringify(menuData)], { type: "application/json" });
        formData.append("menu", jsonBlob);

        const menuName = menuData.menuName;

        $.ajax({
            url: "/menus/" + encodeURIComponent(menuName),
            type: "PUT",
            data: formData,
            processData: false,
            contentType: false,
            success: function () {
                alert("메뉴 정보가 성공적으로 수정되었습니다.");
                window.location.href = "/menus/list";
            },
            error: function (xhr) {
                console.error("수정 실패:", xhr.status, xhr.responseText);
                alert("수정 중 오류가 발생했습니다. \n" + xhr.responseText);
            }
        });
    });
});
