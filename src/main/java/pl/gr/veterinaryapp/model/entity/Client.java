package pl.gr.veterinaryapp.model.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String surname;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private VetAppUser user;
}
