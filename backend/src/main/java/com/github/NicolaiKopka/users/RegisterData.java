package com.github.NicolaiKopka.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterData {

    private String username;
    private String password;
    private String checkPassword;

}
