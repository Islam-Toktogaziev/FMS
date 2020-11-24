package KG.Neobis.FMS.dto;

import KG.Neobis.FMS.Enums.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMessage {

    private ResultCode resultCode;
    private String message;
}

