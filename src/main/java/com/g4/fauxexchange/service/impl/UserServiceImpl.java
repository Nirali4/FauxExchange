package com.g4.fauxexchange.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.g4.fauxexchange.model.User;
import com.g4.fauxexchange.model.LeaderboardInfo;
import com.g4.fauxexchange.model.Wallet;
import com.g4.fauxexchange.model.UserInfo;
import com.g4.fauxexchange.service.UserService;
import com.g4.fauxexchange.dao.UserRepository;


//Currency related
import com.g4.fauxexchange.model.Currency;
import com.g4.fauxexchange.model.Price;
import com.g4.fauxexchange.service.CurrencyService;
import com.g4.fauxexchange.dao.CurrencyRepository;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository uRepo;

    @Autowired
    private CurrencyRepository cRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /* Create User
    This function creates a user from a given data structure that will be passed in
    TODO: This needs to be extended for error checking */
    @Override
    public void createUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRole("GENERIC");
        user.createWallet();
        user.createFriendsList();
        uRepo.save(user);
        System.out.println("Created " + user);
    }

    /* Delete User
    This function will remove the user from the database */
    @Override
    public void deleteUser(User user) {
        uRepo.delete(user);
    }

    /* Update User
    This function runs through the user database and updates the wallets according to the current
    their current prices according to the currency stored in the database */
    @Override
    @Scheduled(fixedRate = 60000, initialDelay = 60000)
    public void updateUser() {
        System.out.println("- Updating Wallets -");
        for(User user : uRepo.findAll()) {
            if(!user.getWallets().isEmpty()) {
                for(Wallet wallet : user.getWallets()) {
                    if(!wallet.getCode().equals("AUD")) {
                        wallet.setPrice(cRepo.findByCode(wallet.getCode()).getRecentPrice());
                        wallet.setValue(wallet.getPrice() * wallet.getQty());   
                    }
                }
                uRepo.save(user);
                System.out.println("Updated: " + user.getUserId());
            }
        }
    }

    /* Save User
    This function is used to update/save any changes made to the given user */
    @Override
    public void saveUser(User user) {
        uRepo.save(user);
    }

    /* Get Users
    Return back all users in the database */
    @Override
    public List<User> getUsers() {
        return uRepo.findAll();
    }

    /* Get User By Email
    Return back a specific user given an email */
    @Override
    public User getUserByEmail(String email) {
        return uRepo.findByEmail(email);
    }

    /* Get User Info
    This function takes in a id. From this id fill out the session info so that it can be sent
    to the frontend */
    @Override
    public UserInfo getUserInfo(String id) {
        User user = uRepo.findByUserId(id);
        
        UserInfo ui = new UserInfo();
        ui.fName = user.getFName();
        ui.lName = user.getLName();
        ui.rank = "whatever";
        ui.cash = user.getWallet("AUD").getValue();
        return ui;
    }

    /* Get User Wallet
    Return back the wallets of the user */
    @Override
    public List<Wallet> getUserWallet(String id) {
        User user = uRepo.findByUserId(id);
        return user.getWallets();
    }

    /* Get Leaderboard
    This function returns back a data structure for the front end.
    It grabs all the users and then sorts by their total account value.
    This is then pushed towards the frontend to display */
    @Override
    public List<LeaderboardInfo> getLeaderboard() {
        List<User> users = uRepo.findAll();
        LinkedList<LeaderboardInfo> leaderboards = new LinkedList<LeaderboardInfo>();
        for(User u : users) {
            LeaderboardInfo person = new LeaderboardInfo(u.getFName() + " " + u.getLName(), 0, u.getWalletsValue());
            leaderboards.add(person);
        }

        Collections.sort(leaderboards, new Comparator<LeaderboardInfo>() { 
                @Override 
                public int compare(LeaderboardInfo s1, LeaderboardInfo s2) { 
                    if(s1.getValue() < s2.getValue()) {
                        return 1;
                    } else {
                        return -1;
                    }
                } 
            });

        int index = 0;
        for(LeaderboardInfo li : leaderboards) {
            li.setRank(index + 1);
            leaderboards.set(index, li);
            index++;
        }

        return leaderboards;
    }

    /* Add Friends 
    Add a friend given the User and the Friends email */
    @Override
    public void addFriends(String userId, String email) {
        System.out.println("UserServiceImpl");
        User user = uRepo.findByUserId(userId);
        user.addFriend(email);
        uRepo.save(user);
    }

    /* Get Friends
    Use the leaderboard info and return back a list of friends that are sorted as well */
    @Override
    public List<LeaderboardInfo> getFriends(String userId) {
        User user = uRepo.findByUserId(userId);
        List<LeaderboardInfo> leaderboard = getLeaderboard();
        LinkedList<LeaderboardInfo> friends = new LinkedList<LeaderboardInfo>();

        for(String email : user.getFriendsList()) {
            User f = uRepo.findByEmail(email);
            for(LeaderboardInfo li : leaderboard) {
                if(f != null) {
                    if(li.getName().equals(f.getFName() +" "+ f.getLName())) {
                        friends.add(li);
                    }
                }
            }
        }

        return friends;
    }

    /* Get User
    Return back the user */
    @Override
    public User getUser(String id) {
        return uRepo.findByUserId(id);
    }

}
