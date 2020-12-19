package KG.Neobis.FMS.Controllers;

import KG.Neobis.FMS.Entities.Users.Privilege;
import KG.Neobis.FMS.Entities.Users.Roles;
import KG.Neobis.FMS.Enums.ResultCode;
import KG.Neobis.FMS.Services.PrivilegeService;
import KG.Neobis.FMS.Services.RolesService;
import KG.Neobis.FMS.dto.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PUT,RequestMethod.OPTIONS})
@RestController
@Api ("APIs for create/change/get roles")
public class RolesController {

    private final RolesService rolesService;
    private final PrivilegeService privilegeService;

    public RolesController(RolesService rolesService, PrivilegeService privilegeService) {
        this.rolesService = rolesService;
        this.privilegeService = privilegeService;
    }

    @PreAuthorize("hasAuthority('АДМИН')")
    @GetMapping("/admin/roles")
    @ApiOperation("API for get all roles")
    public List<Roles> getAll(){
        return rolesService.getAllNotDeleted();
    }

    @PreAuthorize("hasAuthority('АДМИН')")
    @GetMapping("/admin/roles/{roleID}")
    @ApiOperation("API for get one role by ID")
    public Roles getOneByID(@PathVariable Long roleID){
        return rolesService.getOneByID(roleID);
    }

    @PreAuthorize("hasAuthority('АДМИН')")
    @PostMapping("/admin/roles")
    @ApiOperation("API for create new role")
    public ResponseMessage createRole (@RequestBody Roles roles){
        rolesService.createRole(roles);
        return new ResponseMessage(ResultCode.SUCCESS, "Роль успешно создана");
    }

    @PreAuthorize("hasAuthority('АДМИН')")
    @PutMapping("/admin/roles/{roleID}")
    @ApiOperation("API for change role permissions")
    public ResponseMessage changePermissions(@RequestBody Roles newRole, @PathVariable Long roleID){
        rolesService.changeRoleData(newRole, roleID);
        return new ResponseMessage(ResultCode.SUCCESS,"Данные успешно сохранены");
    }

    @PutMapping("/admin/roles/archive/{roleID}")
    @ApiOperation("API for change role permissions")
    public ResponseMessage archive(@PathVariable Long roleID){
        rolesService.archive(roleID);
        return new ResponseMessage(ResultCode.SUCCESS,"Данные успешно сохранены");
    }

    @PreAuthorize("hasAuthority('АДМИН')")
    @DeleteMapping("/admin/roles/delete/{roleID}")
    @ApiOperation("Delete role by ID")
    public ResponseMessage deleteRole(@PathVariable Long roleID){
        rolesService.deleteRole(roleID);
        return new ResponseMessage(ResultCode.SUCCESS,"Успешно удален");
    }

    @PreAuthorize("hasAuthority('АДМИН')")
    @GetMapping("/admin/roles/privileges")
    @ApiOperation("API for get all privileges")
    public List<Privilege> getAllPrivileges(){
        return privilegeService.getAllPrivileges();
    }

}
