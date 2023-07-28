import { Component, OnInit } from '@angular/core';
import * as $ from "jquery";
import { CookieService } from 'ngx-cookie-service';
import { Router } from '@angular/router';
import { LoginComponent } from '../login/login.component';
import { LoginService } from '../service/login.service';
import '../../assets/toast/main.js';
declare var toast: any;
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: [
    `./home.component.css`,
  ]
})
export class HomeComponent {
  userDisplayName = '';
check:boolean = false;
  ngOnInit(): void {
    
    /* click-scroll nav index */
    var sectionArray = [1, 2, 3, 4, 5, 6];

    $.each(sectionArray, function (index, value) {

      $(document).scroll(function () {
        var offsetSection = $('#' + 'section_' + value).offset().top - 83;
        var docScroll = $(document).scrollTop();
        var docScroll1 = docScroll + 1;


        if (docScroll1 >= offsetSection) {
          $('.navbar-nav .nav-item .nav-link').removeClass('active');
          $('.navbar-nav .nav-item .nav-link:link').addClass('inactive');
          $('.navbar-nav .nav-item .nav-link').eq(index).addClass('active');
          $('.navbar-nav .nav-item .nav-link').eq(index).removeClass('inactive');
        }

      });

      $('.click-scroll').eq(index).click(function (e) {
        var offsetClick = $('#' + 'section_' + value).offset()!.top - 83;
        e.preventDefault();
        $('html, body').animate({
          'scrollTop': offsetClick
        }, 300)
      });

    });

    $(document).ready(function () {
      $('.navbar-nav .nav-item .nav-link:link').addClass('inactive');
      $('.navbar-nav .nav-item .nav-link').eq(0).addClass('active');
      $('.navbar-nav .nav-item .nav-link:link').eq(0).removeClass('inactive');
    });
    /* end click-scroll nav index */
  }

  constructor(
    private cookieService: CookieService,
    private loginService: LoginService,
    private router: Router,
  ) {   
    this.userDisplayName = this.cookieService.get('full_name');  
  }

  isLogin(){
    return this.loginService.isLogin();
  }

  logout(){
    return this.loginService.logout();
  }

  checkBooking(){
    if(this.cookieService.get('isUserLoggedIn') != ''){
      this.router.navigate(['listMovieBooking']);
    }else{
      new toast({
        title: 'Thông báo!',
        message: 'Vui lòng đăng nhập để có thể đặt vé!',
        type: 'info',
        duration: 2000,
      });
    }
  }
}
