package pl.kurs.dictionary.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Dictionary {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dictionaryIdGenerator")
    @SequenceGenerator(name = "dictionaryIdGenerator", sequenceName = "dictionary_seq", initialValue = 1000, allocationSize = 100)
    private int id;
    @Column(unique = true)
    private String name;
    @OneToMany(mappedBy = "dictionary")
    private Set<DictionaryValue> values = new HashSet<>();

    public Dictionary(String name) {
        this.name = name;
    }
}
