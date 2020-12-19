package KG.Neobis.FMS.Controllers;

import KG.Neobis.FMS.Entities.Projects;
import KG.Neobis.FMS.Enums.ResultCode;
import KG.Neobis.FMS.Services.ProjectService;
import KG.Neobis.FMS.dto.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PUT,RequestMethod.OPTIONS})
@RestController
@Api (value = "Projects", description = "APIs for projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PreAuthorize("hasAuthority('АДМИН')")
    @GetMapping ("/admin/projects")
    @ApiOperation(value = "API for get all projects")
    public List<Projects> getAllProjects (){
        return projectService.getAllProjects();
    }

    @GetMapping("/projects/not_archived")
    @ApiOperation("API for get all of projects that not archived")
    public List<Projects> getAllNotArchived(){
        return projectService.getAllNotArchived();
    }

    @GetMapping("/projects/not_deleted")
    @ApiOperation("API for get all of projects that not deleted")
    public List<Projects> getAllNotDeleted(){
        return projectService.getAllNotDeleted();
    }

    @GetMapping("/projects/{projectID}")
    @ApiOperation(value = "API for get one of projects")
    public Projects getOne (@PathVariable Long projectID){
        return projectService.getOneByID(projectID);
    }

    @PreAuthorize("hasAuthority('Добавление/изменение_проектов')")
    @PostMapping ("/projects")
    @ApiOperation(value = "API for create a new project")
    public ResponseMessage createProject (@RequestBody Projects projects){
        projectService.createNewProjects(projects);
        return new ResponseMessage(ResultCode.SUCCESS,"Проект успешно добавлен");
    }

    /*@PutMapping("/projects/{projectID}")
    @ApiOperation("API for change project data")
    public ResponseMessage changeData(@PathVariable Long projectID,
                                      Projects projects){
        projectService.changeProjectData(projectID,projects);
        return new ResponseMessage(ResultCode.SUCCESS, "Данные успешно сохранены!");
    }*/

    @PutMapping("/projects/archive/{projectID}")
    @ApiOperation("API for change project data")
    public ResponseMessage changeData(@PathVariable Long projectID) {
        projectService.archive(projectID);
        return new ResponseMessage(ResultCode.SUCCESS, "Данные успешно сохранены!");
    }

    @PreAuthorize("hasAuthority('АДМИН')")
    @DeleteMapping("/admin/projects/delete/{categoryID}")
    @ApiOperation("Delete project by ID")
    public ResponseMessage deleteCategory(@PathVariable Long categoryID){
        projectService.deleteProject(categoryID);
        return new ResponseMessage(ResultCode.SUCCESS,"Успешно удален");
    }
}
