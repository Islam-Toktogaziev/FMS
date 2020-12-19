package KG.Neobis.FMS.Services;

import KG.Neobis.FMS.DAO.DeleteMethods;
import KG.Neobis.FMS.Entities.ChangeLog;
import KG.Neobis.FMS.Entities.Contractors;
import KG.Neobis.FMS.Enums.ResultCode;
import KG.Neobis.FMS.Exceptions.Exception;
import KG.Neobis.FMS.Exceptions.ResourceNotFoundExceptions;
import KG.Neobis.FMS.Repositories.ChangeLogRepository;
import KG.Neobis.FMS.Repositories.ContractorsRepository;
import KG.Neobis.FMS.dto.ContractorModel;
import KG.Neobis.FMS.dto.ResponseMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContractorsService {

    private final ContractorsRepository contractorsRepository;
    private final ChangeLogRepository logRepository;
    private final DeleteMethods deleteMethods;

    public ContractorsService(ContractorsRepository contractorsRepository, ChangeLogRepository logRepository, DeleteMethods deleteMethods) {
        this.contractorsRepository = contractorsRepository;
        this.logRepository = logRepository;
        this.deleteMethods = deleteMethods;
    }

    public List<Contractors> getAllContractors(){
        return contractorsRepository.findAll();
    }

    public List<ContractorModel> getAllNotArchived(){
        List<Contractors> contractorsFromDB = contractorsRepository.findAllByArchivedFalseAndDeletedFalse();
        List<ContractorModel> contractor = new ArrayList<>();
        for (Contractors temp: contractorsFromDB) {
            contractor.add(serialize(temp));
        }
        return contractor;
    }

    public List<ContractorModel> getAllNotDeleted(){
        List<Contractors> contractorsFromDB = contractorsRepository.findAllByDeletedFalse();
        List<ContractorModel> contractor = new ArrayList<>();
        for (Contractors temp: contractorsFromDB) {
            contractor.add(serialize(temp));
        }
        return contractor;
    }

    public Contractors getContractorByID(Long contractorID){
        return contractorsRepository.findById(contractorID)
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION," Не найден контрагент с таким ID")));
    }

    public Contractors getContractorByName(String name){
        return contractorsRepository.findByNameAndDeletedFalse(name)
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION," Не найден контрагент с таким именем")));
    }

    public Contractors createNewContractor (ContractorModel contractor){
        if (contractorsRepository.existsByNameAndDeletedFalse(contractor.getName())){
            throw new Exception(new ResponseMessage(ResultCode.EXCEPTION,"Контрагент с таким именем уже существует"));
        }
        Contractors newContractor = new Contractors();
        newContractor.setName(contractor.getName());
        newContractor.setArchived(false);
        newContractor.setDeleted(false);
        logRepository.save(new ChangeLog("Создал контрагента с именем '" + newContractor.getName() + "'"));
        return contractorsRepository.save(newContractor);
    }

    public Contractors updateContractor (ContractorModel newContractor, Long contractorID){
        return contractorsRepository.findById(contractorID)
                .map(contractors -> {
                    if (contractorsRepository.existsByNameAndDeletedFalse(newContractor.getName()) && !contractorID.equals(contractors.getId())){
                        throw new Exception(new ResponseMessage(ResultCode.EXCEPTION,"Контрагент с таким именем уже существует"));
                    }
                    if (!contractors.getName().equals(newContractor.getName())){
                        logRepository.save(new ChangeLog("Изменил имя контрагента '" + contractors.getName() + "' на '" + newContractor.getName() + "'"));
                        contractors.setName(newContractor.getName());
                    }
                    if (!contractors.isArchived() && newContractor.isArchived()){
                        contractors.setArchived(newContractor.isArchived());
                        logRepository.save(new ChangeLog("Заархивировал контрагента '" + contractors.getName() + "'"));
                    }
                    if (contractors.isArchived() && !newContractor.isArchived()){
                        contractors.setArchived(newContractor.isArchived());
                        logRepository.save(new ChangeLog("Разархивировал контрагента '" + contractors.getName() + "'"));
                    }
                    return contractorsRepository.save(contractors);
                })
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION," Не найден контрагент с таким ID")));
    }

    public Contractors archive ( Long contractorID){
        return contractorsRepository.findById(contractorID)
                .map(contractors -> {
                    if (!contractors.isArchived()){
                        contractors.setArchived(true);
                        logRepository.save(new ChangeLog("Заархивировал контрагента '" + contractors.getName() + "'"));
                        return contractorsRepository.save(contractors);
                    }
                    if (contractors.isArchived()){
                        contractors.setArchived(false);
                        logRepository.save(new ChangeLog("Разархивировал контрагента '" + contractors.getName() + "'"));
                        return contractorsRepository.save(contractors);
                    }
                    return contractorsRepository.save(contractors);
                })
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION," Не найден контрагент с таким ID")));
    }

    public void deleteContractor(Long contractorID){
        contractorsRepository.findById(contractorID)
                .map(contractors -> {
                    deleteMethods.deleteContractor(contractorID);
                    contractors.setDeleted(true);
                    logRepository.save(new ChangeLog("Удалил контрагента '" + contractors.getName() + "'"));
                    return contractorsRepository.save(contractors);
                })
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION," Не найден контрагент с таким ID")));
    }

    private ContractorModel serialize(Contractors temp){
        ContractorModel contractorModel = new ContractorModel();
        contractorModel.setName(temp.getName());
        contractorModel.setArchived(temp.isArchived());
        contractorModel.setId(temp.getId());
        return contractorModel;
    }
}
