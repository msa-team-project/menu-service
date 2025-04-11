$(document).ready(function () {
    // checkToken();
    // setupAjax();

    $("#updateBtn").on("click", function () {
        let fileInput = $("#img")[0].files[0];

        let sideData = {
            sideName: $("#sideName").val(),
            calorie: parseFloat($("#calorie").val()),
            price: parseInt($("#price").val(), 10),
            status: $("#status").val() === "active" ? "ACTIVE" : "DELETED",
            img: $("#imgUrl").val() // 기존 이미지 URL 유지
        };

        let formData = new FormData();
        let jsonBlob = new Blob([JSON.stringify(sideData)], { type: "application/json" });
        formData.append("side", jsonBlob);

        if (fileInput) {
            formData.append("file", fileInput); // 새 이미지가 있을 경우 추가
        }

        $.ajax({
            url: "/sides/" + sideData.sideName,
            type: "PUT",
            data: formData,
            enctype: "multipart/form-data",
            processData: false,
            contentType: false,
            success: function () {
                alert("사이드 정보가 수정되었습니다!");
                window.location.href = "/sides/list";
            },
            error: function (xhr) {
                console.error("Error:", xhr.responseText);
                alert("사이드 정보 수정 중 오류가 발생했습니다.");
            }
        });
    });
});
