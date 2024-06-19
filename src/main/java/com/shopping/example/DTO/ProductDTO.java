

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {


    @NotEmpty
    private String name;

    @NotEmpty
    private String image;

    @NotEmpty
    private String  category;

    @NotEmpty
    private String  brand;

    @NotEmpty
    private String  supplies;


}
