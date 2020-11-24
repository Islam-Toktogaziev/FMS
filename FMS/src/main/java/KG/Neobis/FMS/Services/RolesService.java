package KG.Neobis.FMS.Services;

import KG.Neobis.FMS.Entities.Users.Roles;
import KG.Neobis.FMS.Enums.ResultCode;
import KG.Neobis.FMS.Exceptions.ResourceNotFoundExceptions;
import KG.Neobis.FMS.Repositories.RoleRepository;
import KG.Neobis.FMS.dto.ResponseMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolesService {

    private final RoleRepository roleRepository;

    public RolesService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Roles> getAll(){
        return roleRepository.findAll();
    }

    public Roles getOneByID (Long id){
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION, "Роли с таким ID не найдено!")));
    }

    public void createRole(Roles newRole){
        roleRepository.save(newRole);
    }

    public void changePermissionsInRole (Roles newRole, Long id){
        roleRepository.findById(id)
                .map(roles -> {
                    roles.setPrivileges(newRole.getPrivileges());
                    return roleRepository.save(roles);
                })
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION, "Роли с таким ID не найдено!")));
    }
}
