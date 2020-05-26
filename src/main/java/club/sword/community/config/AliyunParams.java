package club.sword.community.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by XuCao on 2020/5/18
 */
/*
* @component （把普通pojo实例化到spring容器中）
* 泛指各种组件，就是说当我们的类不属于各种归类的时候
* （不属于@Controller、@Services等的时候），我们就可以使用@Component来标注这个类。
*
*/
@Component
@ConfigurationProperties(prefix = "aliyun")
@Data
public class AliyunParams {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String firstKey; //?
}
