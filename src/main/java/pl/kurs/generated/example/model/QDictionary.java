package pl.kurs.generated.example.model;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;
import pl.kurs.dictionary.model.Dictionary;
import pl.kurs.dictionary.model.DictionaryValue;

import javax.annotation.processing.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QDictionary is a Querydsl query type for Dictionary
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDictionary extends EntityPathBase<Dictionary> {

    private static final long serialVersionUID = 1256261218L;

    public static final QDictionary dictionary = new QDictionary("dictionary");

    public final BooleanPath deleted = createBoolean("deleted");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public final SetPath<DictionaryValue, QDictionaryValue> values = this.<DictionaryValue, QDictionaryValue>createSet("values", DictionaryValue.class, QDictionaryValue.class, PathInits.DIRECT2);

    public QDictionary(String variable) {
        super(Dictionary.class, forVariable(variable));
    }

    public QDictionary(Path<? extends Dictionary> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDictionary(PathMetadata metadata) {
        super(Dictionary.class, metadata);
    }

}

