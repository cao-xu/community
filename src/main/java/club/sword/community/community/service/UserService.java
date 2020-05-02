package club.sword.community.community.service;

import club.sword.community.community.mapper.UserMapper;
import club.sword.community.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public void createOrUpdate(User user) {
        //判断是否已经有该用户
        //若有，则更新用户的信息
        //若无，则插入用户信息
        User dbUser = userMapper.findByAccountId(user.getAccountId());
        if (dbUser == null){
            //插入
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }
        else {
            //更新
            dbUser.setGmtModified(System.currentTimeMillis());
            dbUser.setName(user.getName());
            dbUser.setAvatarUrl(user.getAvatarUrl());
            dbUser.setToken(user.getToken());
            userMapper.updateByAccountId(dbUser);
        }
    }
}
