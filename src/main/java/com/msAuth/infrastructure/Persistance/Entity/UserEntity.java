package com.msAuth.infrastructure.Persistance.Entity;

import com.msAuth.domain.Model.RoleUser;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.UUID)
   private UUID id;
   @Column(name = "user_name", nullable = false)
   private String userName;
   @Column(name = "user_password", nullable = false)
   private String password;
   @Enumerated(EnumType.STRING)
   @Column(name = "user_status", nullable = false)
   private UserStatus userStatus;
}