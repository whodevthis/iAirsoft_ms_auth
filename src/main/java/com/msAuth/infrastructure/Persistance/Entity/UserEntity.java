package com.msAuth.infrastructure.Persistance.Entity;

import com.msAuth.domain.Model.RoleUser;
import com.msAuth.domain.Model.UserStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   @Column(name = "user_name", nullable = false)
   private String userName;
   @Column(name = "user_password", nullable = false)
   private String password;
   @Enumerated(EnumType.STRING)
   @Column(name = "user_role", nullable = false)
   private RoleUser role;
   @Enumerated(EnumType.STRING)
   @Column(name = "user_status", nullable = false)
   private UserStatus userStatus;
   @Column(name = "user_creationDate", nullable = false)
   long creationDate;
}