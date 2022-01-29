package com.example.expensetracker.service;

import com.example.expensetracker.dtos.TripDto;
import com.example.expensetracker.dtos.UserDto;
import com.example.expensetracker.exception.ResourceExistsException;
import com.example.expensetracker.model.Trip;
import com.example.expensetracker.model.User;
import com.example.expensetracker.repository.UserRepository;
import com.googlecode.jmapper.JMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto getUserById(Long userId) {
        JMapper<UserDto, User> userMapper = new JMapper<>(
                UserDto.class, User.class
        );

        var user = userRepository.findUserById(userId);
        return userMapper.getDestination(user.get());
    }

    public UserDto getUserByEmail(String userEmail) {
        JMapper<UserDto, User> userMapper = new JMapper<>(
                UserDto.class, User.class
        );

        var user = userRepository.findUserByEmail(userEmail);
        return userMapper.getDestination(user.get());
    }

    public List<UserDto> getAllUsers(){
        JMapper<UserDto, User> userMapper= new JMapper<>(
                UserDto.class, User.class);
        var users = userRepository.findAll();
        var usersDto = new ArrayList<UserDto>();
        users.forEach(u -> usersDto.add(userMapper.getDestination(u)));
        return usersDto;
    }

    public UserDto saveUser(UserDto userDto) {
        JMapper<User, UserDto> userMapper= new JMapper<>(
                User.class, UserDto.class);

        var user = userMapper.getDestination(userDto);

        var checkUser = userRepository.findUserByEmail(user.getEmail());
        if (checkUser.isPresent()) {
            throw new ResourceExistsException("User", "email", checkUser.get().getEmail());
        }

        var savedUser = userRepository.save(user);

        JMapper<UserDto, User> userMapperBack = new JMapper<>(
                UserDto.class, User.class);

        return userMapperBack.getDestination(savedUser);
    }
}
