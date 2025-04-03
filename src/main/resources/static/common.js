let getStoreInfo=()=>{
    return new Promise((resolve,reject)=>{
        $.ajax({
            type: 'GET',
            url: '/stores/info',
            success: (response) => {
                resolve(response);
            },
            error: (xhr) => {
                console.log('xhr'+xhr);
                reject(xhr);
            }

        })
    })
}
