package com.example.stylespo.model;

import androidx.lifecycle.LiveData;

import com.example.stylespo.model.UserAccount;

import java.util.List;


public interface UserAccountDao {

    LiveData<List<UserAccount>> getAllUserAccounts();

    LiveData<UserAccount> findByName(String name, String password);

    void insert(UserAccount userAccount);

    void update(UserAccount... userAccount);

    void delete(UserAccount... userAccount);
}
