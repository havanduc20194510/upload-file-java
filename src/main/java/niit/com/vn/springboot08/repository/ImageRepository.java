package niit.com.vn.springboot08.repository;

import niit.com.vn.springboot08.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

}
