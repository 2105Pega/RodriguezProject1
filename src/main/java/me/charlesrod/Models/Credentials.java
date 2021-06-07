package me.charlesrod.Models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
public class Credentials implements Serializable {
    @Getter
    @Setter
	private String username;
    @Getter
    @Setter
    private String password;
}
