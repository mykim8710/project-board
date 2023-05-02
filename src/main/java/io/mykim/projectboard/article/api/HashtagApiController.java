package io.mykim.projectboard.article.api;

import io.mykim.projectboard.article.service.HashtagService;
import io.mykim.projectboard.global.result.enums.CustomSuccessCode;
import io.mykim.projectboard.global.result.model.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RestController
public class HashtagApiController {
    private final HashtagService hashtagService;

    @GetMapping("/api/v1/hashtags")
    public CommonResponse findAllHashtags(@PageableDefault(page = 0, size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("[GET] /api/v1/hashtags?page={} => find all hashtags", pageable.getOffset());
        return new CommonResponse(CustomSuccessCode.COMMON_OK, hashtagService.findAllHashtags(pageable));
    }

}
