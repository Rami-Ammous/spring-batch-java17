package com.ammous.springbatch.student;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Rami AMMOUS
 */

@Getter
@Setter
@Entity
public class Student {

    @Id
    //@GeneratedValue
    private Integer id;
    private String firstName;
    private String lastName;
    private int age;
}
