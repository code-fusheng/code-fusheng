package xyz.fusheng.project.core.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.fusheng.project.common.annotation.Metrics;
import xyz.fusheng.project.common.exception.BusinessException;
import xyz.fusheng.project.core.mapper.UserMapper;
import xyz.fusheng.project.core.service.ITestService;
import xyz.fusheng.project.model.po.SysUser;

import javax.annotation.Resource;

/**
 * @FileName: TestServiceImpl
 * @Author: code-fusheng
 * @Date: 2022/5/6 22:57
 * @Version: 1.0
 * @Description:
 */

@Service
public class TestServiceImpl implements ITestService {

    @Resource
    private UserMapper userMapper;

    @Override
    @Metrics(ignoreException = true) // step2 改动点2
    @Transactional(rollbackFor = Exception.class)
    public void createUser(SysUser sysUser) {
        userMapper.insert(sysUser);
        if (sysUser.getUsername().contains("test")) {
            throw new BusinessException("用户名校验失败!");
        }
    }
}
