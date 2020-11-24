package KG.Neobis.FMS.Controllers;

import KG.Neobis.FMS.Entities.Projects;
import KG.Neobis.FMS.Enums.ResultCode;
import KG.Neobis.FMS.Services.ProjectService;
import KG.Neobis.FMS.dto.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@Api (value = "Projects", description = "APIs for projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    @ApiOperation(value = "API for get all projects")
    public List<Projects> getAllProjects (){
        return projectService.getAllProjects();
    }

    @GetMapping("/{projectID}")
    @ApiOperation(value = "API for get one of projects")
    public Projects getOne (@PathVariable Long projectID){
        return projectService.getOneByID(projectID);
    }

    @PostMapping
    @ApiOperation(value = "API for create a new project")
    public ResponseMessage createProject (@RequestBody Projects projects){
        projectService.createNewProjects(projects);
        return new ResponseMessage(ResultCode.SUCCESS,"Проект успешно добавлен");
    }
}
