package sit.int221.integratedprojectbe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sit.int221.integratedprojectbe.entities.UserRole;
import sit.int221.integratedprojectbe.services.UserRoleService;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class UserRoleController {
    @Autowired
    private UserRoleService userRoleService;

    @GetMapping("")
    public List<UserRole> getAllRoles(){
        return userRoleService.getRoles();
    }

    @GetMapping("/{roleName}")
    private UserRole getRoleByRoleName(@PathVariable String roleName) {
        return userRoleService.getRoleByRoleName(roleName);
    }
}
