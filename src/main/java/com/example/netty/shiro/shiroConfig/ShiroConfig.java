package com.example.netty.shiro.shiroConfig;

import com.example.netty.shiro.realms.UserRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @program: netty
 * @description: Shiro的配置类
 * @author: 曹孙翔
 * @create: 2019-10-19 20:03
 **/
@Configuration
public class ShiroConfig {
    /**
     * 创建ShiroFilterFactoryBean
     */
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager securityManager) {
        System.out.println("ShiroConfiguration.shirFilter()");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //拦截器
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        //设置login URL
        shiroFilterFactoryBean.setLoginUrl("/login/login");
        //登入成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/a/LoginSuccess");
        //未授权的页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/");
        //src="jquery/jquery-3.2.1.min.js" 生效
        filterChainDefinitionMap.put("/jquery/*", "anon");
        filterChainDefinitionMap.put("/js/*", "anon");
        filterChainDefinitionMap.put("js/*", "anon");
        filterChainDefinitionMap.put("/payAlis", "anon");
        filterChainDefinitionMap.put("/WXpay", "anon");
        // 设置登录的URL为匿名访问，因为一开始没有用户验证
        filterChainDefinitionMap.put("/login/login", "anon");
        filterChainDefinitionMap.put("/Exception.class", "anon");
        // 我写的url一般都是xxx.action，根据你的情况自己修改
        filterChainDefinitionMap.put("/*", "authc");
        // 退出系统的过滤器
        filterChainDefinitionMap.put("/logout", "logout");
        // 现在资源的角色
        filterChainDefinitionMap.put("/admin.html", "roles[admin]");
        filterChainDefinitionMap.put("/user.html", "roles[user]");
        // 最后一班都，固定格式
        filterChainDefinitionMap.put("/**", "authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 创建DefaultWebSecurityManager
     */

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //关联realm
        securityManager.setRealm(userRealm);
        //注入缓存管理器
        securityManager.setCacheManager(ehCacheManager());
        return securityManager;
    }

    /**
     * 凭证匹配器（由于我们的密码校验交给Shiro的SimpeAuthenticationInfo进行处理了
     * 所以我们需要修改下doGetAuthenticationInfo的代码）
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        //散列算法：这里使用MD5算法；
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        // 散列的次数，比如散列两次，相当于md5(md5(""));
        hashedCredentialsMatcher.setHashIterations(1024);
        return hashedCredentialsMatcher;
    }

    /**
     * 创建Realm
     */
    @Bean(name = "userRealm")
    public UserRealm getUserRealm() {
       UserRealm userRealm= new UserRealm();
      // userRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return userRealm;
    }

    /**
     * shiro缓存管理器
     * 需要注入对应的其他实体类中--->安全管理器：securyManager可见securyManager是整个shiro的核心
     */
    @Bean
    public EhCacheManager ehCacheManager() {
        System.out.println("ShiroConfiguration.getEhCacheManager()");
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManagerConfigFile("classpath:ehcache.xml");
        return ehCacheManager;
    }

    /**
     * 开启shiro aop注解支持 使用代理方式；所以需要开启代码支持
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
            DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * DefaultAdvisorAutoProxyCreator，Spring的一个bean，由Advisor决定对哪些类的方法进行AOP代理。
     */
    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }
}