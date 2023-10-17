package com.example.antifraudsystem.controller;

import com.example.antifraudsystem.controller.dto.user.*;
import com.example.antifraudsystem.service.UserService;
import com.example.antifraudsystem.service.dto.userDto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.antifraudsystem.controller.utility.Converter.*;

@Controller
@RequestMapping("/api/auth")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/user")
    public ResponseEntity<UserRegistrationResult> registerUser(@RequestBody UserRegistration user) {
        if (user.getName() == null || user.getUsername() == null || user.getPassword() == null) {
            return ResponseEntity.badRequest().build();
        }

        UserDto userDto = convertUserRegistration(user);
        UserRegistrationResultDto userRegistrationResultDto = userService.registerUser(userDto);
        if (!userRegistrationResultDto.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return new ResponseEntity<>(convertUserResult(userRegistrationResultDto.getUserDto()), HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<List<User>> getUsersList() {
        return new ResponseEntity<>(convertUserList(userService.getUserList()), HttpStatus.OK);
    }

    @DeleteMapping("/user/{username}")
    public ResponseEntity<DeleteUserResult> removeUser(@PathVariable String username) {
        if (username == null) {
            return ResponseEntity.badRequest().build();
        }
        DeleteUserResultDto deleteResultDto = userService.deleteUser(username);
        if (!deleteResultDto.isSuccess()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return new ResponseEntity<>(new DeleteUserResult(username, "Deleted successfully!"), HttpStatus.OK);
    }

    @PutMapping("/role")
    public ResponseEntity<ChangeUserRoleResult> changeRole(@RequestBody ChangeUserRole changeRole) {
        if (changeRole.getUsername() == null || changeRole.getRole() == null ||
                !changeRole.getRole().equals("SUPPORT") && !changeRole.getRole().equals("MERCHANT")) {
            return ResponseEntity.badRequest().build();
        }

        ChangeRoleDto changeRoleDto = convertChangeRole(changeRole);
        ChangeUserRoleDto changeUserRoleDto = userService.changeUserRole(changeRoleDto);

        if (changeUserRoleDto.getResult().equals(ChangeRoleResult.NOT_FOUND)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (changeUserRoleDto.getResult().equals(ChangeRoleResult.CONFLICT)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return new ResponseEntity<>(
                convertUserChangeRole(changeUserRoleDto.getUserDto()), HttpStatus.OK);
    }

    @PutMapping("/access")
    public ResponseEntity<ChangeAccountStateResult> changeAccountState(@RequestBody ChangeAccountState changeAccountState) {
        if (changeAccountState.getUsername() == null || changeAccountState.getOperation() == null) {
            return ResponseEntity.badRequest().build();
        }
        ChangeAccountStateDto accountStateDto = userService.changeAccountState(changeAccountState);
        if (accountStateDto.getStatus().equals(ChangeStateResult.BAD_REQUEST)) {
            ResponseEntity.badRequest().build();
        }
        if (accountStateDto.getStatus().equals(ChangeStateResult.NOT_FOUND)) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        String status = String.format("User %s %sed!", changeAccountState.getUsername(),
                changeAccountState.getOperation().toLowerCase());

        return new ResponseEntity<>(new ChangeAccountStateResult(status), HttpStatus.OK);
    }

}
