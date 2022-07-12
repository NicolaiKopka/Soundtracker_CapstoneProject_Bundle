package com.github.NicolaiKopka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterUserDTO {

    private String username;

}
