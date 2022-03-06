package com.rssp.demorssp.models;

import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Value
@Document("User")
public class UserDocument implements Serializable {
    @Id
    String email;
    String fullName;
}
