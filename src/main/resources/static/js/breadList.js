$(document).ready(function () {
    // checkToken();
    // setupAjax();
    // 빵 목록 불러오기
    function loadBreads() {
        $.ajax({
            url: "/breads",
            type: "GET",
            dataType: "json",
            success: function (data) {
                const breadTableBody = $("#breadTableBody");
                breadTableBody.empty();

                data.forEach(bread => {
                    const row = `<tr>
                        <td>${bread.uid}</td>
                        <td>${bread.breadName}</td>
                        <td>${bread.calorie}</td>
                        <td>${bread.price}</td>
                        <td><img src="${bread.img}" alt="빵 이미지" width="50"></td>
                        <td>${bread.status}</td>
                        <td>
                            <a href="/breads/edit/${encodeURIComponent(bread.breadName)}">수정</a>
                            <button class="delete-btn" data-breadname="${bread.breadName}">삭제</button>
                        </td>
                    </tr>`;
                    breadTableBody.append(row);
                });
            },
            error: function (xhr, status, error) {
                console.error("빵 목록을 불러오는 중 오류 발생:", error);
            }
        });
    }

    // 🔥 동적으로 생성된 삭제 버튼에도 이벤트가 적용되도록 수정!
    $(document).on("click", ".delete-btn", function () {
        let breadName = $(this).data("breadname"); // 소문자로 변경

        if (!breadName) {
            alert("삭제할 빵 이름이 없습니다.");
            return;
        }

        if (confirm("정말 삭제하시겠습니까?")) {
            $.ajax({
                url: "/breads/" + encodeURIComponent(breadName),
                type: "DELETE",
                success: function () {
                    alert("빵이 삭제되었습니다!");
                    loadBreads(); // 목록 새로고침
                },
                error: function () {
                    alert("삭제 중 오류가 발생했습니다.");
                }
            });
        }
    });

    // 페이지 로드 시 빵 목록 불러오기
    loadBreads();
});
