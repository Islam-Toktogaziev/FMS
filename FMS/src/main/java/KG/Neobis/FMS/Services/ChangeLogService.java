package KG.Neobis.FMS.Services;

import KG.Neobis.FMS.Entities.ChangeLog;
import KG.Neobis.FMS.Repositories.ChangeLogRepository;
import KG.Neobis.FMS.dto.ChangeLogsDTO;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChangeLogService {

    private final ChangeLogRepository logRepository;

    public ChangeLogService(ChangeLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public List<ChangeLogsDTO> getChangeLogs(){
        List<ChangeLog> listFromDB = logRepository.findAll(Sort.by("id").descending());
        List<ChangeLogsDTO> logList = new ArrayList<>();
        for (ChangeLog temp: listFromDB
             ) {
            logList.add(new ChangeLogsDTO(temp.getCreatedAt(), temp.getCreatedBy(), temp.getOperation()));
        }
        return logList;
    }
}
