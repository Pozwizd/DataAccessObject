package models;

import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;
import java.util.List;


@Setter
@Getter
@Entity
@Table(name = "users", schema = "shop")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String email;

    @Column
    private String phone_number;

    @OneToOne(mappedBy = "user")
    private UserDetails userDetails;

    @OneToMany(mappedBy="user")
    private List<ShoppingCart> shoppingCarts;

    @OneToMany(mappedBy="user")
    private List<Order> orders;

    public User() {}

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phone_number='" + phone_number + '\'' +
                '}';
    }

    public User(int id, String username, String password, String email, String phoneNumber) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone_number = phoneNumber;
    }

    public User(String username, String password, String email, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone_number = phoneNumber;
    }
}