package com.github.NicolaiKopka.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserInfoDTO {

    private String username;
    private List<Integer> movieIds;

}
