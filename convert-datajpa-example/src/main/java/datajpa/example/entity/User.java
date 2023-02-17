package datajpa.example.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @MapKeyEnumerated
    private GenderEnum gender;
    private StatusEnum status;
}
