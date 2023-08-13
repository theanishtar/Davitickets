import { tap, catchError } from 'rxjs/operators';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ForgotpasswordService {
  private userURL = 'http://localhost:8080/oauth/forgotpassword';
  private userURLCheckCode = 'http://localhost:8080/oauth/checkForgotpassword';
  private userURLChangePassword = 'http://localhost:8080/user/changePassword';

  sendMail(data: any) {
    // return this.http.post(this.userURL, data);
    return this.http.post<any>(this.userURL, data).pipe(
      // tap(() => console.log("Lấy dữ liệu thành công")),
      tap((receivedUser) =>
        console.log(`receivedUser = ${JSON.stringify(receivedUser)}`)
      ),
      catchError((error) => of([]))
    );
  }

  checkCodeMail(data: any) {
    // return this.http.post(this.userURL, data);
    return this.http.post<any>(this.userURLCheckCode, data).pipe(
      // tap(() => console.log("Lấy dữ liệu thành công")),
      tap((receivedUser) =>
        console.log(`receivedUser = ${JSON.stringify(receivedUser)}`)
      ),
      catchError((error) => of([]))
    );
  }
  changePassword(data: any) {
    // return this.http.post(this.userURL, data);
    return this.http.post<any>(this.userURLChangePassword, data).pipe(
      // tap(() => console.log("Lấy dữ liệu thành công")),
      tap((receivedUser) =>
        console.log(`receivedUser = ${JSON.stringify(receivedUser)}`)
      ),
      catchError((error) => of([]))
    );
  }

  constructor(private http: HttpClient) {}
}
