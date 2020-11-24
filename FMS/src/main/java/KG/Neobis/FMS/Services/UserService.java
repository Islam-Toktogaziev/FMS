package KG.Neobis.FMS.Services;

import KG.Neobis.FMS.Entities.Users.AppUsers;
import KG.Neobis.FMS.Entities.Users.Roles;
import KG.Neobis.FMS.Enums.ResultCode;
import KG.Neobis.FMS.Exceptions.Exception;
import KG.Neobis.FMS.Exceptions.ResourceNotFoundExceptions;
import KG.Neobis.FMS.Repositories.AppUserRepository;
import KG.Neobis.FMS.Repositories.RoleRepository;
import KG.Neobis.FMS.dto.CreateUserRequest;
import KG.Neobis.FMS.dto.RequestChangePassword;
import KG.Neobis.FMS.dto.ResponseMessage;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {

    private final AppUserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;


    public UserService(AppUserRepository userRepository, BCryptPasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public List<AppUsers> getAllUsers(){
        return userRepository.findAll();
    }

    public AppUsers getOneByID(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION, "Пользователся с таким ID не найдено!")));
    }

    public void createUser (CreateUserRequest newUser) throws java.lang.Exception {
        if(!emailIsValid(newUser.getEmail())){
            throw new Exception(new ResponseMessage(ResultCode.EXCEPTION, "Введите адрес электронной почты"));
        }

        if (userRepository.existsByEmail(newUser.getEmail())){
            throw new Exception(new ResponseMessage(ResultCode.EXCEPTION, "Пользователь с такой электронной почтой уже зарегистрирован"));
        }
        AppUsers user = new AppUsers();
        String password = generatePassword(10);
        user.setEmail(newUser.getEmail());
        String username = newUser.getEmail().substring(0,newUser.getEmail().indexOf('@'));
        while (userRepository.existsByUsername(username)){
            username += generatePassword(1);
        }
        user.setUsername(username);
        Collection<Roles> roles = new ArrayList<>();
        for (String newRoles: newUser.getRoles()) {
            if (roleRepository.existsByName(newRoles)){
                roles.add(roleRepository.findByName(newRoles));
            }
        }
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(password));
        Mail.SendEmail(user.getEmail(), "FMS Neobis","Здравствуйте, вас добавили в группу пользователей FMS Neobis" +
                "\nДля входа используйте:\n\tАдрес электронной почты: " + user.getEmail() + "\n\tПароль: " + password +
                "\n\nПосле входа, пожалуйста, поменяйте пароль");
        userRepository.save(user);
    }

    public void changeUsersData(AppUsers newUser, String username){
        if (userRepository.existsByUsername(newUser.getUsername())){
            throw new Exception(new ResponseMessage(ResultCode.EXCEPTION, "Данное имя пользователя уже занято"));
        }
        userRepository.findByUsername(username)
                .map(appUsers -> {
                    appUsers.setNumber(newUser.getNumber());
                    appUsers.setUsername(newUser.getUsername());
                    return userRepository.save(appUsers);
                })
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION, "Пользователя с таким именем не найдено!")));
    }

    public void changeUsersRoles(AppUsers newUser, String username){
        userRepository.findByUsername(username)
                .map(appUsers -> {
                    appUsers.setRoles(newUser.getRoles());
                    return userRepository.save(appUsers);
                })
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION, "Пользователя с таким именем не найдено!")));
    }

    public AppUsers getByUserName(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION,"Пользователя с таким именем не найден")));
    }

    public AppUsers getByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }

    public void sendRecoveryCode(String email) throws java.lang.Exception {
        AppUsers user = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception(new ResponseMessage(ResultCode.EXCEPTION,"Пользователя с электронной почтой " + email + " не найдено!")));
        user.setRecoveryCode(UUID.randomUUID().toString());
        Mail.SendEmail(user.getEmail(), "Сброс пароля", "Код для сброса пароля: " + user.getRecoveryCode() +"\nПожалуйста, никому не сообщайте этот код в целях безопасности вашего аккаунта");
        userRepository.save(user);
    }

    public void resetPassword (String code, String newPassword){
        userRepository.findByRecoveryCode(code)
                .map(appUsers -> {
                    appUsers.setPassword(passwordEncoder.encode(newPassword));
                    appUsers.setRecoveryCode(null);
                    return userRepository.save(appUsers);
                })
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.FAIL, "Неправильно веден код")));
    }

    public void changePassword(String username,RequestChangePassword password){
        if (!password.getNewPassword().equals(password.getConfirmNewPassword())){
            throw new Exception(new ResponseMessage(ResultCode.FAIL, "Пароли не совпадают"));
        }
        AppUsers users = userRepository.findByUsername(username)
                .orElseThrow(() -> new Exception(new ResponseMessage(ResultCode.EXCEPTION, "Пользователя с именем " + username + " не найдено")));
        if (!passwordEncoder.matches(password.getOldPassword(),users.getPassword())){
            throw new Exception(new ResponseMessage(ResultCode.FAIL,"Пароль неверен"));
        }
        users.setPassword(passwordEncoder.encode(password.getNewPassword()));
        userRepository.save(users);
    }

    private String generatePassword(int size){
        PasswordGenerator passwordGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                .useDigits(true)
                .useLower(true)
                .useUpper(true)
                .build();
        String password = passwordGenerator.generate(size);
        return password;
    }

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private boolean emailIsValid(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }
}
