package com.rssp.demorssp.service;

import com.rssp.demorssp.models.UserDocument;
import com.rssp.demorssp.repository.UserBulkRepository;
import com.rssp.demorssp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserBulkRepository userBulkRepository;

    public Flux<UserDocument> getAllUsers() {
        return userRepository.findAll();
    }

    public Mono<UserDocument> addUser(UserDocument user) {
        return userRepository.save(user);
    }

    public Mono<Void> deleteUser(String email) {
        return userRepository.deleteById(email);
    }

    public Flux<UserDocument> saveAll(Set<UserDocument> users) {
        return userBulkRepository.save(users)
                .flatMapMany(bulkResult -> bulkResult.wasAcknowledged()
                        ? Flux.fromIterable(users)
                        : Flux.error(new Exception("Error while saving the users.")));
    }

    public Mono<Void> deleteMany(Set<String> emails) {
        return userBulkRepository.delete(emails)
                .flatMap(bulkResult -> bulkResult.wasAcknowledged()
                        ? Mono.empty()
                        : Mono.error(new Exception("Error while deleting some users.")));
    }
}
