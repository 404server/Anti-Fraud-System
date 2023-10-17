package com.example.antifraudsystem.controller.utility;

import com.example.antifraudsystem.controller.dto.card.AddCardResult;
import com.example.antifraudsystem.controller.dto.card.Card;
import com.example.antifraudsystem.controller.dto.ip.AddIpRequest;
import com.example.antifraudsystem.controller.dto.ip.Ip;
import com.example.antifraudsystem.controller.dto.user.*;
import com.example.antifraudsystem.repository.domain.Role;
import com.example.antifraudsystem.service.dto.cardDto.CardDto;
import com.example.antifraudsystem.service.dto.ipDto.IpDto;
import com.example.antifraudsystem.service.dto.userDto.ChangeRoleDto;
import com.example.antifraudsystem.service.dto.userDto.UserDto;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public final class Converter {
    public static UserRegistrationResult convertUserResult(UserDto userDto) {
        return new UserRegistrationResult(userDto.getId(),
                userDto.getName(),
                userDto.getUsername(), userDto.getRole().name());
    }

    public static List<User> convertUserList(List<UserDto> userDtoList) {
        List<User> userList = new ArrayList<>();

        for (UserDto userDto : userDtoList) {
            userList.add(new User(userDto.getId(), userDto.getName(),
                    userDto.getUsername(), userDto.getRole().name()));
        }

        return userList;
    }

    public static UserDto convertUserRegistration(UserRegistration userRegistration) {
        return new UserDto()
                .setName(userRegistration.getName())
                .setUsername(userRegistration.getUsername())
                .setPassword(userRegistration.getPassword());
    }

    public static ChangeUserRoleResult convertUserChangeRole(UserDto userDto) {
        return new ChangeUserRoleResult()
                .setId(userDto.getId())
                .setName(userDto.getName())
                .setUsername(userDto.getUsername())
                .setRole(userDto.getRole().name());
    }

    public static ChangeRoleDto convertChangeRole(ChangeUserRole changeUserRole) {
        return new ChangeRoleDto()
                .setUsername(changeUserRole.getUsername())
                .setRole(Role.valueOf(changeUserRole.getRole()));
    }

    public static IpDto convertIpDto(AddIpRequest ip) {
        return new IpDto()
                .setIp(ip.getIp());
    }

    public static Ip convertIpResult(IpDto ipDto) {
        return new Ip(ipDto.getId(),
                ipDto.getIp());
    }

    public static List<Ip> convertIpList(List<IpDto> ipDtoList) {
        List<Ip> ipList = new ArrayList<>();

        for (IpDto ipDto : ipDtoList) {
            ipList.add(new Ip(ipDto.getId(), ipDto.getIp()));
        }

        return ipList;
    }

    public static List<Card> convertCardList(List<CardDto> cardDtoList) {
        List<Card> cardList = new ArrayList<>();

        for (CardDto cardDto : cardDtoList) {
            cardList.add(new Card(cardDto.getId(), cardDto.getNumber()));
        }
        return cardList;
    }

    public static AddCardResult convertCardAddResult(CardDto cardDto) {
        return new AddCardResult()
                .setId(cardDto.getId())
                .setNumber(cardDto.getNumber());
    }

    public static Timestamp convertStringToTimestamp(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
        return Timestamp.valueOf(dateTime);
    }
}

