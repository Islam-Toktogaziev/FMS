package KG.Neobis.FMS.Controllers;

import KG.Neobis.FMS.Entities.Contractors;
import KG.Neobis.FMS.Enums.ResultCode;
import KG.Neobis.FMS.Services.ContractorsService;
import KG.Neobis.FMS.dto.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(value = "Contractors", description = "APIs for contractors")
public class ContractorController {

    private ContractorsService service;

    public ContractorController(ContractorsService service) {
        this.service = service;
    }

    @GetMapping("/contractors")
    @ApiOperation(value = "API for get all of contractors")
    public List<Contractors> getAll(){
        return service.getAllContractors();
    }

    @PostMapping("/contractors")
    @ApiOperation(value = "API for post contractor")
    public ResponseMessage createContractor (@RequestBody Contractors contractor){
        service.createNewContractor(contractor);
        return new ResponseMessage(ResultCode.SUCCESS," Контрагент успешно добавлен");
    }

    @PutMapping("/contractors/{contractorID}")
    @ApiOperation(value = "API for change contractor's name")
    public ResponseMessage changeName (@RequestBody Contractors contractors, @PathVariable Long contractorID){
        service.updateContractor(contractors,contractorID);
        return new ResponseMessage(ResultCode.SUCCESS,"Имя контрагента успешно изменена");
    }
}
