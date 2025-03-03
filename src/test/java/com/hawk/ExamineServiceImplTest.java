package com.hawk;

import com.hawk.biz.entity.Examine;
import com.hawk.biz.service.ExamineService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2025-02-28 10:01
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class ExamineServiceImplTest {

    @Resource
    private ExamineService examineService;

    @Test
    public void testInsert(){
        Examine examine = new Examine();
        examine.setName("王朝");
        examine.setGender(String.valueOf(1));
        examine.setBirthday("1993-10-30");
        examine.setCardType(String.valueOf(1));
        examine.setIdCard("10102199310301231");
        examine.setMobile("13123458769");
        examineService.insert(examine);
    }

    @Test
    public void testInsert2(){
        Examine examine = new Examine();
        examine.setName("马汉");
        examine.setGender(String.valueOf(1));
        examine.setBirthday("1992-11-30");
        examine.setCardType(String.valueOf(1));
        examine.setIdCard("10102199211301231");
        examine.setMobile("18823458769");
        examineService.insert(examine);
    }
}
