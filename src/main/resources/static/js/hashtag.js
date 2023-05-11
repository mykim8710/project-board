window.onload = function () {
    findAllHashtags();
}

let page = 0;

function findAllHashtags() {
    callFindAllHashtagApi().then(response => {
        console.log(response);

        drawHashtags(response.data.hashtags);
        drawFindMoreHashtagButton(response.data.hasNextPage, response.data.page);
    });
}

function drawHashtags(hashtags) {
    let hashtagArea = document.getElementById('hashtags');
    hashtags.forEach(hashtag => {
        const newHashtag = document.createElement('div');
        newHashtag.className = "p-2 text-primary";

        let html = `<h2 class="text-center lh-lg font-monospace">
                        <a style="cursor:pointer;text-decoration: underline !important;" href="javascript:void(0);" onclick="findArticlesByHashtag(this)">#${hashtag}</a>
                    </h2>`;

        newHashtag.innerHTML = html;
        hashtagArea.appendChild(newHashtag);
    });
}

function drawFindMoreHashtagButton(hasNextPage, currentPage) {
    let hashtagArea = document.getElementById('findMoreHashtagButtonArea');
    hashtagArea.innerHTML = "";

    if (!hasNextPage) {
        return;
    }

    let html = `<div class="p-2">
                    <button class="btn btn-primary btn-sm" style="padding: 8px;font-size: 12px;" onclick="findMoreHashtag(${currentPage})">more</button>
                </div>`;
    hashtagArea.innerHTML = html;
}

function findMoreHashtag(currentPage) {
    page = Number(currentPage) +1;
    findAllHashtags();
}

function findArticlesByHashtag(obj) {
    const searchParams = new URLSearchParams(location.search);
    let pageParam = searchParams.get('page') == null ? '' :searchParams.get('page');
    let sizeParam = searchParams.get('size') == null ? '' :searchParams.get('size');
    let sortParam = searchParams.get('sort') == null ? '' :searchParams.get('sort');
    let searchKeyword= obj.innerText.replace("#", "").trim();
    location.href=`/articles/hashtag?searchType=${hashtagSearchType}&searchKeyword=${searchKeyword}&page=${pageParam}&size=${sizeParam}&sort=${sortParam}`;
}

async function callFindAllHashtagApi() {
    const res = await fetch(`/api/v1/hashtags?page=${page}`, {
        method: 'GET',
        headers: {
            'Content-type': 'application/json; charset=UTF-8',
        },
    }).catch(error => {
        console.log(error);
    });
    return await res.json();
}
