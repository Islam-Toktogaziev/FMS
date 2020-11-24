package KG.Neobis.FMS.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateUserRequest {

    private String email;
    private List<String> roles;
}
