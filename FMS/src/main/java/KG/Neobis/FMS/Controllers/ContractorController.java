package KG.Neobis.FMS.Controllers;

import KG.Neobis.FMS.Entities.Contractors;
import KG.Neobis.FMS.Services.ContractorsService;
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
    public Contractors createContractor (@RequestBody Contractors contractor){
        return service.createNewContractor(contractor);
    }

    @PutMapping("/contractors/{contractorID}")
    @ApiOperation(value = "API for change contractor's name")
    public Contractors changeName (@RequestBody Contractors contractors, @PathVariable Long contractorID){
        return service.updateContractor(contractors,contractorID);
    }
}
