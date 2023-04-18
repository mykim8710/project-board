package io.mykim.projectboard.user.api;

import io.mykim.projectboard.global.result.enums.CustomSuccessCode;
import io.mykim.projectboard.global.result.model.CommonResponse;
import io.mykim.projectboard.user.dto.request.UserCreateDto;
import io.mykim.projectboard.user.dto.request.UserInfoDuplicateCheckDto;
import io.mykim.projectboard.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserApiController {
    private final UserService userService;

    @GetMapping("/api/v1/users/duplicate-check")
    public ResponseEntity<CommonResponse> duplicateCheckUserInfoApi(@ModelAttribute UserInfoDuplicateCheckDto duplicateCheckDto) {
        log.info("[GET] /api/v1/users/duplicate-check?type={}&keyword={}  =>  duplicate Check UserInfo api", duplicateCheckDto.getType(), duplicateCheckDto.getKeyword());
        CommonResponse response = userService.duplicateCheckUserInfo(duplicateCheckDto);
        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @PostMapping("/api/v1/users")
    public ResponseEntity<CommonResponse> createUserApi(@RequestBody @Valid UserCreateDto createDto) {
        log.info("[POST] /api/v1/users  =>  User Create api, UserCreateDto = {}", createDto);
        CommonResponse response = new CommonResponse(CustomSuccessCode.INSERT_OK, userService.createUser(createDto));
        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }
}
