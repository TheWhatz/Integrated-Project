package sit.int221.integratedprojectbe.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sit.int221.integratedprojectbe.dtos.*;
import sit.int221.integratedprojectbe.entities.User;
import sit.int221.integratedprojectbe.services.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("")
    public List<UserDetailsDTO> getAllUsers(){
        return userService.getUsers();

    }

    @GetMapping("/{userId}")
    public UserDetailsDTO getUserByUserId(@PathVariable Integer userId){
        return userService.getUserById(userId);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Integer userId) {
        userService.removeUser(userId);
    }
    @PatchMapping("/{userId}")
    public User update(@Valid @RequestBody EditUserDTO editUser,
                       BindingResult bindingResult, @PathVariable Integer userId) {
        return userService.editUser(userId, editUser, bindingResult);
    }
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDetailsDTO create(@Valid @RequestBody CreateUserDTO newUser, BindingResult bindingResult) {
        return userService.addNewUser(newUser, bindingResult);

    }
}
