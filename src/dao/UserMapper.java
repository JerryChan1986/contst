package dao;

import java.util.List;

import model.User;


public interface UserMapper {
	void insert(User record);

    int insertSelective(User record);
    
    int deleteByPrimaryKey(String id);
    
    List<User> findUsers(User record);
        
    int checkExistUser(String name);
}
