package pl.kurs.dictionary.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = {"value", "dictionary"})
@NoArgsConstructor
//@SQLDelete(sql = "update dictionary_value set deleted = true where id = ?1")
@Where(clause = "deleted = false")
public class DictionaryValue {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dictionaryValueIdGenerator")
    @SequenceGenerator(name = "dictionaryValueIdGenerator", sequenceName = "dictionary_value_seq", initialValue = 100000, allocationSize = 1000)
    private int id;
    @Column(name = "dict_value")
    private String value;
    @ManyToOne
    @JoinColumn(name = "dictionary_id")
    private Dictionary dictionary;
    private boolean deleted;

    public DictionaryValue(String value, Dictionary dictionary) {
        this.value = value;
        this.dictionary = dictionary;
        dictionary.getValues().add(this);
    }

}
