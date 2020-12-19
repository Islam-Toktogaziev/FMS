package KG.Neobis.FMS.Services;

import KG.Neobis.FMS.DAO.DeleteMethods;
import KG.Neobis.FMS.Entities.ChangeLog;
import KG.Neobis.FMS.Entities.Users.Privilege;
import KG.Neobis.FMS.Entities.Users.Roles;
import KG.Neobis.FMS.Enums.ResultCode;
import KG.Neobis.FMS.Exceptions.Exception;
import KG.Neobis.FMS.Exceptions.ResourceNotFoundExceptions;
import KG.Neobis.FMS.Repositories.ChangeLogRepository;
import KG.Neobis.FMS.Repositories.RoleRepository;
import KG.Neobis.FMS.dto.ResponseMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolesService {

    private final RoleRepository roleRepository;
    private final ChangeLogRepository logRepository;
    private final DeleteMethods deleteMethods;

    public RolesService(RoleRepository roleRepository, ChangeLogRepository logRepository, DeleteMethods deleteMethods) {
        this.roleRepository = roleRepository;
        this.logRepository = logRepository;
        this.deleteMethods = deleteMethods;
    }

    public List<Roles> getAll(){
        return roleRepository.findAll();
    }

    public List<Roles> getAllNotArchived(){
        return roleRepository.findAllByDeletedFalseAndArchivedFalse();
    }

    public List<Roles> getAllNotDeleted(){
        return roleRepository.findAllByDeletedFalse();
    }

    public Roles getOneByID (Long id){
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION, "Роли с таким ID не найдено!")));
    }

    public void createRole(Roles newRole){
        if (roleRepository.existsByNameAndDeletedFalse(newRole.getName())){
            throw new Exception(new ResponseMessage(ResultCode.EXCEPTION,"Роль с таким именем уже существует"));
        }
        logRepository.save(new ChangeLog("Создал роль '" + newRole.getName() + "'"));
        roleRepository.save(newRole);
    }

    public void changeRoleData(Roles newRole, Long id){
        roleRepository.findById(id)
                .map(roles -> {
                    if (roleRepository.existsByNameAndDeletedFalse(newRole.getName())
                    && !id.equals(roles.getId())){
                        throw new Exception(new ResponseMessage(ResultCode.EXCEPTION,"Роль с таким именем уже существует"));
                    }
                    if (!roles.getName().equals(newRole.getName())){
                        logRepository.save(new ChangeLog("Сменил название роли '" + roles.getName() + "' на '" + newRole.getName() + "'"));
                        roles.setName(newRole.getName());
                    }
                    if (roles.getPrivileges() != newRole.getPrivileges()){
                        logRepository.save(new ChangeLog("Сменил возможности для роли '" + roles.getPrivileges() + "'"));
                        roles.setPrivileges(newRole.getPrivileges());
                    }
                    return roleRepository.save(roles);
                })
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION, "Роли с таким ID не найдено!")));
    }

    public void archive(Long id){
        roleRepository.findById(id)
                .map(roles -> {
                    if (!roles.isArchived()){
                        logRepository.save(new ChangeLog("Заархивировал роль '" + roles.getName() + "'"));
                        roles.setArchived(true);
                        return roleRepository.save(roles);
                    }
                    if (roles.isArchived()){
                        logRepository.save(new ChangeLog("Раархивировал роль '" + roles.getName() + "'"));
                        roles.setArchived(false);
                        return roleRepository.save(roles);
                    }
                    return roleRepository.save(roles);
                })
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION, "Роли с таким ID не найдено!")));
    }

    public void deleteRole(Long roleID){
        roleRepository.findById(roleID)
                .map(roles -> {
                    logRepository.save(new ChangeLog("Удалил роль '" + roles.getName() + "'"));
                    roles.setDeleted(true);
                    deleteMethods.deleteRole(roleID);
                    return roleRepository.save(roles);
                })
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION, "Роли с таким ID не найдено!")));
    }
}
