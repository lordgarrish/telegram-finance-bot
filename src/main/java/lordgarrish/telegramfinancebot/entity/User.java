package lordgarrish.telegramfinancebot.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@NoArgsConstructor
@Getter
@Entity
@Table(name="users")
public class User {

    @Id
    @Column(name = "chat_id")
    private Long chatId;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "tg_username")
    private String tgUsername;
    @Column(name = "registered_at", updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private Instant registeredAt;

    public User(Long chatId, String firstName, String lastName, String tgUsername) {
        this.chatId = chatId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.tgUsername = tgUsername;
    }

    @Override
    public String toString() {
        return "User{" +
                "Chat ID = " + chatId +
                ", Name = '" + firstName + " " + lastName + '\'' +
                ", tg_Username = '" + tgUsername + '\'' +
                '}';
    }
}
