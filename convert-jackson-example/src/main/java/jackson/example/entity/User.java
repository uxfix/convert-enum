package jackson.example.entity;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String name;
    private GenderEnum gender;
}
