package io.mykim.projectboard.user.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserInfoDuplicateCheckDto {
    private String keyword;
    private String type;

    @Builder
    public UserInfoDuplicateCheckDto(String keyword, String type) {
        this.keyword = keyword;
        this.type = type;
    }
}
