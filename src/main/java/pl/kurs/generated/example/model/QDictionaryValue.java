package pl.kurs.generated.example.model;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;
import pl.kurs.dictionary.model.DictionaryValue;

import javax.annotation.processing.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QDictionaryValue is a Querydsl query type for DictionaryValue
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDictionaryValue extends EntityPathBase<DictionaryValue> {

    private static final long serialVersionUID = 1123502127L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDictionaryValue dictionaryValue = new QDictionaryValue("dictionaryValue");

    public final BooleanPath deleted = createBoolean("deleted");

    public final QDictionary dictionary;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath value = createString("value");

    public QDictionaryValue(String variable) {
        this(DictionaryValue.class, forVariable(variable), INITS);
    }

    public QDictionaryValue(Path<? extends DictionaryValue> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDictionaryValue(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDictionaryValue(PathMetadata metadata, PathInits inits) {
        this(DictionaryValue.class, metadata, inits);
    }

    public QDictionaryValue(Class<? extends DictionaryValue> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.dictionary = inits.isInitialized("dictionary") ? new QDictionary(forProperty("dictionary")) : null;
    }

}

