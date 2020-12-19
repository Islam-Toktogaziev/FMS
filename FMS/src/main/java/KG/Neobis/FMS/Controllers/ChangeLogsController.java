package KG.Neobis.FMS.Controllers;

import KG.Neobis.FMS.Services.ChangeLogService;
import KG.Neobis.FMS.dto.ChangeLogsDTO;
import io.swagger.annotations.Api;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PUT,RequestMethod.OPTIONS})
@RestController
@Api(value = "Change logs", description = "APIs for change logs")
public class ChangeLogsController {

    private final ChangeLogService service;

    public ChangeLogsController(ChangeLogService service) {
        this.service = service;
    }

    @PreAuthorize("hasAuthority('АДМИН')")
    @GetMapping("/admin/change_logs")
    public List<ChangeLogsDTO> getChangeLogs(){
        return service.getChangeLogs();
    }
}
