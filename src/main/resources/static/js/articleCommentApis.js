async function callCreateArticleCommentApi(createDto) {
    const res = await fetch(`/api/v1/articles/${articleId}/article-comments`, {
        method: 'POST',
        headers: {
            'Content-type': 'application/json; charset=UTF-8',
        },
        body: JSON.stringify(createDto)
    }).catch(error => {
        console.log(error);
    });
    return await res.json();
}

async function callFindAllArticleCommentUnderArticleApi() {
    const res = await fetch(`/api/v1/articles/${articleId}/article-comments?page=${page}`, {
        method: 'GET',
        headers: {
            'Content-type': 'application/json; charset=UTF-8',
        },
    }).catch(error => {
        console.log(error);
    });
    return await res.json();
}

async function callRemoveArticleCommentApi(articleCommentId) {
    const res = await fetch(`/api/v1/articles/${articleId}/article-comments/${articleCommentId}`, {
        method: 'DELETE',
        headers: {
            'Content-type': 'application/json; charset=UTF-8',
        },
    }).catch(error => {
        console.log(error);
    });
    return await res.json();
}

async function callEditArticleCommentApi(articleCommentId, editDto) {
    const res = await fetch(`/api/v1/articles/${articleId}/article-comments/${articleCommentId}`, {
        method: 'PATCH',
        headers: {
            'Content-type': 'application/json; charset=UTF-8',
        },
        body: JSON.stringify(editDto)
    }).catch(error => {
        console.log(error);
    });
    return await res.json();
}