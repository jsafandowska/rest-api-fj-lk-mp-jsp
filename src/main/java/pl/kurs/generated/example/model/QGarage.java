package pl.kurs.generated.example.model;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;
import pl.kurs.model.Car;
import pl.kurs.model.Garage;

import javax.annotation.processing.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QGarage is a Querydsl query type for Garage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGarage extends EntityPathBase<Garage> {

    private static final long serialVersionUID = 637577757L;

    public static final QGarage garage = new QGarage("garage");

    public final StringPath address = createString("address");

    public final SetPath<Car, QCar> cars = this.<Car, QCar>createSet("cars", Car.class, QCar.class, PathInits.DIRECT2);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final BooleanPath lpgAllowed = createBoolean("lpgAllowed");

    public final NumberPath<Integer> places = createNumber("places", Integer.class);

    public QGarage(String variable) {
        super(Garage.class, forVariable(variable));
    }

    public QGarage(Path<? extends Garage> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGarage(PathMetadata metadata) {
        super(Garage.class, metadata);
    }

}

