package KG.Neobis.FMS.Services;

import KG.Neobis.FMS.Entities.Projects;
import KG.Neobis.FMS.Enums.ResultCode;
import KG.Neobis.FMS.Exceptions.ResourceNotFoundExceptions;
import KG.Neobis.FMS.Repositories.ProjectsRepository;
import KG.Neobis.FMS.dto.ResponseMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectsRepository projectsRepository;

    public ProjectService(ProjectsRepository projectsRepository) {
        this.projectsRepository = projectsRepository;
    }

    public List<Projects> getAllProjects (){
        return projectsRepository.findAll();
    }

    public Projects getOneByID (Long id){
        return projectsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION,"Не найден проект с таким ID")));
    }

    public Projects getOneByName (String name) {
        return projectsRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION,"Не найден проект с таким  именем")));
    }

    public Projects createNewProjects (Projects projects){
        return projectsRepository.save(projects);
    }
}
