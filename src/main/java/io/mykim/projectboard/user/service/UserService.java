package io.mykim.projectboard.user.service;

import io.mykim.projectboard.global.result.enums.CustomErrorCode;
import io.mykim.projectboard.global.result.enums.CustomSuccessCode;
import io.mykim.projectboard.global.result.exception.NotValidRequestException;
import io.mykim.projectboard.global.result.model.CommonResponse;
import io.mykim.projectboard.user.dto.request.UserCreateDto;
import io.mykim.projectboard.user.dto.request.UserInfoDuplicateCheckDto;
import io.mykim.projectboard.user.entity.User;
import io.mykim.projectboard.global.result.exception.DuplicateUserInfoException;
import io.mykim.projectboard.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long createUser(UserCreateDto createDto) {
        // password 암호화(bCrypt)
        createDto.encodePassword(passwordEncoder);

        // dto -> entity
        User of = User.of(createDto);
        return userRepository.save(of).getId();
    }

    @Transactional(readOnly = true)
    public CommonResponse duplicateCheckUserInfo(UserInfoDuplicateCheckDto duplicateCheckDto) {
        // username, nickname, email
        CommonResponse response = new CommonResponse(CustomSuccessCode.COMMON_OK);

        switch (duplicateCheckDto.getType()) {
            case "username":
                Boolean existsByUsername = userRepository.existsByUsername(duplicateCheckDto.getKeyword());
                if(existsByUsername) {
                    throw new DuplicateUserInfoException(CustomErrorCode.DUPLICATE_USER_NAME);
                }
                break;
            case "nickname":
                Boolean existsByNickname = userRepository.existsByNickname(duplicateCheckDto.getKeyword());
                if(existsByNickname) {
                    throw new DuplicateUserInfoException(CustomErrorCode.DUPLICATE_USER_NICKNAME);
                }
                break;
//            case "email":
//                Boolean existsByEmail = userRepository.existsByEmail(duplicateCheckDto.getKeyword());
//                if(existsByEmail) {
//                    throw new DuplicateUserInfoException(CustomErrorCode.DUPLICATE_USER_EMAIL);
//                }
//                break;
            default:
                throw new NotValidRequestException(CustomErrorCode.NOT_VALID_REQUEST);
        }

        return response;
    }

}
