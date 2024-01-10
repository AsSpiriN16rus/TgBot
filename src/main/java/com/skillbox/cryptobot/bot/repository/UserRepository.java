package com.skillbox.cryptobot.bot.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository
{
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean userIsExists(String idUser){
        int userCount = this.jdbcTemplate.queryForObject("select count(*) from subscribers where id = ?", Integer.class, idUser);
        if (userCount == 0){
            return false;
        }else {
            return true;
        }
    }

    public void addUser(String idUser){
        this.jdbcTemplate.update(
                "insert into subscribers (id) values (?)",
                idUser);
    }

    public void addPrice(String idUser, String price){
        this.jdbcTemplate.update(
                "UPDATE subscribers SET price = ? where id = ?;",
                price,idUser);
    }

    public String getSubscribe(String idUser){
        String price = this.jdbcTemplate.queryForObject("select price from subscribers where id = ?;",
                String.class,
                idUser);
        return price;
    }

    public void delPrice(String idUser){
        this.jdbcTemplate.update(
                "UPDATE subscribers SET price = null where id = ?;",
                idUser);
    }
}
