package KG.Neobis.FMS.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@SequenceGenerator(name = "SEQ_ID", sequenceName = "SEQ_CHANGE_LOG", allocationSize = 1)
public class ChangeLog extends BaseEntityAudit {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String operation;

    public ChangeLog(String operation) {
        this.operation = operation;
    }
}
