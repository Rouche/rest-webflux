package com.resolutech.restwebflux.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 */
@Data
@Builder
@EqualsAndHashCode(of={"id"})
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Document
public class Vendor {

    @Id
    private String id;

    private String firstName;
    private String lastName;
}
