package com.accenture.codingtest.springbootcodingtest.service;

import com.accenture.codingtest.springbootcodingtest.entity.User;
import com.accenture.codingtest.springbootcodingtest.model.UserDto;
import com.accenture.codingtest.springbootcodingtest.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * This class contains all the business logic for users.
 * @author Parag Sansare
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream().map(userEntity -> modelMapper.map(userEntity, UserDto.class)).collect(Collectors.toList());
    }

    public UserDto findById(UUID id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        return modelMapper.map(optionalUser.get(),UserDto.class);
    }

    public void create(UserDto userDto) throws Exception {
        User userByUsername = userRepository.findByUsername(userDto.getUsername());
        if (userByUsername != null)
            throw new Exception("Username is taken");
        userRepository.save(modelMapper.map(userDto, User.class));
    }

    public void update(UUID id, UserDto userDto) throws Exception {
        User userByUsername = userRepository.findByUsername(userDto.getUsername());
        if (userByUsername != null)
            throw new Exception("Username is taken");
        User user =  new User();
        user.setId(id);
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        userRepository.save(user);
    }

    public void patch(UUID id, UserDto userDto) throws Exception {
        User user = userRepository.findById(id).orElseThrow();
        boolean needUpdate  = false;
        if (StringUtils.hasLength(userDto.getUsername())) {
            User userByUsername = userRepository.findByUsername(userDto.getUsername());
            if (userByUsername != null)
                throw new Exception("Username is taken");
            user.setUsername(userDto.getUsername());
            needUpdate  = true;
        }
        if (StringUtils.hasLength(userDto.getPassword())) {
            user.setPassword(userDto.getPassword());
            needUpdate  = true;
        }
        if (needUpdate )
            userRepository.save(user);
    }

    public void deleteById(UUID id) {
        userRepository.deleteById(id);
    }
}
