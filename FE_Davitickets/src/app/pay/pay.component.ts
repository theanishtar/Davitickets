import { Component } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { Router } from '@angular/router';
import { LoginService } from '../service/login.service';
import { ListMovieService } from '../service/list-movie.service';
import '../../assets/toast/main.js';
import { DomSanitizer } from '@angular/platform-browser';
import { Location } from '@angular/common';
import { MyappService } from '../service/myapp.service';
declare var toast: any;
@Component({
  selector: 'app-pay',
  templateUrl: './pay.component.html',
  styleUrls: ['./pay.component.css']
})
export class PayComponent {
  userDisplayName = '';
  formPay: any[] = [];
  count: number = 0;
  money: number = 0;
  seat: string;
  imagePath: any;

  constructor(
    private cookieService: CookieService,
    private loginService: LoginService,
    private router: Router,
    private listMovieService: ListMovieService,
    private _sanitizer: DomSanitizer,
    private location: Location,
    private myappService: MyappService
  ) {
    this.userDisplayName = this.cookieService.get('full_name');
    let idMovie = Number.parseInt(this.cookieService.get("idMovie"));
    let idShowTime = Number.parseInt(this.cookieService.get("idShowTime"));
    // if (this.cookieService.get("dataBooking") != null) {
    //   // this.listMovieService.onClickLoadBooking(idMovie, idShowTime);
    //   // this.cookieService.set("dataBooking", null);
    //   this.location.back();
    // }
    this.cookieService.set("dataBooking", null);
    console.warn(JSON.parse("[" + JSON.stringify(this.listMovieService.getListShowTime()) + "]"));
    this.formPay = JSON.parse("[" + JSON.stringify(this.listMovieService.getListShowTime()) + "]");
    this.count = this.listMovieService.getArrayDetailTicket().length;
    this.money = this.listMovieService.getTotal();
    this.seat = this.listMovieService.getArrayDetailTicket().toString();
    this.imagePath = this._sanitizer.bypassSecurityTrustResourceUrl('data:image/jpg;base64,' + this.formPay[0].qrCodeGeneratorService);
  }

  payment(){
    let userEmail = this.cookieService.get('userEmail');
    var data = {"money": this.money,"showtimeId":this.formPay[0].showtime.showtime_id,"seat":this.seat,"userEmail":userEmail,"qrCode":this.formPay[0].qrCodeGeneratorService};
    this.listMovieService.loadFormPayment( data).subscribe(res=>{
      window.location.href=""+res;
    })
  }

  isLogin() {
    return this.loginService.isLogin();
  }

  logout() {
    return this.loginService.logout();
  }

  loadDataProfile(){
    return this.myappService.loadDataProfile();
  }
}
