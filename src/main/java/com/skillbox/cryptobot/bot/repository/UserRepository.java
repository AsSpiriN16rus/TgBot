package com.skillbox.cryptobot.bot.repository;

import com.skillbox.cryptobot.bot.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;

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

    public void addUser(String idUser, String uuid){
        this.jdbcTemplate.update(
                "insert into subscribers (id,uuid) values (?,?)",
                idUser,uuid);
    }

    public void addPrice(String idUser, Integer price){
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

    public List<User> getUserNotification(Double price){
        return this.jdbcTemplate.query("select * from subscribers where price > " + price + "", UserRowMapper);
    }

    private final RowMapper<User> UserRowMapper = (resultSet, rowNum) -> {
        User user = new User();
        user.setUuid(resultSet.getString("uuid"));
        user.setId(resultSet.getString("id"));
        user.setPrice(resultSet.getBigDecimal("price"));
        user.setLast_notification(resultSet.getString("last_notification"));
        return user;
    };

    public void updateLastNotification(String idUser,String time){
        this.jdbcTemplate.update(
                "UPDATE subscribers SET last_notification = ? where id = ?;",
                time,idUser);
    }
}
