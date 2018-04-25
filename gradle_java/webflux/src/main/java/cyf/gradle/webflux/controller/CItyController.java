package cyf.gradle.webflux.controller;

import cyf.gradle.webflux.domain.City;
import cyf.gradle.webflux.service.CityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Cheng Yufei
 * @create 2018-04-14 上午11:03
 **/
@RestController
@RequestMapping("/city")
public class CItyController {

    @Autowired
    private CityHandler cityHandler;

    @PostMapping
    public Mono save(@RequestBody City city) {

                return cityHandler.save(city);
        }

    @GetMapping
    public Flux<City> findAll() {

        return cityHandler.findAl();
    }

    @GetMapping("/{id}")
    public Mono<City> getById(@PathVariable long id) {

        return cityHandler.getById(id);
    }
}
