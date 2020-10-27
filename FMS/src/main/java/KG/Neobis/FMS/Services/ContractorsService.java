package KG.Neobis.FMS.Services;

import KG.Neobis.FMS.Entities.Contractors;
import KG.Neobis.FMS.Exceptions.ResourceNotFoundExceptions;
import KG.Neobis.FMS.Repositories.ContractorsRepository;
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
                .orElseThrow(() -> new ResourceNotFoundExceptions(" Не найден контрагент с таким ID"));
    }

    public Contractors getContractorByName(String name){
        return contractorsRepository.findByName(name)
                .orElseGet(() ->{
                    return contractorsRepository.save(new Contractors(name));
                });
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
                .orElseThrow(() -> new ResourceNotFoundExceptions(" Не найден контрагент с таким ID"));
    }
}
