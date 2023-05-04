window.onload = function () {
    findAllArticleCommentsUnderArticle();
}
document.getElementById('article-comment-create-button').addEventListener('click', e => {
    createNewArticleComment();
});

let page = 0;
let articleComments = [];
function findAllArticleCommentsUnderArticle() {
    callFindAllArticleCommentUnderArticleApi().then(response => {
        if(response.status === 200) {
            articleComments = response.data.responseArticleCommentFindDtos;
            drawArticleCommentListDom();
            drawArticleCommentPagination(response.data.paginationResponse);
        }
        if(response.status === 400) {
            alert(response.message);
        }
    });
}
function drawArticleCommentListDom() {
    let articleCommentArea = document.getElementById('articleCommentArea');
    articleCommentArea.innerHTML = "";

    articleComments.forEach(articleComment => {
        const newList = document.createElement('li');
        let html = `<div class="row my-3">
                        <div class="col-md-9 col-lg-9">
                            <b class="me-4">${articleComment.nickname}</b>
                            <time>${articleComment.lastModifiedAt}</time>
                            <div class="input-group mb-3">
                                <input type="text" class="form-control w-90 articleCommentContent" value="${articleComment.articleCommentContent}" disabled="disabled" id="articleCommentContent_${articleComment.articleCommentId}">`;

        if(articleComment.userId === signInUserId) {
            html += `
                                <button class="btn btn-secondary btn-sm" type="button" onclick="editMode(this, ${articleComment.articleCommentId})">수정모드</button>
                            </div>
                        </div>
                        
                        <div class="col-md-3 col-lg-3 text-center">
                            <br>
                            <button class="btn btn-warning btn-sm visually-hidden" type="button" id="editBtn_${articleComment.articleCommentId}" onclick="editArticleComment(${articleComment.articleCommentId})">수정</button>
                            <button class="btn btn-danger  btn-sm" type="button" onclick="removeArticleComment(${articleComment.articleCommentId})">삭제</button>
                        </div>`;
        }

        html += `</div>
                
                <button type="button" class="btn btn-primary btn-sm">▼ 댓글쓰기</button>
                
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
    if (pageNumber-1 === page) {
        return;
    }

    page = pageNumber-1;
    findAllArticleCommentsUnderArticle();
}
function createNewArticleComment() {
    let articleCommentContent = document.getElementById('article-comment-content').value;
    if(articleCommentContent.trim() === '' || articleCommentContent.length === 0) {
        alert("댓글을 작성해주세요.");
        return;
    }

    let createDto = {
        'content' : articleCommentContent
    }

    callCreateArticleCommentApi(createDto).then(response => {
        if (response.status === 200) {
            document.getElementById('article-comment-content').value = '';
            offset = 1;
            findAllArticleCommentsUnderArticle();
        }
        if(response.status === 401) {
            location.href = response.data;
        }
    });
}
function editMode(obj, articleCommentId) {
    let target = obj.previousElementSibling;
    let editBtn = document.getElementById(`editBtn_${articleCommentId}`);

    obj.innerText === '수정모드' ? obj.innerText = '수정취소' : obj.innerText = '수정모드';
    target.disabled ? target.disabled = false : articleCommentInputReset(target, articleCommentId);
    editBtn.classList.contains('visually-hidden') ? editBtn.className = 'btn btn-warning btn-sm' : editBtn.className += ' visually-hidden';
}
function articleCommentInputReset(target, articleCommentId) {
    target.value = findRawArticleCommentContent(articleCommentId);
    target.disabled = true;
}
function findRawArticleCommentContent(articleCommentId) {
    let filteredArticleComment = articleComments.filter(ac => ac.articleCommentId === articleCommentId);
    return filteredArticleComment[0].articleCommentContent;
}
function editArticleComment(articleCommentId) {
    let articleCommentContent = document.getElementById(`articleCommentContent_${articleCommentId}`).value;
    if(articleCommentContent.trim() === '' || articleCommentContent.length === 0){
        alert("수정할 댓글내용을 입력하세요.");
        return;
    }

    let editDto = {
        'content' : articleCommentContent
    }

    callEditArticleCommentApi(articleCommentId, editDto).then(response => {
        if (response.status === 200) {
            findAllArticleCommentsUnderArticle();
        }
        if(response.status === 400) {
            alert(response.message);
        }
        if(response.status === 401) {
            location.href = response.data;
        }
    });
}
function removeArticleComment(articleCommentId) {
    callRemoveArticleCommentApi(articleCommentId).then(response => {
        if (response.status === 200) {
            offset = 1;
            findAllArticleCommentsUnderArticle();
        }
        if(response.status === 400) {
            alert(response.message);
        }
        if(response.status === 401) {
            location.href = response.data;
        }
    });
}