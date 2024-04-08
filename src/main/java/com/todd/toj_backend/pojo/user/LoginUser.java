package com.todd.toj_backend.pojo.user;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser implements UserDetails {

    private User user;

    @JSONField(serialize = false)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for(int i = 0; i <= user.getRole(); i++){
            String[] roleList = UserRoleList.getUserRoleArray();
            authorities.add(new SimpleGrantedAuthority(roleList[i]));
        }
        return authorities; //该用户有哪些权限
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {//帐号是不是没有过期
        return true;
    }

    @Override
    public boolean isAccountNonLocked() { //是不是没有被锁定
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() { //凭证是不是没有过期
        return true;
    }

    @Override
    public boolean isEnabled() {  //是否可用
        return true;
    }
}
