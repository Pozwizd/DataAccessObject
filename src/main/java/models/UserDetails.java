package models;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import javax.persistence.GenerationType;
import java.util.Date;

@Setter
@Getter
@Entity(name="user_details")
public class UserDetails {

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="gender")
    private String gender;

    @Column(name="date_of_birth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @Column
    private String address;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private int userId;

    @OneToOne
    @JoinColumn(name="user_id")
    private User user;

    public UserDetails() {}

    public UserDetails(String firstName, String lastName, String gender, Date dateOfBirth, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }
}