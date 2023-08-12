package Entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name = "user_details")
public class UserDetails {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "gender")
    private Gender gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column
    private String address;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public UserDetails() {
    }

    public UserDetails(String firstName, String lastName, Gender gender, LocalDate dateOfBirth, String address, User user) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.user = user;
    }



}