package sga.sgajwt.Dtos;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    // @Email
    private String email;

    @NotBlank
    private String first_name;
    
    @NotBlank
    private String last_name;

    private Set<String> roles;

}
