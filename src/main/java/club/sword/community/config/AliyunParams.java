package club.sword.community.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by XuCao on 2020/5/18
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
