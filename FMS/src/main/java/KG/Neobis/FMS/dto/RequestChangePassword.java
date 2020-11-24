package KG.Neobis.FMS.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestChangePassword {

    private String oldPassword;
    private String newPassword;
    private String confirmNewPassword;
}
