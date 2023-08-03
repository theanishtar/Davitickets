import { Component } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { Router } from '@angular/router';
import { LoginService } from '../service/login.service';
import '../../assets/toast/main.js';
declare var toast: any;
@Component({
  selector: 'app-pay',
  templateUrl: './pay.component.html',
  styleUrls: ['./pay.component.css']
})
export class PayComponent {
  userDisplayName = '';

  constructor(
    private cookieService: CookieService,
    private loginService: LoginService,
    private router: Router,
  ){
    this.userDisplayName = this.cookieService.get('full_name');  
  }

  isLogin(){
    return this.loginService.isLogin();
  }

  logout(){
    return this.loginService.logout();
  }
}
