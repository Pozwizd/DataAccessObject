package models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "user_details")
public class UserDetails {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int userId;

    @OneToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String gender;

    @Column
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @Column
    private String address;

    public UserDetails() {}

    public UserDetails(String firstName, String lastName, String gender, Date dateOfBirth, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }
}