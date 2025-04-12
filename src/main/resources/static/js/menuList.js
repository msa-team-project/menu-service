$(document).ready(function () {
    // checkToken();
    // setupAjax();

    function loadMenus() {
        $.ajax({
            url: "/menus",
            type: "GET",
            dataType: "json",
            success: function (data) {
                renderMenuList(data);
            },
            error: function (xhr, status, error) {
                console.error(`메뉴 목록 불러오기 실패 [${status}]:`, error);
                alert("메뉴 목록을 불러오는 중 오류가 발생했습니다.");
            }
        });
    }

    function renderMenuList(menus) {
        const menuTableBody = $("#menuTableBody");
        menuTableBody.empty();

        menus.forEach(menu => {
            const imgTag = menu.img
                ? `<img src="${menu.img}" alt="메뉴 이미지" width="50">`
                : `<span>이미지 없음</span>`;

            const rowHtml = `
                <tr>
                    <td>${menu.uid}</td>
                    <td>${menu.menuName}</td>
                    <td>${menu.price}</td>
                    <td>${menu.calorie}</td>
                    <td>${imgTag}</td>
                    <td>${menu.status}</td>
                    <td>
                        <a href="/menus/edit/${encodeURIComponent(menu.menuName)}">수정</a>
                        <button class="delete-btn" data-menuname="${menu.menuName}">삭제</button>
                    </td>
                </tr>
            `;
            menuTableBody.append(rowHtml);
        });
    }

    function deleteMenu(menuName) {
        if (!menuName) {
            alert("삭제할 메뉴 이름이 없습니다.");
            return;
        }

        if (!confirm(`'${menuName}' 메뉴를 삭제하시겠습니까?`)) return;

        $.ajax({
            url: "/menus/" + encodeURIComponent(menuName),
            type: "DELETE",
            success: function () {
                alert("메뉴가 삭제되었습니다!");
                loadMenus();
            },
            error: function (xhr, status, error) {
                console.error(`삭제 실패 [${status}]:`, error);
                alert("삭제 중 오류가 발생했습니다.");
            }
        });
    }

    // 삭제 버튼 이벤트 등록
    $(document).on("click", ".delete-btn", function () {
        const menuName = $(this).data("menuname");
        deleteMenu(menuName);
    });

    // 초기화

});
