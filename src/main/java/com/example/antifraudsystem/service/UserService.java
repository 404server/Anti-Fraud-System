package com.example.antifraudsystem.service;

import com.example.antifraudsystem.configuration.AppUserAdapter;
import com.example.antifraudsystem.controller.dto.user.ChangeAccountState;
import com.example.antifraudsystem.repository.UserRepository;
import com.example.antifraudsystem.repository.domain.Role;
import com.example.antifraudsystem.repository.domain.UserModel;
import com.example.antifraudsystem.service.dto.userDto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserRegistrationResultDto registerUser(UserDto user) {
        if (repository.existsByUsername(user.getUsername())) {
            return new UserRegistrationResultDto(false);
        }

        if (repository.count() == 0) {
            repository.save(new UserModel(user.getName(), user.getUsername(), passwordEncoder.encode(user.getPassword()), Role.ADMINISTRATOR, true));

            UserModel userModel = repository.findUserByUsername(user.getUsername());

            UserDto userDto = new UserDto().setId(userModel.getId()).setName(userModel.getName()).setUsername(userModel.getUsername()).setPassword(userModel.getPassword()).setRole(userModel.getRole());

            return new UserRegistrationResultDto(userDto, true);
        }
        repository.save(new UserModel(user.getName(), user.getUsername(), passwordEncoder.encode(user.getPassword()), Role.MERCHANT, false));

        UserModel userModel = repository.findUserByUsername(user.getUsername());

        UserDto userDto = new UserDto().setId(userModel.getId()).setName(userModel.getName()).setUsername(userModel.getUsername()).setPassword(userModel.getPassword()).setRole(userModel.getRole());

        return new UserRegistrationResultDto(userDto, true);
    }

    public List<UserDto> getUserList() {
        List<UserDto> userDtoList = new ArrayList<>();
        for (UserModel userModel : repository.findByOrderByIdAsc()) {
            userDtoList.add(new UserDto().setId(userModel.getId()).setName(userModel.getName()).setUsername(userModel.getUsername()).setPassword(userModel.getPassword()).setRole(userModel.getRole()));
        }
        return userDtoList;
    }

    public DeleteUserResultDto deleteUser(String username) {
        if (!repository.existsByUsername(username)) {
            return new DeleteUserResultDto(false);
        }
        repository.deleteByUsername(username);
        return new DeleteUserResultDto(true);
    }

    public ChangeUserRoleDto changeUserRole(ChangeRoleDto changeRoleDto) {
        if (!repository.existsByUsername(changeRoleDto.getUsername())) {
            return new ChangeUserRoleDto(ChangeRoleResult.NOT_FOUND);
        }
        UserModel userModel = repository.findUserByUsername(changeRoleDto.getUsername());

        if (userModel.getRole().equals(changeRoleDto.getRole())) {
            return new ChangeUserRoleDto(ChangeRoleResult.CONFLICT);
        }
        userModel.setRole(changeRoleDto.getRole());
        repository.save(userModel);
        UserDto userDto = new UserDto()
                .setId(userModel.getId())
                .setName(userModel.getName())
                .setUsername(userModel.getUsername())
                .setPassword(userModel.getPassword())
                .setRole(userModel.getRole());
        return new ChangeUserRoleDto(userDto, ChangeRoleResult.SUCCESS);
    }

    public ChangeAccountStateDto changeAccountState(ChangeAccountState changeAccountState) {
        if (!repository.existsByUsername(changeAccountState.getUsername())) {
            return new ChangeAccountStateDto(ChangeStateResult.NOT_FOUND);
        }
        UserModel userModel = repository.findUserByUsername(changeAccountState.getUsername());
        if (userModel.getRole().equals(Role.ADMINISTRATOR)) {
            return new ChangeAccountStateDto(ChangeStateResult.BAD_REQUEST);
        }

        boolean operation = "LOCK".equals(changeAccountState.getOperation());
        userModel.setAccountNonLocked(!operation);
        repository.save(userModel);

        return new ChangeAccountStateDto(ChangeStateResult.SUCCESS);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel userModel = repository.findAppUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Not Found"));

        return new AppUserAdapter(userModel);
    }
}
