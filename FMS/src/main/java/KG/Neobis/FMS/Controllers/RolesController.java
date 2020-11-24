package KG.Neobis.FMS.Controllers;

import KG.Neobis.FMS.Entities.Users.Roles;
import KG.Neobis.FMS.Enums.ResultCode;
import KG.Neobis.FMS.Services.RolesService;
import KG.Neobis.FMS.dto.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api ("APIs for create/change/get roles")
public class RolesController {

    private final RolesService rolesService;

    public RolesController(RolesService rolesService) {
        this.rolesService = rolesService;
    }

    @GetMapping("/admin/roles")
    @ApiOperation("API for get all roles")
    public List<Roles> getAll(){
        return rolesService.getAll();
    }

    @GetMapping("/admin/roles/{roleID}")
    @ApiOperation("API for get one role by ID")
    public Roles getOneByID(@PathVariable Long roleID){
        return rolesService.getOneByID(roleID);
    }

    @PostMapping("/admin/roles")
    @ApiOperation("API for create new role")
    public ResponseMessage createRole (@RequestBody Roles roles){
        rolesService.createRole(roles);
        return new ResponseMessage(ResultCode.SUCCESS, "Роль успешно создана");
    }

    @PutMapping("/admin/roles/{roleID}")
    @ApiOperation("API for change role permissions")
    public ResponseMessage changePermissions(@RequestBody Roles newRole, @PathVariable Long roleID){
        rolesService.changePermissionsInRole(newRole, roleID);
        return new ResponseMessage(ResultCode.SUCCESS,"Данные успешно сохранены");
    }
}
