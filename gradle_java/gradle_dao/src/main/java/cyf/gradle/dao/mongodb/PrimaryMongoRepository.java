package cyf.gradle.dao.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PrimaryMongoRepository extends MongoRepository<PrimaryMongoObject, String> {
}
