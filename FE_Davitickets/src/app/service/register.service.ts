import { Observable, of } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
	providedIn: 'root',
})
export class RegisterService {
	private userURL = 'http://localhost:8080/oauth/register';
	private userCheckCodeMail = "http://localhost:8080/oauth/checkCodeMail";
	registerUser(data: any) {
		return this.http.post<any>(this.userURL, data).pipe(
			tap((receivedUser) =>
				console.log(`receivedUser = ${JSON.stringify(receivedUser)}`)
			),
			catchError((error) => of([]))
		);
	}
	checkCodeMail(data: any) {
		return this.http.post<any>(this.userCheckCodeMail, data).pipe(
			tap(receivedUser => console.log(`receivedUser = ${JSON.stringify(receivedUser)}`)),
			catchError(error => of([]))
		)
	}

	constructor(private http: HttpClient, private router: Router) { }
}
