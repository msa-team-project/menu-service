$(document).ready(()=>{
    //  checkToken();
    //  setupAjax();
    // getStoreInfo().then((storeInfo)=>{
    //  $('#welcome-message').text(`${storeInfo.storeName}님 환영합니다!`);
    //    $('#hiddenStoreName').val(storeInfo.storeName);
    //  }).catch((error) => {
    //  console.error('store list store info error : ', error)
    // });
    getStores();

});

let getStores=()=>{
    let currnetPage=1;
    let pageSize=10;//한 페이지에 보여줄 게시글 수

    loadStores(currnetPage,pageSize);
    //다음 페이지 버튼 클릭 이벤트
    $('#nextPage').on('click',()=>{
        currnetPage++;
        loadStores(currnetPage,pageSize);
    });
    //이전 페이지 버튼 클릭 이벤트
    $('#prevPage').on("click",()=>{
        if(currnetPage>1){
            currnetPage--;
            loadStores(currnetPage,pageSize);
        }
    });
}

let loadStores=(page,size)=> {
    $.ajax({
        type: 'GET',
        url: '/storeList',
        data: {
            page: page,
            size: size
        },
        success: (response) => {
            console.log('loadStores : ', response);
            $('#storeContent').empty(); // 기존 게시글 내용 비우기
            if (response.articles.length <= 0) {
                // 게시글이 없는 경우 메시지 출력
                $('#storeContent').append(
                    `<tr>
                        <td colspan="4" style="text-align: center;">지점이 존재하지 않습니다.</td>
                    </tr>`
                );
            } else {
                response.articles.forEach((article) => {
                    $('#storeContent').append(
                        `
                            <tr>
                                <td>${article.uid}</td>
                                <td><a href="/detail?id=${article.uid}">${article.storeName}</a></td>
                                <td>${article.storeAddress}</td>
                                <td>${article.storeCreatedDate}</td>
                                <td>${article.storeStatus}</td>
                            </tr>
                    `
                    );
                });
            }

            // 페이지 정보 업데이트
            $('#pageInfo').text(page);
            // 이전/다음 버튼 상태 설정
            $('#prevPage').prop('disabled', page === 1);
            $('#nextPage').prop('disabled', response.last);
        },
        error: (error) => {
            console.error('board list error :: ', error);
        }
    })

}


let logout = () => {
    $.ajax({
        type: 'POST',
        url: '/logout',
        success: () => {
            alert('로그아웃이 성공했습니다.');
            localStorage.removeItem('accessToken');
            window.location.href = '/member/login'
        },
        error: (error) => {
            console.log('오류발생 : ', error);
            alert('로그아웃 중 오류가 발생했습니다.');
        }
    });
}