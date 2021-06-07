package me.charlesrod.Models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Table(name="roles")
public class Roles implements Serializable {
	@Id
	@GeneratedValue
	@Getter @Setter private int id;
	@Getter @Setter private String title;
	@Override
	public String toString() {
		return "Roles [id=" + id + ", title=" + title + "]";
	}
	public Roles(String title) {
		this.title = title;
	}
}
