package com.pcpedia.api.iam.domain.model.aggregate;

import com.pcpedia.api.iam.domain.model.enums.Role;
import com.pcpedia.api.shared.domain.model.AggregateRoot;
import com.pcpedia.api.shared.domain.model.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends AuditableEntity implements AggregateRoot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(name = "company_name", length = 200)
    private String companyName;

    @Column(length = 11)
    private String ruc;

    @Column(length = 20)
    private String phone;

    @Column(length = 300)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    // Domain methods
    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void toggleStatus() {
        this.isActive = !this.isActive;
    }

    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }

    public boolean isClient() {
        return this.role == Role.CLIENT;
    }
}
