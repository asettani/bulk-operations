package com.rssp.demorssp.repository;

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.model.DeleteManyModel;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.UpdateOptions;
import com.rssp.demorssp.models.UserDocument;
import lombok.AllArgsConstructor;
import lombok.val;
import org.bson.Document;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Repository
@AllArgsConstructor
public class UserBulkRepository {

    private final ReactiveMongoTemplate mongoTemplate;

    public Mono<BulkWriteResult> save(Collection<UserDocument> users) {
        return mongoTemplate.getCollection("User")
                .flatMap(usersCollection -> {
                    val operations = toBulkUpdateOperations(users);
                    return Mono.from(usersCollection.bulkWrite(operations));
                });
    }

    public Mono<BulkWriteResult> delete(Collection<String> emails) {
        return mongoTemplate.getCollection("User")
                .flatMap(usersCollection -> {
                    val operations = new DeleteManyModel<Document>(new Document("_id", new Document("$in", emails)));
                    return Mono.from(usersCollection.bulkWrite(List.of(operations)));
                });
    }

    private List<UpdateOneModel<Document>> toBulkUpdateOperations(Collection<UserDocument> users) {
        return users.stream()
                .map(user -> {
                    Document documentToSave = new Document();
                    mongoTemplate.getConverter().write(user, documentToSave);
                    Document filter = new Document("_id", user.getEmail());
                    return toUpdateModel(filter, documentToSave);
                })
                .collect(toList());
    }

    private UpdateOneModel<Document> toUpdateModel(Document filter, Document documentToSave) {
        val updateOptions = new UpdateOptions().upsert(true);
        return new UpdateOneModel<>(filter, new Document("$set", documentToSave), updateOptions);
    }
}
