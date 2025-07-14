
package com.kalyan.url.shortner.Repository;
import com.kalyan.url.shortner.Model.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlMappingRepository extends JpaRepository<UrlMapping, String> {
}
