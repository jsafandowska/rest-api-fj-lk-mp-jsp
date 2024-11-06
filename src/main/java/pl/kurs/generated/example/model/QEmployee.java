package pl.kurs.generated.example.model;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;
import pl.kurs.inheritance.model.Employee;

import javax.annotation.processing.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QEmployee is a Querydsl query type for Employee
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEmployee extends EntityPathBase<Employee> {

    private static final long serialVersionUID = -2070422456L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEmployee employee = new QEmployee("employee");

    public final QPerson _super;

    //inherited
    public final NumberPath<Integer> age;

    // inherited
    public final QDictionaryValue country;

    //inherited
    public final DatePath<java.time.LocalDate> dateOfBirth;

    //inherited
    public final EnumPath<pl.kurs.inheritance.enums.Gender> gender;

    //inherited
    public final NumberPath<Integer> id;

    //inherited
    public final StringPath name;

    public final QDictionaryValue position;

    public final NumberPath<Integer> salary = createNumber("salary", Integer.class);

    public QEmployee(String variable) {
        this(Employee.class, forVariable(variable), INITS);
    }

    public QEmployee(Path<? extends Employee> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEmployee(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEmployee(PathMetadata metadata, PathInits inits) {
        this(Employee.class, metadata, inits);
    }

    public QEmployee(Class<? extends Employee> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QPerson(type, metadata, inits);
        this.age = _super.age;
        this.country = _super.country;
        this.dateOfBirth = _super.dateOfBirth;
        this.gender = _super.gender;
        this.id = _super.id;
        this.name = _super.name;
        this.position = inits.isInitialized("position") ? new QDictionaryValue(forProperty("position"), inits.get("position")) : null;
    }

}

