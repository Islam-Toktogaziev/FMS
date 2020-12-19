package KG.Neobis.FMS.Controllers;

import KG.Neobis.FMS.Config.JwtOutils;
import KG.Neobis.FMS.Entities.Users.AppUsers;
import KG.Neobis.FMS.Enums.ResultCode;
import KG.Neobis.FMS.Exceptions.ResourceNotFoundExceptions;
import KG.Neobis.FMS.Services.UserService;
import KG.Neobis.FMS.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PUT,RequestMethod.OPTIONS})
@RestController
@Api("APIs for add/get users")
public class UserController {

    private final BCryptPasswordEncoder myBCryptPasswordEncoder;
    private final JwtOutils jwtOutils;
    private final UserService userService;


    public UserController(BCryptPasswordEncoder myBCryptPasswordEncoder, JwtOutils jwtOutils, UserService userService) {
        this.myBCryptPasswordEncoder = myBCryptPasswordEncoder;
        this.jwtOutils = jwtOutils;
        this.userService = userService;
    }

    @PostMapping("/login")
    @ApiOperation("API for authentication")
    public AuthResponse signIn(@RequestBody AuthRequest authRequest) {
        AppUsers user = userService.getByEmail(authRequest.getEmail());
        if (user != null && myBCryptPasswordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            return new AuthResponse(jwtOutils.create(user));
        }
        throw new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION,"Не найден пользователь с таким Email"));
    }

    @GetMapping("/recovery/{email}")
    @ApiOperation("API for send to email about password reset")
    public ResponseMessage sendRecoveryCode(@PathVariable("email") String email) throws java.lang.Exception {
        userService.sendRecoveryCode(email);
        return new ResponseMessage(ResultCode.SUCCESS,"Код сброса отправлена на почту");
    }

    @GetMapping("/recovery/{code}/{newPassword}")
    @ApiOperation("API for reset password")
    public ResponseMessage recovery(@PathVariable String code,
                           @PathVariable String newPassword) {
        userService.resetPassword(code,newPassword);
        return new ResponseMessage(ResultCode.SUCCESS,"Пароль успешно изменен");
    }

    @PreAuthorize("hasAuthority('АДМИН')")
    @GetMapping("/admin/users")
    @ApiOperation("API User's list for ADMIN")
    public List<AppUsers> getAll(){
        return userService.getAllUsers();
    }

    @PreAuthorize("hasAuthority('АДМИН')")
    @GetMapping("/admin/users/{username}")
    @ApiOperation("API for get User data for ADMIN")
    public AppUsers getOneByUserNameForAdmin(@PathVariable String username){
        return userService.getByUserName(username);
    }

    @GetMapping("/users/me")
    @ApiOperation("API for get own user data")
    public UserProfile getOwnUserPage (Authentication authentication){
        AppUsers users = userService.getByEmail(authentication.getPrincipal().toString());
        return new UserProfile(users.getUsername(), users.getEmail(), users.getNumber());
    }

    @PutMapping("/users/me/change-password")
    @ApiOperation("API for change password")
    public ResponseMessage changePassword (Authentication authentication, @RequestBody RequestChangePassword password){
        userService.changePassword(authentication,password);
        return new ResponseMessage(ResultCode.SUCCESS, "Пароль успешно изменен!");
    }

    @PreAuthorize("hasAuthority('АДМИН')")
    @PostMapping("/admin/users")
    @ApiOperation("API for create new user")
    public ResponseMessage createUser (@RequestBody CreateUserRequest user) throws Exception {
        userService.createUser(user);
        return new ResponseMessage(ResultCode.SUCCESS,"Пользователь успешно добавлен");
    }

    @PutMapping("/users/{username}")
    @ApiOperation("API for change own user data")
    public ResponseMessage changeUserData (@RequestBody AppUsers newUser, @PathVariable String username){
        userService.changeUsersData(newUser,username);
        return new ResponseMessage(ResultCode.SUCCESS, "Данные успешно сохранены");
    }

    @PreAuthorize("hasAuthority('АДМИН')")
    @PutMapping("/admin/users/{username}")
    @ApiOperation("API for change user roles")
    public ResponseMessage changeUserRoles (@RequestBody AppUsers newUser, @PathVariable String username){
        userService.changeUsersRoles(newUser,username);
        return new ResponseMessage(ResultCode.SUCCESS, "Данные успешно сохранены");
    }
}
