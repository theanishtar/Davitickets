import { Observable, of } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class RegisterService {
  private userURL = 'http://localhost:8080/user/register';
  private userLoginUrl='http://localhost:8080/user/register_login';
  private userCheckCodeMail="http://localhost:8080/user/checkCodeMail";
  registerUser(data: any) {
    // return this.http.post(this.userURL, data);
    return this.http.post<any>(this.userURL, data).pipe(
      // tap(() => console.log("Lấy dữ liệu thành công")),
      tap((receivedUser) =>
        console.log(`receivedUser = ${JSON.stringify(receivedUser)}`)
      ),
      catchError((error) => of([]))
    );
  }

  registerAndLogin(): Observable<any>{
      return this.http.get<any>(this.userLoginUrl).pipe(
        // tap(() => console.log("Lấy dữ liệu thành công")),
        tap(receivedUser => console.log(`receivedUser = ${JSON.stringify(receivedUser)}`)),
        catchError(error => of([]))
      )
    }
    checkCodeMail(data: any) {
      // return this.http.post(this.userURL, data);
      return this.http.post<any>(this.userCheckCodeMail,data).pipe(
            // tap(() => console.log("Lấy dữ liệu thành công")),
            tap(receivedUser => console.log(`receivedUser = ${JSON.stringify(receivedUser)}`)),
            catchError(error => of([]))
          )
    }

  constructor(private http: HttpClient, private router: Router) {}
}
