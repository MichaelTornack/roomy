package com.nerdware.roomy.features.users.services;

import com.nerdware.roomy.domain.entities.User;
import com.nerdware.roomy.domain.entities.UserRole;
import com.nerdware.roomy.domain.exceptions.BadRequestException;
import com.nerdware.roomy.domain.exceptions.ResourceNotFoundException;
import com.nerdware.roomy.domain.exceptions.UnauthorizedException;
import com.nerdware.roomy.features.users.dtos.requests.CreateUserDto;
import com.nerdware.roomy.features.users.dtos.requests.UpdateUserDto;
import com.nerdware.roomy.features.users.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService
{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(CreateUserDto request, User caller) throws Exception
    {

        if(caller.getRole() != UserRole.Admin)
            throw new UnauthorizedException("You do not have permission to add user");

        var existingUser = userRepository.findByEmail(request.email());
        if (existingUser.isPresent())
            throw new BadRequestException("Email already exists");

        var user = new User();
        user.setFullName(request.name());
        user.setEmail(request.email());
        user.setRole(request.role());
        user.setPassword(passwordEncoder.encode(request.password()));

        return userRepository.save(user);
    }

    public User getUserById(Integer id) throws ResourceNotFoundException
    {
        var user = userRepository.findById(id);
        if(!user.isPresent()) throw new ResourceNotFoundException("User not found");
        return user.get();
    }

    public User updateUser(Integer id, UpdateUserDto request, User caller) throws Exception
    {
        var userResult = userRepository.findById(id);
        if(!userResult.isPresent())
            throw new ResourceNotFoundException("User not found");

        var user = userResult.get();

        if(caller.getId() != user.getId() && caller.getRole() != UserRole.Admin)
            throw new UnauthorizedException("You do not have permission to update user");

        if(request.email() != null) {
            var otherUser = userRepository.findByEmail(request.email());
            if(otherUser.isPresent())
                throw new BadRequestException("Email is already taken");

            user.setEmail(request.email());
        }

        if(request.name() != null)
            user.setFullName(request.name());

        if(request.password() != null)
            user.setPassword(request.password());

        if(request.role() != null) {
            if(caller.getRole() != UserRole.Admin)
                throw new UnauthorizedException("Only admins can change roles");
            user.setRole(request.role());
        }

        return userRepository.save(user);
    }

    public void deleteUser(Integer id, User caller) throws Exception
    {
        if(caller.getRole() != UserRole.Admin)
            throw new UnauthorizedException("You do not have permission to delete a user");

        var userResult = userRepository.findById(id);
        if(!userResult.isPresent())
            throw new ResourceNotFoundException("User not found");

        userRepository.deleteById(id);
    }
}
