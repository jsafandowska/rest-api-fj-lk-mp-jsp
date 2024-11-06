package pl.kurs.generated.example.model;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;
import pl.kurs.model.ImportStatus;

import javax.annotation.processing.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QImportStatus is a Querydsl query type for ImportStatus
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QImportStatus extends EntityPathBase<ImportStatus> {

    private static final long serialVersionUID = -384704915L;

    public static final QImportStatus importStatus = new QImportStatus("importStatus");

    public final StringPath failedReason = createString("failedReason");

    public final StringPath fileName = createString("fileName");

    public final DateTimePath<java.time.LocalDateTime> finishDate = createDateTime("finishDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> processed = createNumber("processed", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> startDate = createDateTime("startDate", java.time.LocalDateTime.class);

    public final EnumPath<ImportStatus.Status> status = createEnum("status", ImportStatus.Status.class);

    public final DateTimePath<java.time.LocalDateTime> submitDate = createDateTime("submitDate", java.time.LocalDateTime.class);

    public QImportStatus(String variable) {
        super(ImportStatus.class, forVariable(variable));
    }

    public QImportStatus(Path<? extends ImportStatus> path) {
        super(path.getType(), path.getMetadata());
    }

    public QImportStatus(PathMetadata metadata) {
        super(ImportStatus.class, metadata);
    }

}

