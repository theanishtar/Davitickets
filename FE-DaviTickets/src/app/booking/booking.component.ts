import { Component, OnInit } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { Router } from '@angular/router';
import { LoginService } from '../service/login.service';
import '../../assets/toast/main.js';
declare var toast: any;
@Component({
  selector: 'app-booking',
  templateUrl: './booking.component.html',
  styleUrls: ['./booking.component.css']
})
export class BookingComponent {
  userDisplayName = '';


  ngOnInit(): void {
    const container = document.querySelector(".container-booking");
    // const seats = document.querySelectorAll(".row .seat:not(.occupied)");
    // const count = document.getElementById("count");
    // const total = document.getElementById("total");
    // const movieSelect = document.getElementById("movie");

    container.addEventListener("click", (e) => {
      const target = e.target as HTMLTextAreaElement;
      if (
        target.classList.contains("seat") &&
        !target.classList.contains("occupied") &&
        !target.classList.contains("notselected")&&
        !target.classList.contains("your")
      ) {
        console.log("abc")
        target.classList.toggle("selected");
  
      }
    });
  }

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
