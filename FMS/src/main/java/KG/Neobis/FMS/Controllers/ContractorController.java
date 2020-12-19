package KG.Neobis.FMS.Controllers;

import KG.Neobis.FMS.Entities.Contractors;
import KG.Neobis.FMS.Enums.ResultCode;
import KG.Neobis.FMS.Services.ContractorsService;
import KG.Neobis.FMS.dto.ContractorModel;
import KG.Neobis.FMS.dto.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PUT,RequestMethod.OPTIONS})
@RestController
@Api(value = "Contractors", description = "APIs for contractors")
public class ContractorController {

    private ContractorsService service;

    public ContractorController(ContractorsService service) {
        this.service = service;
    }

    @PreAuthorize("hasAuthority('АДМИН')")
    @GetMapping("/admin/contractors")
    @ApiOperation(value = "API for get all of contractors")
    public List<Contractors> getAll(){
        return service.getAllContractors();
    }

    @GetMapping("/contractors/not_archived")
    @ApiOperation(value = "API for get all of contractors that are not archived")
    public List<ContractorModel> getAllNotArchived(){
        return service.getAllNotArchived();
    }

    @GetMapping("/contractors/not_deleted")
    @ApiOperation(value = "API for get all of contractors that are not deleted")
    public List<ContractorModel> getAllNotDeleted(){
        return service.getAllNotDeleted();
    }

    @GetMapping("/contractors/{contractorID}")
    @ApiOperation("API for get one contractors")
    public Contractors getOne(@PathVariable Long contractorID){
        return service.getContractorByID(contractorID);
    }

    @PreAuthorize("hasAuthority('Добавление/изменение_контрагентов')")
    @PostMapping("/contractors")
    @ApiOperation(value = "API for post contractor")
    public ResponseMessage createContractor (@RequestBody ContractorModel contractor){
        service.createNewContractor(contractor);
        return new ResponseMessage(ResultCode.SUCCESS," Контрагент успешно добавлен");
    }

    @PreAuthorize("hasAuthority('Добавление/изменение_контрагентов')")
    @PutMapping("/contractors/{contractorID}")
    @ApiOperation(value = "API for change contractor's name")
    public ResponseMessage changeName (@RequestBody ContractorModel contractors, @PathVariable Long contractorID){
        service.updateContractor(contractors,contractorID);
        return new ResponseMessage(ResultCode.SUCCESS,"Имя контрагента успешно изменена");
    }

    @PreAuthorize("hasAuthority('АДМИН')")
    @DeleteMapping("/admin/contractors/delete/{id}")
    @ApiOperation("Delete contractor by ID")
    public ResponseMessage deleteContractor(@PathVariable Long id){
        service.deleteContractor(id);
        return new ResponseMessage(ResultCode.SUCCESS,"Контрагент успешно удален");
    }

    @PostMapping("/contractors/archive/{id}")
    @ApiOperation(value = "API for archive contractor")
    public ResponseMessage archive (Long id){
        service.archive(id);
        return new ResponseMessage(ResultCode.SUCCESS," Контрагент успешно добавлен");
    }
}

