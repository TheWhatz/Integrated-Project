package sit.int221.integratedprojectbe.services;


import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.integratedprojectbe.dtos.JwtTokenDTO;
import sit.int221.integratedprojectbe.dtos.LoginDTO;
import sit.int221.integratedprojectbe.dtos.UserDetailsDTO;
import sit.int221.integratedprojectbe.entities.User;
import sit.int221.integratedprojectbe.exceptions.ArgumentNotValidException;
import sit.int221.integratedprojectbe.repositories.UserRepository;
import sit.int221.integratedprojectbe.services.imp.UserDetailsServiceImp;
import sit.int221.integratedprojectbe.utils.JwtUtils;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsServiceImp userDetailsServiceImp;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private Argon2 argon2Factory;
    private AuthenticationManager authenticationManager;

    public JwtTokenDTO login(LoginDTO login , BindingResult bindingResult) {
        User user;
        if (bindingResult.hasErrors()) {
            throw new ArgumentNotValidException(bindingResult);
        }
        if(login.getEmail() != null && userRepository.existsByEmail(login.getEmail())){
            user = userRepository.findByEmail(login.getEmail().strip());
        }else  {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found.");
        }
        if(!argon2Factory.verify(user.getPassword(), login.getPassword()) )
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Password mismatch.");

        try {
            authenticate(user.getEmail(), user.getPassword());
        } catch (Exception ex) {
            System.out.println(ex);
        }
        UserDetails userDetails = userDetailsServiceImp.loadUserByUsername(user.getEmail());
        String accessToken = jwtUtils.generateToken(userDetails);
        String refreshToken = jwtUtils.refreshToken(accessToken);
        JwtTokenDTO jwtTokenDTO = new JwtTokenDTO("Login successful", accessToken,refreshToken);
        return jwtTokenDTO;
    }

    public UserDetailsDTO loadUserDetailByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("Email %s is doesn't exist.", email)
            );
        }
        return modelMapper.map(user, UserDetailsDTO.class);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
