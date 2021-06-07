//package me.charlesrod.Models;
//
//import java.io.Serializable;
//import java.util.Objects;
//
//import javax.persistence.EmbeddedId;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import javax.persistence.IdClass;
//import javax.persistence.ManyToOne;
//
//import org.hibernate.annotations.DynamicInsert;
//import org.hibernate.annotations.DynamicUpdate;
//
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import lombok.ToString;
//

//@Entity
//@IdClass(User.class)
//@IdClass(Account.class)
// class CustomerAccount implements Serializable{
//	@Id
//	@ManyToOne
//    @Getter @Setter private User user;
//	@Id
//	@ManyToOne
//    @Getter @Setter private Account account;
//	
//    CustomerAccount(User user,Account account){
//        this.user = user;	
//        this.account = account;
//    }
//    CustomerAccount(){	
//    }
//    @Override
//    public boolean equals(Object o) {
//    	if (this == o) {
//    		return true;
//    	}
//    	if (o==null || getClass() != o.getClass()) {
//    		return false;
//    	}
//    	CustomerAccount custacc = (CustomerAccount) o;
//    	return Objects.equals(user, custacc.user) && Objects.equals(account, custacc.account);
//    }
//}
