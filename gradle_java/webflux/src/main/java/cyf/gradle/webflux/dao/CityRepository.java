package cyf.gradle.webflux.dao;

import cyf.gradle.webflux.domain.City;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Cheng Yufei
 * @create 2018-04-14 上午11:04
 **/
@Repository
public class CityRepository {

    private ConcurrentMap<Long, City> map = new ConcurrentHashMap();

    private static final AtomicLong idGenerator = new AtomicLong(0);


    public long save(City city) {

        long id = idGenerator.incrementAndGet();
        city.setId(id);
        map.put(id, city);
        return id;
    }
    
    public  Collection<City> findAll() {
        return map.values();
    }

    public City getById(long id) {
        return map.get(id);
    }
    
}
