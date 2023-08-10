import { Component } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { Router } from '@angular/router';
import { LoginService } from '../service/login.service';
import '../../assets/toast/main.js';
import { catchError, map, tap } from 'rxjs/operators';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { of } from 'rxjs';
import { MyappService } from '../service/myapp.service';
declare var toast: any;
@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.css']
})
export class HistoryComponent {
  userDisplayName = '';
  items: any[] = [];
  ngOnInit() {
    this.loadDataHistory();
  }
  constructor(
    private cookieService: CookieService,
    private loginService: LoginService,
    private router: Router,
    private http: HttpClient,
    private myappService: MyappService
  ){
    this.userDisplayName = this.cookieService.get('full_name'); 

  }

  loadDataHistory() {
    const historyURL = 'http://localhost:8080/history/data';
    let headers = new HttpHeaders({
      'Authorization': localStorage.getItem("token"),
      'Content-Type': 'application/json'
    });

    this.http.get<any[]>(historyURL, {headers}).subscribe(items => {
      this.items = items;
      console.log(this.items);
    });
  }

  isLogin(){
    return this.loginService.isLogin();
  }

  logout(){
    return this.loginService.logout();
  }

  loadDataProfile(){
    return this.myappService.loadDataProfile();
  }
}
