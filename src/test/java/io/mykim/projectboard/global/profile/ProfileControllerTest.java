package io.mykim.projectboard.global.profile;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

class ProfileControllerTest {
    @Test
    void prod_Profile이_조회된다() throws Exception{
        // given
        String expectedProfile = "prod";
        MockEnvironment env = new MockEnvironment();
        env.addActiveProfile(expectedProfile);

        ProfileController profileController = new ProfileController(env);

        // when
        String profile = profileController.profile();
        System.out.println("profile = " + profile);

        // then
        Assertions.assertThat(profile).isEqualTo(expectedProfile);
    }

    @Test
    void prod_Profile이_없으면_첫번째가_조회된다() throws Exception{
        // given
        String expectedProfile = "oauth";
        MockEnvironment env = new MockEnvironment();
        env.addActiveProfile(expectedProfile);

        ProfileController profileController = new ProfileController(env);

        // when
        String profile = profileController.profile();
        System.out.println("profile = " + profile);

        // then
        Assertions.assertThat(profile).isEqualTo(expectedProfile);
    }
}