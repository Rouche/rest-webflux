package com.resolutech.restwebflux.repositories;

import com.resolutech.restwebflux.domain.Vendor;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * Created by jt on 9/24/17.
 */
public interface VendorRepository extends ReactiveMongoRepository<Vendor, String> {
}
