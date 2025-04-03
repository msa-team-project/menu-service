$(document).ready(()=>{

    $('#storeRegister').click((event)=>{

        event.preventDefault();//폼의 제출 기본 동작을 막음

        let  storeName=$('#store_name').val();
        let  address=$('#address').val();
        let  postcode=$('#postcode').val();
        let  status='ACTIVE';


        let formData={
            storeName : storeName,
            storeAddress: address,
            storePostcode: postcode,
            storeStatus: status

        }

        console.log(formData);

        $.ajax({
            type:'POST',
            url:'/stores',
            data: JSON.stringify(formData),//데이터를 JSON 형식으로 변환
            contentType: 'application/json; charset=utf-8',//전송 데이터의 타입
            dataType: 'json',//서버에서 받을 데이터의 타입
            success:(response)=>{
                alert('지점가입이 성공했습니다.');

                window.location.href='/store/storelist';

            },
            error:(error)=>{
                console.log('오류발생 :',error);
                alert('지점가입 중 오류가 발생했습니다')
            }
        })
    })

});