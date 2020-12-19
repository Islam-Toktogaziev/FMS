package KG.Neobis.FMS.Services;

import KG.Neobis.FMS.Entities.Users.Privilege;
import KG.Neobis.FMS.Repositories.PrivilegeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrivilegeService {

    private final PrivilegeRepository repository;

    public PrivilegeService(PrivilegeRepository repository) {
        this.repository = repository;
    }

    public List<Privilege> getAllPrivileges(){
        return repository.findAll();
    }
}
