package com.msAuth.infrastructure.Persistance.Entity;

import com.msAuth.domain.types.UserType;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.UUID)
   private UUID id;

   @Column(name = "user_name", nullable = false, unique = true)
   private String userName;

   @Column(name = "user_password", nullable = false)
   private String password;

   @Column(name = "email", nullable = false, unique = true)
   private String email;

   @Enumerated(EnumType.STRING)
   @Column(name = "user_type", nullable = false)
   private UserType userType;

   @Column(name = "user_status", nullable = false)
   private boolean userStatus;
}