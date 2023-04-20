window.onload = function () {
    getArticleComments();
}

document.getElementById('article-comment-create-button').addEventListener('click', e => {
    createNewArticleComment();
});

function createNewArticleComment() {
    let articleId = document.getElementById('articleId').value;
    let articleCommentContent = document.getElementById('article-comment-content').value;
    if(articleCommentContent.trim() === '' || articleCommentContent.length === 0) {
        alert("댓글을 작성해주세요.");
        return;
    }

    let createDto = {
        'content' : articleCommentContent
    }

    callCreateArticleCommentApi(articleId, createDto).then(response => {
        if (response.status === 200) {
            document.getElementById('article-comment-content').value = '';
            offset = 1;
            getArticleComments();
        }
    });
}

async function callCreateArticleCommentApi(articleId, createDto) {
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

let offset = 1;
const limit = 5;

function getArticleComments() {
    let articleId = document.getElementById('articleId').value;
    callFindAllArticleCommentUnderArticleApi(articleId, offset).then(response => {
        if(response.status === 200) {
            drawArticleCommentListDom(articleId, response.data.responseArticleCommentFindDtos);

            // draw pagination
            drawArticleCommentPagination(response.data.paginationResponse);
        }
        if(response.status === 400) {
            alert(response.message);
        }
    });
}

async function callFindAllArticleCommentUnderArticleApi(articleId, offset) {
    const res = await fetch(`/api/v1/articles/${articleId}/article-comments?offset=${offset}&limit=${limit}`, {
        method: 'GET',
        headers: {
            'Content-type': 'application/json; charset=UTF-8',
        },
    }).catch(error => {
        console.log(error);
    });
    return await res.json();
}

function drawArticleCommentListDom(articleId, articleComments) {
    let articleCommentArea = document.getElementById('articleCommentArea');
    articleCommentArea.innerHTML = "";

    articleComments.forEach(articleComment => {
        const newList = document.createElement('li');

        let html = `<div class="row my-3">
                        <div class="col-md-9 col-lg-9">
                            <b class="me-4">${articleComment.nickname}</b>
                            <time>${articleComment.lastModifiedAt}</time>
                            <div class="input-group mb-3">
                                <input type="text" class="form-control w-90 articleCommentContent" value="${articleComment.articleCommentContent}" disabled="disabled" id="articleCommentContent_${articleComment.articleCommentId}">
                                <button class="btn btn-secondary btn-sm" type="button" onclick="editMode(this, ${articleComment.articleCommentId})">수정모드</button>
                            </div>
                        </div>
                        <div class="col-md-3 col-lg-3 text-center">
                            <br>
                            <button class="btn btn-warning btn-sm visually-hidden" type="button" id="editBtn_${articleComment.articleCommentId}" onclick="editArticleComment(${articleId}, ${articleComment.articleCommentId})">수정</button>
                            <button class="btn btn-danger  btn-sm" type="button" onclick="removeArticleComment(${articleId}, ${articleComment.articleCommentId})">삭제</button>
                        </div>
                    </div>
                    <hr>`;

        newList.innerHTML = html;
        articleCommentArea.appendChild(newList);
    });
}

function drawArticleCommentPagination(paginationResponse) {
    let articleCommentPaginationArea = document.getElementById('articleCommentPaginationArea');
    articleCommentPaginationArea.innerHTML = "";

    let html = `<ul class="pagination">
                    <li class="page-item">
                        <a class="page-link" onclick="movePage(${paginationResponse.prevPage})" aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span>
                            <span class="sr-only">이전글</span>
                        </a>
                    </li>

                    <li class="page-item">
                        <a class="page-link" onclick="movePage(${paginationResponse.nextPage})" aria-label="Next">
                            <span class="sr-only">다음글</span>
                            <span aria-hidden="true">&raquo;</span>
                        </a>
                    </li>
                </ul>`;

    articleCommentPaginationArea.innerHTML = html;
}

function movePage(pageNumber) {
    if (pageNumber === offset) {
        return;
    }

    offset = pageNumber;
    getArticleComments();
}

function removeArticleComment(articleId, articleCommentId) {
    callRemoveArticleCommentApi(articleId, articleCommentId).then(response => {
        if (response.status === 200) {
            offset = 1;
            getArticleComments();
        }
        if(response.status === 400) {
            alert(response.message);
        }
    });
}

async function callRemoveArticleCommentApi(articleId, articleCommentId) {
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

function editMode(obj, articleCommentId) {
    let target = obj.previousElementSibling;
    obj.innerText === '수정모드' ? obj.innerText = '수정취소' : obj.innerText = '수정모드';
    target.disabled ? target.disabled = false : target.disabled = true;

    let editBtn = document.getElementById(`editBtn_${articleCommentId}`);
    editBtn.classList.contains('visually-hidden') ? editBtn.className = 'btn btn-warning btn-sm' : editBtn.className += ' visually-hidden';
}

function editArticleComment(articleId, articleCommentId) {
    let articleCommentContent = document.getElementById(`articleCommentContent_${articleCommentId}`).value;
    if(articleCommentContent.trim() === '' || articleCommentContent.length === 0){
        alert("수정할 댓글내용을 입력하세요.");
        return;
    }

    let editDto = {
        'content' : articleCommentContent
    }

    callEditArticleCommentApi(articleId, articleCommentId, editDto).then(response => {
        if (response.status === 200) {
            getArticleComments();
        }
        if(response.status === 400) {
            alert(response.message);
        }
    });
}

async function callEditArticleCommentApi(articleId, articleCommentId, editDto) {
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