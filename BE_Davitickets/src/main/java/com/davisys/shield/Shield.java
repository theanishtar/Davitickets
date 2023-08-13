package com.davisys.shield;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shield {
	String email;
	String ip;
	Long time;
	int countErr;
	
}
