package cyf.gradle.sharding.controller;

import cyf.gradle.dao.model.Record;
import cyf.gradle.dao.model.User;
import cyf.gradle.sharding.service.SeparateService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Cheng Yufei
 * @create 2019-08-16 11:09
 **/
@RestController
@RequestMapping("/sharing")
public class SeparateControlle {

    @Resource
    private SeparateService separateService;

    @GetMapping("/insert")
    public void insert(@RequestBody User user) {
        separateService.insert(user);
    }

    @GetMapping("/selectById/{uid}")
    public User selectById(@PathVariable Integer uid) {
        return separateService.getUserById(uid);
    }

    @GetMapping("/insertRecord")
    public void insertRecord(@RequestBody Record record) {
        separateService.insertRecord(record);
    }

    @GetMapping("/selectRecordById/{id}")
    public Record selectRecordById(@PathVariable Long id) {
        return separateService.getRecordById(id);
    }

    @GetMapping("/getRecordList")
    public List<Record> getRecordList(@RequestParam String name) {
        return separateService.getRecordList(name);
    }
}
