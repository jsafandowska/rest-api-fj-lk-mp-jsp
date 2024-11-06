package pl.kurs.model.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.kurs.generated.example.model.QBook;

import java.util.Arrays;

@Getter
@Setter
@ToString
public class FindBookQuery {
    private String title;
    private String category;
    private Boolean available;
    private String author;

    public Predicate toPredicate() {
        BooleanBuilder conditions = new BooleanBuilder();
        if(title != null) conditions.and(QBook.book.title.containsIgnoreCase(title));
        if(category != null) conditions.and(QBook.book.category.eq(category));
        if(available != null) conditions.and(QBook.book.available.eq(available));
        if(author != null) {
            String[] authorParts = author.split(" ");
            BooleanBuilder authorCondition = new BooleanBuilder();
            Arrays.stream(authorParts).forEach(part -> {
                authorCondition.or(QBook.book.author.name.containsIgnoreCase(part));
                authorCondition.or(QBook.book.author.surname.containsIgnoreCase(part));
                conditions.and(authorCondition);
            });
        }
        return conditions;
    }
    // todo
    // przerobic ten kod w taki sposb zeby mozna bylo wyszukac po autorze Adam Mickiewicz Henryk Sienkiewicz
    // i ma wyszukac wszystkich Henrykow wszystkich adamoow wszystkich Mickiewiczow i wszystkich sienkiewiczow

}
