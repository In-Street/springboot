package cyf.gradle.interview.service.transactional;

import cyf.gradle.dao.mapper.RegionMapper;
import cyf.gradle.dao.model.Region;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author Cheng Yufei
 * @create 2019-06-28 17:12
 **/
@Service
@Slf4j
public class RegionService {

    @Resource
    private RegionMapper regionMapper;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addRegion() throws Exception {

        Region region = new Region();
        region.name("北京市");
        regionMapper.insert(region);
        throw new Exception();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void addRegionRequiresNew() throws Exception {

        Region region = new Region();
        region.name("北京市");
        regionMapper.insert(region);
        throw new Exception();
    }


    @Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
    public void addRegionNested() throws Exception {

        Region region = new Region();
        region.name("北京市");
        regionMapper.insert(region);
        throw new Exception();
    }
}
