$(document).ready(function () {
    $("#submitBtn").on("click", function () {
        let fileInput = $("#img")[0].files[0];

        if (!fileInput) {
            alert("이미지를 업로드해야 합니다.");
            return;
        }

        let imageFormData = new FormData();
        imageFormData.append("file", fileInput);

        // ✅ 1. 이미지 업로드 요청 (비동기)
        let imageUploadPromise = $.ajax({
            url: "/upload",
            type: "POST",
            data: imageFormData,
            processData: false,
            contentType: false
        });

        // ✅ 2. 상태 값을 변환 (active → ACTIVE, inactive → DELETED)
        let statusValue = $("#status").val();
        let convertedStatus = statusValue === "active" ? "ACTIVE" : "DELETED";

        // ✅ 3. 이미지 업로드 후 빵 정보 저장
        imageUploadPromise.then((imageResponse) => {
            if (!imageResponse.success) {
                alert("이미지 업로드에 실패했습니다.");
                return;
            }

            let breadData = {
                breadName: $("#breadName").val(),
                calorie: parseFloat($("#calorie").val()),
                price: parseInt($("#price").val(), 10),
                status: convertedStatus, // 변환된 상태 값 적용
                img: imageResponse.url // 업로드된 이미지 URL
            };

            // ✅ 4. 빵 정보 전송
            return $.ajax({
                url: "/breads",
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify(breadData)
            });
        }).then(() => {
            alert("빵 정보가 등록되었습니다!");
            window.location.href = "/breads/list"; // 목록 페이지로 이동
        }).catch(error => {
            console.error("Error:", error);
            alert("빵 정보 등록 중 오류가 발생했습니다.");
        });
    });
});
