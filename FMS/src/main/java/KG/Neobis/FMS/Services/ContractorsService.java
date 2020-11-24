package KG.Neobis.FMS.Services;

import KG.Neobis.FMS.Entities.Contractors;
import KG.Neobis.FMS.Enums.ResultCode;
import KG.Neobis.FMS.Exceptions.ResourceNotFoundExceptions;
import KG.Neobis.FMS.Repositories.ContractorsRepository;
import KG.Neobis.FMS.dto.ResponseMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractorsService {

    private final ContractorsRepository contractorsRepository;

    public ContractorsService(ContractorsRepository contractorsRepository) {
        this.contractorsRepository = contractorsRepository;
    }

    public List<Contractors> getAllContractors(){
        return contractorsRepository.findAll();
    }

    public Contractors getContractorByID(Long contractorID){
        return contractorsRepository.findById(contractorID)
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION," Не найден контрагент с таким ID")));
    }

    public Contractors getContractorByName(String name){
        return contractorsRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION," Не найден контрагент с таким именем")));
    }

    public Contractors getContractorIDByName(String name){
        return contractorsRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION," Не найден контрагент с таким именем")));
    }

    public Contractors createNewContractor (Contractors contractor){
        return contractorsRepository.save(contractor);
    }

    public Contractors updateContractor (Contractors newContractor, Long contractorID){
        return contractorsRepository.findById(contractorID)
                .map(contractors -> {
                    contractors.setName(newContractor.getName());
                    return contractorsRepository.save(contractors);
                })
                .orElseThrow(() -> new ResourceNotFoundExceptions(new ResponseMessage(ResultCode.EXCEPTION," Не найден контрагент с таким ID")));
    }
}
