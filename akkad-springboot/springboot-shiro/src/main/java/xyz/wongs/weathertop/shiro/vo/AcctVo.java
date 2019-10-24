package xyz.wongs.weathertop.shiro.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcctVo implements Serializable {

    @NotBlank(message = "{required}")
    private String username;

    @NotBlank(message = "{required}")
    private String password;
}

