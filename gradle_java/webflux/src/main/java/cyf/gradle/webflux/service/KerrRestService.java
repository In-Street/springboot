package cyf.gradle.webflux.service;

import com.google.common.collect.Maps;
import cyf.gradle.base.model.Response;
import cyf.gradle.dao.mapper.KerrMapper;
import cyf.gradle.dao.model.Kerr;
import cyf.gradle.dao.model.KerrExample;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Cheng Yufei
 * @create 2018-04-08 16:01
 **/
@Service
@Slf4j
public class KerrRestService {

    @Autowired
    private KerrMapper kerrMapper;

    public Flux<Kerr> getKerrs() {
        KerrExample kerrExample = new KerrExample();
//        kerrExample.createCriteria().andVotesGreaterThan(30);
        List<Kerr> kerrs = kerrMapper.selectByExample(kerrExample);
        return Flux.create(sink -> {
            kerrs.forEach(kerr -> {
                sink.next(kerr);
            });
            sink.complete();
        });

    }

    public Mono<Response> getKerrById(Integer id) {
        Kerr kerr = kerrMapper.selectByPrimaryKey(id);
       return Mono.create(sink -> {
            sink.success(new Response(kerr));
        });
    }

    public Flux getKerrsByIds(Flux<Integer> ids) {
        Flux<Kerr> flux = ids.flatMap(id -> Mono.justOrEmpty(kerrMapper.selectByPrimaryKey(id)));

        Map map = Maps.newHashMap();
        flux.doOnNext(f -> {
            map.put(f.getId(), f);
        });
        System.out.println(map.size());
        return flux;
    }
}
