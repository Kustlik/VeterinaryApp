package pl.gr.veterinaryapp.model.entity;

import lombok.Data;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Immutable
@Data
@Entity
@Table(name = "treatment_rooms")
public class TreatmentRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
}
