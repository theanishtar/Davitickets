import { Injectable, NgZone } from '@angular/core';

//Xử lí bất đồng bộ
import { Observable } from 'rxjs';
import { of } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { Router } from '@angular/router';

import { CookieService } from 'ngx-cookie-service';
import Swal from 'sweetalert2';
import '../../assets/toast/main.js';
declare var toast: any;
@Injectable({
  providedIn: 'root',
})
export class LoginService {
  private userURL = 'http://localhost:8080/oauth/login';
  private userURLGG = 'http://localhost:8080/rest/loginWithGG';
  private userCheckCodeMail="http://localhost:8080/rest/checkCodeMail";
  private userLoginWithGG = "http://localhost:8080/login/oauth/authenticated";
  // private userLoginAuth = "http://localhost:8080/login/oauth/authenticated";
  private userLoginAuth = "http://localhost:8080/oauth/login/authenticated"
  private userLogin = "http://localhost:8080/login";
  private userLogined: any[] = [];
  // loginUser(data: any) {
  //   // return this.http.post(this.userURL, data);
  //   return this.http.post<any>(this.userURL,data).pipe(
  //         // tap(() => console.log("Lấy dữ liệu thành công")),
  //         tap(receivedUser => console.log(`receivedUser = ${JSON.stringify(receivedUser)}`)),
  //         catchError(error => of([alert(error)]))
  //       )
  // }

  loginUser(data: any) {
    // return this.http.post(this.userURL, data);
    return this.http.post<any>(this.userURL,data).pipe(
          // tap(() => console.log("Lấy dữ liệu thành công")),
        // tap(receivedUser => console.log(`receivedUser = ${JSON.stringify(receivedUser)}`)),
          tap(receivedUser => {
            this.userLogined = JSON.parse(JSON.stringify(receivedUser));
            this.setUserLog(this.userLogined);
            console.warn(this.userLogined)
            localStorage.setItem("token", JSON.parse(JSON.stringify(this.getUserLog())).token);
          }),
          catchError(error => of([alert(error)]))
        )
  }

  log(data: any) {
    // return this.http.post(this.userURL, data);
    return this.http.post<any>(this.userLogin,data).pipe(
          // tap(() => console.log("Lấy dữ liệu thành công")),
          tap(receivedUser => console.log(`receivedUser = ${JSON.stringify(receivedUser)}`)),
          catchError(error => of([alert(error)]))
        )
  }

  loginWithGoogle(data: any) {
    // return this.http.post(this.userURL, data);
    return this.http.post<any>(this.userLoginWithGG,data).pipe(
          tap(() => console.log("Thành công")),
          // tap(receivedUser => console.log(`receivedUser = ${JSON.stringify(receivedUser)}`)),
          catchError(error => of([]))
        )
  }

  loginAuth(data: any) {
    //alert("dataparsms:"+data);
    var strData = {"data":data };
    // return this.http.post(this.userURL, data);
    return this.http.post<any>(this.userLoginAuth,strData).pipe(
          tap((res) => console.log("Thành công"+res)),
          // tap(receivedUser => console.log(`receivedUser = ${JSON.stringify(receivedUser)}`)),
          catchError(error => of([]))
        )
  }

  loginWithGG(data: any) {
    // return this.http.post(this.userURL, data);
    return this.http.post<any>(this.userURLGG,data).pipe(
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

  // getAllUsers() : Observable<any>{
  //   return this.http.get<any>(this.userURL).pipe(
  //     // tap(() => console.log("Lấy dữ liệu thành công")),
  //     tap(receivedUser => console.log(`receivedUser = ${JSON.stringify(receivedUser)}`)),
  //     catchError(error => of([]))
  //   )
  // }

  constructor(
    private http: HttpClient,
    private cookieService: CookieService,
    private router: Router,
    private route: ActivatedRoute,
    private httpClient: HttpClient
  ) {}

  isLogin(): boolean {
    if (this.cookieService.get('isUserLoggedIn') == '') {
      return false;
    }
    return true;
  }

  logout(): void {
    function delay(ms: number) {
      return new Promise(function (resolve) {
        setTimeout(resolve, ms);
      });
    }
    Swal.fire({
      title: 'Bạn muốn đăng xuất?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Yes',
      cancelButtonText: 'No',
    }).then((result) => {
      if (result.value) {
        delay(1).then((res) => {
          this.cookieService.deleteAll();
          this.router.navigate(['home']);
          new toast({
            title: 'Đã đăng xuất!',
            message: 'Hẹn gặp lại',
            type: 'warning',
            duration: 2000,
        });
          // delay(1).then((_) => {
          //   location.reload();
          // });
        });
      }
    });
  }

  // Getter
  getUserLog(): any[] {
    return this.userLogined;
}

//   Setter
setUserLog(data: any[]): void {
    this.userLogined = data;
}
}
