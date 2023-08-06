import { Injectable } from '@angular/core';

@Injectable()
export class AuthService {
	private token: string;

	setToken(token: string) {
		this.token = token;
	}

	getToken() {
		return this.token;
	}
}
