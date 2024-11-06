package pl.kurs.generated.example.model;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;
import pl.kurs.pesimistic.lock.example.model.Doctor;
import pl.kurs.pesimistic.lock.example.model.Visit;

import javax.annotation.processing.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QDoctor is a Querydsl query type for Doctor
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDoctor extends EntityPathBase<Doctor> {

    private static final long serialVersionUID = 15782576L;

    public static final QDoctor doctor = new QDoctor("doctor");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public final SetPath<Visit, QVisit> visits = this.<Visit, QVisit>createSet("visits", Visit.class, QVisit.class, PathInits.DIRECT2);

    public QDoctor(String variable) {
        super(Doctor.class, forVariable(variable));
    }

    public QDoctor(Path<? extends Doctor> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDoctor(PathMetadata metadata) {
        super(Doctor.class, metadata);
    }

}

