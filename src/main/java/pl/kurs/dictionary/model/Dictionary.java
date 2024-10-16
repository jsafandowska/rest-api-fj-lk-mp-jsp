package pl.kurs.dictionary.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "name")
@NoArgsConstructor
//@SQLDelete(sql = "update dictionary set deleted = true where id = ?1")
@Where(clause = "deleted = false")

public class Dictionary {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dictionaryIdGenerator")
    @SequenceGenerator(name = "dictionaryIdGenerator", sequenceName = "dictionary_seq", initialValue = 1000, allocationSize = 100)
    private int id;
    @Column(unique = true)
    private String name;
    @OneToMany(mappedBy = "dictionary", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private Set<DictionaryValue> values = new HashSet<>();
    private boolean deleted;

    public Dictionary(String name) {
        this.name = name;
    }

    public Dictionary(String name, Set<String> values) {
        this.name = name;
        this.values = values.stream().map(v -> new DictionaryValue(v, this)).collect(Collectors.toSet());
    }
    public DictionaryValue getValueByName(String value) {
        return values.stream()
                .filter(dictValue -> dictValue.getValue().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Value not found: " + value));
    }
}
