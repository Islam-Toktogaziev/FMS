package KG.Neobis.FMS.Services;

import KG.Neobis.FMS.Entities.Users.AppUsers;
import KG.Neobis.FMS.Repositories.AppUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private AppUserRepository UserRepository;

    public UserDetailsServiceImpl(AppUserRepository UserRepository) {
        this.UserRepository = UserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUsers User = UserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
        return User;
    }
}