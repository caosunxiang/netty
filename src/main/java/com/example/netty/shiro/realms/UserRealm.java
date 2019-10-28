package com.example.netty.shiro.realms;

import com.example.netty.mapper.UserMapper;
import com.example.netty.pojo.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * @program: netty
 * @description: 自定义
 * @author: 曹孙翔
 * @create: 2019-10-19 20:06
 **/
public class UserRealm extends AuthorizingRealm {
    @Autowired
    private UserMapper userMapper;
    /**
     * 执行授权
     * */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行授权");
        System.out.println("MyShiroRealm的doGetAuthorizationInfo授权方法执行");
        // User user=(User)
        // principals.fromRealm(this.getClass().getName()).iterator().next();//获取session中的用户
        // System.out.println("在MyShiroRealm中AuthorizationInfo（授权）方法中从session中获取的user对象:"+user);

        // 从PrincipalCollection中获得用户信息
        Object principal=principalCollection.getPrimaryPrincipal();
        System.out.println("ShiroRealm  AuthorizationInfo:"+principal.toString());
        //根据用户名来查询数据库赋予用户角色，权限（查数据库）
        Set<String> roles=new HashSet<>();
        Set<String> permissions=new HashSet<>();
        //个体用户添加user权限（给所有的用户添加user权限）
        if("user".equals(principal)){
            roles.add("user");
            permissions.add("user:query");
        }
        //当用户名为admin时 为用户添加权限admin 两个 admin可以理解为两个字段
        if ("曹孙翔".equals(principal)){
            roles.add("admin");
            permissions.add("admin:query");
        }
        //为用户添加visit游客权限，在url中没有visit权限，所以，所有的操作都没有权限
        if ("visit".equals(principal)){
            roles.add("visit");
            permissions.add("visit:query");
        }
        //更新以上代码
        SimpleAuthorizationInfo info =new SimpleAuthorizationInfo(roles);
        //添加权限
        info.setStringPermissions(permissions);
        return info;
    }

    /**
    * 执行认证
     *方便用于加密参数 ：AuthenticationtToken是从表单穿过封装好的对象
    * */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("执行认证");
        System.out.println("authenticationToken："+authenticationToken);
        //讲authenticationToken强转为authenticationToken对象
        UsernamePasswordToken passwordToken=(UsernamePasswordToken)authenticationToken;
        //获得从表单传过来的用户名
        String username=passwordToken.getUsername();
        System.out.println(username);
        //从数据库查看用户的是否存在
        User user=userMapper.getUser(username);
        //不存在抛出异常
        if (user==null){
            throw new UnknownAccountException("无此用户");
        }
        //认证的实体信息，可以是username，也可以是用户的实体类对象，这里用的是用户名
        Object principal=username;
        //从数据库查询的密码
        Object credentials=user.getPwd();
        //颜值加密的颜 可以用用户名
        ByteSource credentialsSalt=ByteSource.Util.bytes(username);
        //当前realm对象的名称，调用分类的getName()
        String realName=this.getName();
        //创建SimpleAuthenticationInfo对象，并且把username和password等信息封装到里面
        //用户密码的对比是shiro帮我们完成
        SimpleAuthenticationInfo info=null;
        info=new SimpleAuthenticationInfo(principal,credentials,credentialsSalt,realName);
        return info;
    }
}
