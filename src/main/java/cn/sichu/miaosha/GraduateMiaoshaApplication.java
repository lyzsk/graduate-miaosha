package cn.sichu.miaosha;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.sichu.miaosha.dao.UserDOMapper;
import cn.sichu.miaosha.dataobject.UserDO;

/**
 * "@EnableAutoConfiguration" 把启动类当成自动化支持配置的Bean，能够开启工程基于Spring的配置
 * <p>
 * SpringMVC解决Web控制层的问题
 * 
 * @author sichu
 * @date 2022/04/12
 */
// @EnableAutoConfiguration
@SpringBootApplication(scanBasePackages = {"cn.sichu.miaosha"})
@RestController
@MapperScan("cn.sichu.miaosha.dao")
public class GraduateMiaoshaApplication {

    @Autowired
    private UserDOMapper userDOMapper;

    /**
     * requestmapping写在main方法里用于测试
     * 
     * @return
     */
    @RequestMapping("/")
    public String home() {
        UserDO userDO = userDOMapper.selectByPrimaryKey(1);
        if (userDO == null) {
            return "用户不存在";
        } else {
            return userDO.getName();
        }
    }

    public static void main(String[] args) {
        System.out.println("Hello World");
        SpringApplication.run(GraduateMiaoshaApplication.class, args);
    }

}
