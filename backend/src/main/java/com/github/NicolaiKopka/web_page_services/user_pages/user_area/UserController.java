package com.github.NicolaiKopka.web_page_services.user_pages.user_area;


import com.github.NicolaiKopka.dto.UserInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/soundtracker/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping()
    public UserInfoDTO getUserInfo(Principal principal) {
        return userService.getUserInfo(principal.getName());
    }
}
