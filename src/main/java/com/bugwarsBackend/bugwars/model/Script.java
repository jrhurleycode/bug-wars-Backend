package com.bugwarsBackend.bugwars.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "scripts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Script {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "name", length = 100)
    @Size(min = 1, max = 100)
    private String name;

    @Column(name = "raw", length = 10000)
    @Size(max = 10000)
    private String raw;

    @Size(max = 10000)
    @Column(name = "bytecode", length = 10000)
    private int[] bytecode;

    @Column(name = "is_bytecode_valid")
    private boolean isBytecodeValid;


}