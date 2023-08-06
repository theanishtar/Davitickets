import { Injectable } from '@angular/core';
//Xử lí bất đồng bộ
import { Observable } from 'rxjs';
import { of } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { Router } from '@angular/router';

import { CookieService } from 'ngx-cookie-service';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  private profileURL = 'http://localhost:8080/profile/data';
  private updateProfileURL = 'http://localhost:8080/profile/update';

  // getProfile() : Observable<any>{
  //   let headers = new HttpHeaders({
  //     'Authorization': localStorage.getItem("token"),
  //     'Content-Type': 'application/json'
  //   });
  //   // alert(localStorage.getItem("token"))
  //   return this.http.get<any>(this.profileURL, { headers }).pipe(
  //     // tap(() => console.log("Lấy dữ liệu thành công")),
  //     tap(receivedUser => console.log(`receivedUser = ${JSON.stringify(receivedUser)}`)),
  //     catchError(error => of([]))
  //   )
  // }

  getProfile() {
    let headers = new HttpHeaders({
      'Authorization': localStorage.getItem("token"),
      'Content-Type': 'application/json'
    });
    return this.http.get<any>(this.profileURL, {headers}).pipe(
      // tap(() => console.log("Lấy dữ liệu thành công")),
      tap((receivedUser) =>
        // this.listSeat = JSON.parse(JSON.stringify(receivedUser))
        console.log(`receivedUser = ${JSON.stringify(receivedUser)}`)
      ),
      catchError((error) => of([]))
    );
  }

  updateProfile(data: any) {
    // return this.http.post(this.userURL, data);
    return this.http.post<any>(this.updateProfileURL, data).pipe(
      // tap(() => console.log("Lấy dữ liệu thành công")),
      tap((receivedUser) =>
        console.log(`receivedUser = ${JSON.stringify(receivedUser)}`)
      ),
      catchError((error) => of([]))
    );
  }
  constructor(
    private http: HttpClient,
    private cookieService: CookieService,
    private router: Router,
    private route: ActivatedRoute,
    private httpClient: HttpClient
  ) { }
}
