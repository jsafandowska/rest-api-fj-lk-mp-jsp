package pl.kurs.generated.example.model;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;
import pl.kurs.inheritance.model.Student;

import javax.annotation.processing.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QStudent is a Querydsl query type for Student
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStudent extends EntityPathBase<Student> {

    private static final long serialVersionUID = 1202155873L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStudent student = new QStudent("student");

    public final QPerson _super;

    //inherited
    public final NumberPath<Integer> age;

    // inherited
    public final QDictionaryValue country;

    //inherited
    public final DatePath<java.time.LocalDate> dateOfBirth;

    //inherited
    public final EnumPath<pl.kurs.inheritance.enums.Gender> gender;

    public final StringPath group = createString("group");

    //inherited
    public final NumberPath<Integer> id;

    //inherited
    public final StringPath name;

    public final NumberPath<Integer> scholarship = createNumber("scholarship", Integer.class);

    public QStudent(String variable) {
        this(Student.class, forVariable(variable), INITS);
    }

    public QStudent(Path<? extends Student> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStudent(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStudent(PathMetadata metadata, PathInits inits) {
        this(Student.class, metadata, inits);
    }

    public QStudent(Class<? extends Student> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QPerson(type, metadata, inits);
        this.age = _super.age;
        this.country = _super.country;
        this.dateOfBirth = _super.dateOfBirth;
        this.gender = _super.gender;
        this.id = _super.id;
        this.name = _super.name;
    }

}

