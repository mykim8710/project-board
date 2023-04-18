document.getElementById('username').addEventListener('input', e => {
    duplicateCheckUserInfo(e.target.value, "username");
});
document.getElementById('nickname').addEventListener('input', e => {
    duplicateCheckUserInfo(e.target.value, "nickname");
});
document.getElementById('email').addEventListener('input', e => {
    duplicateCheckUserInfo(e.target.value, "email");
});

function duplicateCheckUserInfo(keyword, type) {
    if(keyword.trim() === "" || keyword.length === 0) {
        return;
    }

    callDuplicateCheckUserInfoApi(keyword, type).then(response => {
        let target = document.getElementById(`duplicate_error_${type}`);
        if(response.status === 200) {
            if(!target.classList.contains('visually-hidden')) {
                target.className += ' visually-hidden';
            }
        }
        if(response.status === 400) {
            target.innerText = response.message;
            target.classList.contains('visually-hidden') ? target.className = 'duplicate_error text-danger' : target.className += ' visually-hidden';
        }
    });
}

async function callDuplicateCheckUserInfoApi(keyword, type) {
    const res = await fetch(`/api/v1/users/duplicate-check?keyword=${keyword}&type=${type}`, {
        method: 'GET',
        headers: {
            'Content-type': 'application/json; charset=UTF-8',
        },
    }).catch(error => {
        console.log(error);
    });
    return await res.json();
}

function signUp(obj) {
    let targetUsername = document.getElementById('duplicate_error_username');
    let targetNickname = document.getElementById('duplicate_error_nickname');
    let targetEmail = document.getElementById('duplicate_error_email');

    if(!targetUsername.classList.contains('visually-hidden')
        || !targetNickname.classList.contains('visually-hidden')
            || !targetEmail.classList.contains('visually-hidden')) {
        alert("중복된 항목이 있습니다.");
        return;
    }

    obj.form.submit();
}



