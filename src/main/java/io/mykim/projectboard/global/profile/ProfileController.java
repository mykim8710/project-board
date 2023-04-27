package io.mykim.projectboard.global.profile;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProfileController {
    private final Environment environment;

    @GetMapping("/profile")
    public String profile() {
        List<String> profiles = Arrays.asList(environment.getActiveProfiles());

        /**
         * environment.getActiveProfiles()
         * 현재 실행 중인 ActiveProfile을 모두 가져온다.
         * => prod
         */

        List<String> realProfiles = Arrays.asList("prod", "prod1", "prod2");
        /**
         * 무중단배포에 prod1, prod2를 사용
         */

        String defaultProfile = profiles.isEmpty() ? "default" : profiles.get(0);

        return profiles.stream()
                .filter(profile -> realProfiles.contains(profile))
                .findAny()
                .orElse(defaultProfile);
    }

}
