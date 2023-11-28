package com.kaskademindbank.service.impl;

import com.kaskademindbank.entity.Users;
import com.kaskademindbank.mapper.UsersMapper;
import com.kaskademindbank.service.IUsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ZiyuanZhou
 * @since 2023-11-28
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {

}
