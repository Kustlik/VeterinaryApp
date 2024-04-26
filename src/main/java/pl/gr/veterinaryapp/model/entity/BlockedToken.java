package pl.gr.veterinaryapp.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "blocked_tokens")
public class BlockedToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "token")
    private String authToken;
    private OffsetDateTime expirationTime;
}
