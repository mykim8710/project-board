package io.mykim.projectboard.article.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import io.mykim.projectboard.article.entity.ArticleComment;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.*;

@ToString
@Getter
public class ResponseArticleCommentFindDto {
    private Long articleCommentId;
    private String articleCommentContent;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy/MM/dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy/MM/dd HH:mm:ss")
    private LocalDateTime lastModifiedAt;
    private Long userId;
    private String nickname;
    private Long parentArticleCommentId;
    private Set<ResponseArticleCommentFindDto> childArticleComments = new LinkedHashSet<>();

    private ResponseArticleCommentFindDto(ArticleComment articleComment) {
        this.articleCommentId = articleComment.getId();
        this.articleCommentContent = articleComment.getContent();
        this.createdAt = articleComment.getCreatedAt();
        this.lastModifiedAt = articleComment.getLastModifiedAt();
        this.userId = articleComment.getCreatedBy().getId();
        this.nickname = articleComment.getCreatedBy().getNickname();
        this.parentArticleCommentId = articleComment.getParentArticleComment() == null ? null : articleComment.getParentArticleComment().getId();
    }

    public static ResponseArticleCommentFindDto of(ArticleComment articleComment) {
        return new ResponseArticleCommentFindDto(articleComment);
    }

    @QueryProjection
    public ResponseArticleCommentFindDto(Long articleCommentId,
                                         String articleCommentContent,
                                         LocalDateTime createdAt,
                                         LocalDateTime lastModifiedAt,
                                         Long userId,
                                         String nickname,
                                         Long parentArticleCommentId) {
        this.articleCommentId = articleCommentId;
        this.articleCommentContent = articleCommentContent;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.userId = userId;
        this.nickname = nickname;
        this.parentArticleCommentId = parentArticleCommentId;
    }

    public boolean hasParentArticleComment() {
        return parentArticleCommentId != null;
    }
}
