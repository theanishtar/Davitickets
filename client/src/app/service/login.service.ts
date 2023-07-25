import { Injectable, NgZone } from '@angular/core';

//Xử lí bất đồng bộ
import { Observable } from 'rxjs';
import { of } from 'rxjs';

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
  private userURL = 'http://localhost:8080/rest/login';
  private userURLGG = 'http://localhost:8080/rest/loginWithGG';

  loginUser(data: any) {
    
    // return this.http.post(this.userURL, data);
    return this.http.post<any>(this.userURL,data).pipe(
          // tap(() => console.log("Lấy dữ liệu thành công")),
          tap(receivedUser => console.log(`receivedUser = ${JSON.stringify(receivedUser)}`)),
          catchError(error => of([alert(error)]))
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
    private router: Router
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
}
