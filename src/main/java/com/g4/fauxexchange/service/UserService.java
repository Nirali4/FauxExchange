package com.g4.fauxexchange.service;

import java.util.List;
import com.g4.fauxexchange.model.User;
import com.g4.fauxexchange.model.UserInfo;
import com.g4.fauxexchange.model.Wallet;
import com.g4.fauxexchange.model.LeaderboardInfo;

public interface UserService {
    public abstract void createUser(User user);
    public abstract void deleteUser(User user);
    public abstract void updateUser();
    public abstract List<User> getUsers();
    public abstract User getUserByEmail(String email);
    public abstract UserInfo getUserInfo(String id);
    public abstract List<Wallet> getUserWallet(String id);
    public abstract void saveUser(User user);
    public abstract List<LeaderboardInfo> getLeaderboard();
    public abstract void addFriends(String userId, String email);
    public abstract List<LeaderboardInfo> getFriends(String userId);
    public abstract User getUser(String id);
}
