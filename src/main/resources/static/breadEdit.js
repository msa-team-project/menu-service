$(document).ready(function () {
    $("#updateBtn").on("click", function () {
        let breadName = $("#breadName").val();
        let calorie = parseFloat($("#calorie").val());
        let price = parseInt($("#price").val(), 10);
        let statusValue = $("#status").val();
        let convertedStatus = statusValue === "active" ? "ACTIVE" : "DELETED";
        let currentImageUrl = $("#imgUrl").val();
        let fileInput = $("#img")[0].files[0];

        // ✅ 이미지 업로드가 필요한 경우
        if (fileInput) {
            let imageFormData = new FormData();
            imageFormData.append("file", fileInput);

            $.ajax({
                url: "/upload",
                type: "POST",
                data: imageFormData,
                processData: false,
                contentType: false
            }).then((imageResponse) => {
                if (!imageResponse.success) {
                    alert("이미지 업로드에 실패했습니다.");
                    return;
                }
                updateBread(breadName, calorie, price, imageResponse.url, convertedStatus);
            }).catch(() => {
                alert("이미지 업로드 중 오류가 발생했습니다.");
            });
        } else {
            // ✅ 새 이미지 없을 경우 기존 이미지 유지
            updateBread(breadName, calorie, price, currentImageUrl, convertedStatus);
        }
    });

    function updateBread(breadName, calorie, price, img, status) {
        let breadData = {
            breadName: breadName,
            calorie: calorie,
            price: price,
            img: img,
            status: status
        };

        $.ajax({
            url: "/breads/" + breadName,
            type: "PUT",
            contentType: "application/json",
            data: JSON.stringify(breadData)
        }).then(() => {
            alert("빵 정보가 수정되었습니다!");
            window.location.href = "/breads/list";
        }).catch(() => {
            alert("빵 정보 수정 중 오류가 발생했습니다.");
        });
    }
});
