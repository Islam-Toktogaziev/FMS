package KG.Neobis.FMS.Services;

import KG.Neobis.FMS.DAO.DeleteMethods;
import KG.Neobis.FMS.Entities.ChangeLog;
import KG.Neobis.FMS.Entities.Projects;
import KG.Neobis.FMS.Enums.ResultCode;
import KG.Neobis.FMS.Exceptions.Exception;
import KG.Neobis.FMS.Exceptions.ResourceNotFoundExceptions;
import KG.Neobis.FMS.Repositories.ChangeLogRepository;
import KG.Neobis.FMS.Repositories.ProjectsRepository;
import KG.Neobis.FMS.dto.ResponseMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectsRepository projectsRepository;
    private final ChangeLogRepository logRepository;
    private final DeleteMethods deleteMethods;

    public ProjectService(ProjectsRepository projectsRepository, ChangeLogRepository logRepository, DeleteMethods deleteMethods) {
        this.projectsRepository = projectsRepository;
        this.logRepository = logRepository;
        this.deleteMethods = deleteMethods;
    }

    public List<Projects> getAllProjects (){
        return projectsRepository.findAll();
    }

    public List<Projects> getAllNotArchived(){
        return projectsRepository.findAllByDeletedFalseAndArchivedFalse();
    }

    public List<Projects> getAllNotDeleted(){
        return projectsRepository.findAllByDeletedFalse();
    }

    public Projects getOneByID (Long id){
        return projectsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION,"Не найден проект с таким ID")));
    }

    public Projects getOneByName (String name) {
        return projectsRepository.findByNameAndDeletedFalse(name)
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION,"Не найден проект с таким  именем")));
    }

    public Projects createNewProjects (Projects projects){
        if (projectsRepository.existsByNameAndDeletedFalse(projects.getName())){
            throw new Exception(new ResponseMessage(ResultCode.EXCEPTION,"Проект с таким именем уже существует"));
        }
        logRepository.save(new ChangeLog("Создал проект с названием '" + projects.getName() + "'"));
        return projectsRepository.save(projects);
    }

    public Projects changeProjectData(Long id, Projects newProjects){
        return projectsRepository.findById(id)
                .map(projects -> {
                    if (projectsRepository.existsByNameAndDeletedFalse(newProjects.getName())
                    && !id.equals(projects.getId())){
                        throw new Exception(new ResponseMessage(ResultCode.EXCEPTION,"Невозможно установить данное имя, так как проект с таким именем уже существует"));
                    }
                    projects.setName(newProjects.getName());
                    projects.setArchived(newProjects.isArchived());
                    projects.setTransactions(newProjects.getTransactions());
                    return projectsRepository.save(projects);
                })
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION,"Не найден проект с таким ID")));
    }

    public Projects archive(Long id){
        return projectsRepository.findById(id)
                .map(projects -> {
                    if (!projects.isArchived()){
                        logRepository.save(new ChangeLog("Заархивировал проект '" + projects.getName() + "'"));
                        projects.setArchived(true);
                        return projectsRepository.save(projects);
                    }
                    if (projects.isArchived()){
                        logRepository.save(new ChangeLog("Разархивировал проект '" + projects.getName() + "'"));
                        projects.setArchived(false);
                        return projectsRepository.save(projects);
                    }
                    return projectsRepository.save(projects);
                })
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION,"Не найден проект с таким ID")));
    }

    public void deleteProject(Long projectID){
        projectsRepository.findById(projectID)
                .map(projects -> {
                    deleteMethods.deleteProject(projectID);
                    projects.setDeleted(true);
                    logRepository.save(new ChangeLog("Удалил проект '" + projects.getName() + "'"));
                    return projectsRepository.save(projects);
                })
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION,"Не найден проект с таким ID")));
    }
}
