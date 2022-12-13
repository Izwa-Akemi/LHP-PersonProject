package blog.example.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.*;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name="account")
public class UserEntity {
	@Id
	@Column(name="user_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long userId;

	@NonNull
	@Column(name="user_name")
	private String userName;
	
	@NonNull
	@Column(name="user_email")
	private String userEmail;

	@NonNull
	@Column(name="password")
	private String password;
}
