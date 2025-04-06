$(document).ready(()=>{

    initStoreList();

});

let cursorStack=[];//이전 커서들을 저장
let lastCursor= null; //현재 커서
let cursorMap=new Map();//페이지번호=>커서 매핑
let page= 1;
let limit=10

let initStoreList=() =>{

    loadStores({ limit, lastUid: lastCursor});

    $('#nextPage').click((event)=>{
        if ($('#nextPage').prop('disabled')) return; // 비활성화일 경우 무시
        if(lastCursor!==null) {
            cursorStack.push(lastCursor);//현재 커서 저장
        }
        const currentCursor=lastCursor; //요청용 커서 저장
        page++;
        loadStores({ limit, lastUid: currentCursor});
    });

    $('#prevPage').click((event)=>{
        if(cursorStack.length===0) return;
        lastCursor = cursorStack.pop();//이전 커서로 복귀
        console.log("이전 페이지 커서 : ",lastCursor);
        const currentCursor=lastCursor;
        page--;
        loadStores({ limit, lastUid: currentCursor});
    });

    $('#firstPage').click((event)=>{
        cursorStack=[];
        lastCursor=null;
        page=1;
        loadStores({ limit, lastUid:null });
    });
}

let loadStores=({ limit, lastUid})=> {

    let url = `/stores/list?limit=${limit}`;
    if (lastUid !== null && lastUid !== undefined) {
        url += `&lastUid=${lastUid}`;
    }

     $.ajax({
        type: 'GET',
        url: url,

        success: (response) => {
            console.log('loadStores : ', response);

            $('#storeContent').empty(); // 기존 지점 목록 비우기

            if (response.storeList.length === 0) {
                // 지점 목록이 없는 경우 메시지 출력
                $('#storeContent').append(
                    `<tr>
                        <td colspan="4" style="text-align: center;">지점이 존재하지 않습니다.</td>
                    </tr>`
                );
            } else {
                response.storeList.forEach((store) => {
                    //날짜 재표기
                    const dateObj = new Date(store.storeCreatedDate);
                    const year = dateObj.getFullYear();
                    const month = String(dateObj.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1
                    const day = String(dateObj.getDate()).padStart(2, '0');
                    const formattedDate = `${year}년 ${month}월 ${day}일`;

                    $('#storeContent').append(
                        `
                            <tr>
                                <td>${store.uid}</td>
                                <td><a href="/detail?id=${store.uid}">${store.storeName}</a></td>
                                <td>${store.storeAddress}</td>
                                 <td>${store.storePostcode}</td>
                                <td>${formattedDate}</td>
                                <td>${store.storeStatus}</td>
                            </tr>
                    `
                    );
                });

                //페이지 번호 초기화 조건
                if(page===1) {
                    cursorMap.clear();
                    cursorMap.set(1, null); // 첫 페이지는 null 커서
                }

                //다음 페이지 커서 저장(단,다음 페이지가 있을 때만)
                if(response.nextCursor && response.storeList.length === limit){
                    cursorMap.set(page+1,response.nextCursor);
                }
                //커서 갱신
               lastCursor = response.nextCursor; //다음 페이지 요청


            }
            // 페이지 번호 및 버튼 상태 업데이트
            updatePaginationButtons(response);


        },
        error: (error) => {
            console.error('지점 목록 조회 오류 :: ', error);
        }
    })

}

let updatePaginationButtons = (response)=> {

    $('#pageInfo').text(page);
    // 이전/다음 버튼 상태 설정
    $('#prevPage').prop('disabled', cursorStack.length === 0);
    $('#nextPage').prop('disabled', !response.nextCursor || response.storeList.length < limit);

    const $pageNumbers= $('#pageNumbers');
    $pageNumbers.empty();

    for(let i=1;i<cursorMap.size+1;i++){
        const $btn=$(`<button class="btn page-btn" data-page="${i}">${i}</button>`);
        if(i===page){
            $btn.css('background-color','#999');
        }
        $pageNumbers.append($btn);
    }

    //클릭 이벤트
    $('.page-btn').off('click').on('click', function(){
        const targetPage=parseInt(($(this).data("page")));
        if(targetPage===page) return;

        const targetCursor=targetPage===1?null:cursorMap.get(targetPage-1);
        page=targetPage;
        lastCursor=targetCursor;

        //커서 스택 재구성
        cursorStack=[];
        for(let i=1;i<targetPage;i++){
            const c=cursorMap.get(i);
            if(c)cursorStack.push(c);
        }

        loadStores({ limit, lastUid: lastCursor});
    });


};


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