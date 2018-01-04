package cyf.gradle.api.service.interfaces;


import cyf.gradle.dao.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserForPageRepository extends PagingAndSortingRepository<User,Integer> {
}
