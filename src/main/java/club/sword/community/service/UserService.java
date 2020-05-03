package club.sword.community.service;

import club.sword.community.mapper.UserMapper;
import club.sword.community.model.User;
import club.sword.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public void createOrUpdate(User user) {
        //判断是否已经有该用户
        //若有，则更新用户的信息
        //若无，则插入用户信息
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(userExample);
        if (users.size() == 0){
            //插入
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }
        else {
            //更新
            User dbUser = users.get(0);
            User updateUser = new User();
            updateUser.setGmtModified(System.currentTimeMillis());
            updateUser.setName(user.getName());
            updateUser.setAvatarUrl(user.getAvatarUrl());
            updateUser.setToken(user.getToken());
            //设置SQL语句
            UserExample example = new UserExample();
            example.createCriteria().andIdEqualTo(dbUser.getId());
            //只更新部分内容，不更新创建时间
            userMapper.updateByExampleSelective(updateUser, example);
        }
    }
}
