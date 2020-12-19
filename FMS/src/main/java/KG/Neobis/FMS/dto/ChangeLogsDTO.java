package KG.Neobis.FMS.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeLogsDTO {

    @JsonFormat(pattern="dd-MM-yyyy")
    private Date createdDate;

    private String userEmail;
    private String operation;

}
