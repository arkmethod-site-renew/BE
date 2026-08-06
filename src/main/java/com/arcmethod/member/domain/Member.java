package com.arcmethod.member.domain;
import com.arcmethod.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "member")
public class Member extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 255, unique = true)
    private String email;
    @Column(name = "password_hash", nullable =false, length =255)
    private String passwordHash;
    @Column(nullable = false, length = 50)
    private String name;
    @Column(length = 20)
    private String phone;
    @Column(nullable = false, length = 20)
    private String role;
    @Column(nullable = false, length = 20)
    private String status;
    @Column(name = "last_login_at")
    private OffsetDateTime lastLoginAt;
    @Column(length = 300)
    private String memo;
    public boolean isAdmin(){
        return "ADMIN".equals(role);
    }
    public void markLoggedIn() {
        this.lastLoginAt = OffsetDateTime.now();
    }
    public void updateMemo(String memo){
        this.memo = memo;
    }
    public void updateStatus(String status){
        this.status = status;
    }
    public static Member signUp(String email, String encodedPassword, String name, String phone){
        Member m = new Member();
        m.email = email;
        m.passwordHash = encodedPassword;
        m.name = name;
        m.phone = phone;
        m.role = "USER";
        m.status = "ACTIVE";
        return m;
    }
}
