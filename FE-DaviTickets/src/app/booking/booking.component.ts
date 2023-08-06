import { Component, OnInit, HostListener, Input } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { Router } from '@angular/router';
import { LoginService } from '../service/login.service';
import { ListMovieService } from '../service/list-movie.service';
import '../../assets/toast/main.js';
import { JsonPipe } from '@angular/common';
import { getJSON } from 'jquery';
import { list } from '@angular/fire/storage';
declare var toast: any;
@Component({
  selector: 'app-booking',
  templateUrl: './booking.component.html',
  styleUrls: ['./booking.component.css']
})
export class BookingComponent {
  userDisplayName = '';
  divSeats: string[] = [];
  listSeat: any[] = [];
  json: any[] = [];
  regularSeatPrice: number = 0;
  vipSeatPrice: number = 0;
  checkBooking: boolean = false;

  ngOnInit(): void {
    // DOM elements
    const container: HTMLElement | null = document.querySelector(".container-booking");
    const seats: NodeListOf<Element> = document.querySelectorAll(".row .seat:not(.occupied)" || ".row .seat:not(.notselected)" || ".row .seat:not(.your)");
    const count: HTMLElement | null = document.getElementById("count");
    const total: HTMLElement | null = document.getElementById("total");



    // Seat prices
    // const regularPrice = this.listSeat.some(listSeatAll => this.regularSeatPrice = listSeatAll.showtime.price);
    // const vipPrice = this.listSeat.some(listSeatAll => this.vipSeatPrice = listSeatAll.showtime.price + 10000);
    const regularPrice: number = this.regularSeatPrice;
    const vipPrice: number = this.vipSeatPrice;
    // populateUI();

    // Click event on seat
    if (container) {
      container.addEventListener("click", (e) => {
        const target = e.target as HTMLElement;

        if (target.classList.contains("seat") && !target.classList.contains("occupied") && !target.classList.contains("notselected") && !target.classList.contains("your")) {
          target.classList.toggle("selected");
          updateSelectedCount();
        }

        //Thêm tên ghế vào mảng khi click
        if (target.classList.contains("seat") && !target.classList.contains("occupied") && !target.classList.contains("notselected") && !target.classList.contains("your")) {
          if (!this.divSeats.includes(target.innerText)) {
            this.divSeats.push(target.innerText);
          } else {
            //filter() để lọc ra các giá trị khác target.innerText
            this.divSeats = this.divSeats.filter(item => item !== target.innerText);
          }
        }

        if (this.divSeats.length > 0) {
          this.checkBooking = true;
        } else {
          this.checkBooking = false;
        }
      });
    }

    // Update selected count and total price
    function updateSelectedCount(): void {

      // alert(this.regularSeatPrice+10000)
      const selectedSeats: Array<Element> = Array.from(document.querySelectorAll(".row-seat .seat:not(.vip).selected"));
      const selectedVipSeats: Array<Element> = Array.from(document.querySelectorAll(".seat.vip.selected"));

      const seatsIndex: number[] = selectedSeats.map((seat: Element) => Array.from(seats).indexOf(seat));
      const vipSeatsIndex: number[] = selectedVipSeats.map((seat: Element) => Array.from(seats).indexOf(seat));
      console.log(seatsIndex);
      localStorage.setItem("selectedSeats", JSON.stringify(seatsIndex));
      localStorage.setItem("selectedVipSeats", JSON.stringify(vipSeatsIndex));

      const selectedSeatsCount: number = selectedSeats.length;
      const selectedVipSeatsCount: number = selectedVipSeats.length;
      console.log(selectedSeatsCount);
      // if((selectedSeatsCount + selectedVipSeatsCount) <= 6){
      if (selectedVipSeatsCount > 0 && selectedSeatsCount > 0) {
        total.innerText = ((selectedVipSeatsCount * vipPrice) + (regularPrice * selectedSeatsCount)).toString();
        count.innerText = (selectedVipSeatsCount + selectedSeatsCount).toString();
      }
      else if (selectedVipSeatsCount > 0) {
        total.innerText = (selectedVipSeatsCount * vipPrice).toString();
        count.innerText = (selectedVipSeatsCount).toString();
      }
      else {
        total.innerText = (selectedSeatsCount * regularPrice).toString();
        count.innerText = (selectedSeatsCount).toString();
      }
    }

    // Initial count and total set
    updateSelectedCount();
  }

  constructor(
    private cookieService: CookieService,
    private loginService: LoginService,
    private router: Router,
    private listMovieService: ListMovieService
  ) {
    this.userDisplayName = this.cookieService.get('full_name');
    // this.cookieService.set("listSeat", JSON.stringify(this.listMovieService.getListData()));
    // this.listSeat = this.listMovieService.getListData();
    // console.log("listSeat bên booking nè: " + JSON.parse(JSON.stringify(this.listMovieService.getListData())));


    if (this.cookieService.get("dataBooking") != null) {
      let idMovie = Number.parseInt(this.cookieService.get("idMovie"));
      let idShowTime = Number.parseInt(this.cookieService.get("idShowTime"));
      this.listMovieService.onclickChooseShowtime(idMovie, idShowTime);
    }
    this.listSeat = JSON.parse("[" + JSON.stringify(this.listMovieService.getListData()) + "]");
    this.listSeat.some(listSeatAll => this.regularSeatPrice = listSeatAll.showtime.price);
    this.listSeat.some(listSeatAll => this.vipSeatPrice = listSeatAll.showtime.price + 10000);

    // this.listSeat=getJSON
    // console.warn("listSeat bên booking nè: " + this.listSeat)

    // console.warn("listSeat bên booking nè: " +  Object.create(JSON.parse(JSON.stringify(this.listMovieService.getListData()))));
  }

  //add css seat
  getStyle(index: number) {
    if (index === 1 || index === 12 || index === 23 || index === 34 || index === 45 || index === 56 || index === 67) {
      return {
        'margin-right': '18px'
      };
    } else if (index === 9 || index === 20 || index === 31 || index === 42 || index === 53 || index === 64 || index === 75) {
      return {
        'margin-left': '18px'
      };
    } else {
      return {}; // Return empty object for other items without any style
    }
  }
  isSeatOccupied(bookings: any[], seatId: number): boolean {
    return bookings.some(booking => booking.seat.seat_id === seatId);
  }
  isSeatSelected(bookings: any[], userId: number, seatId: number): boolean {
    return bookings.some(booking => booking.userID == userId && booking.seat.seat_id === seatId);
  }
  pay() {
    this.listMovieService.setArrayDetailTicket(this.divSeats);
    let total = document.getElementById("total").innerText;
    this.listMovieService.setTotal(Number.parseInt(total));
    let idMovie = Number.parseInt(this.cookieService.get("idMovie"));
    let idShowTime = Number.parseInt(this.cookieService.get("idShowTime"));
    this.listMovieService.onClickLoadBooking(idMovie, idShowTime);
  
  }


  isLogin() {
    return this.loginService.isLogin();
  }

  logout() {
    return this.loginService.logout();
  }
}
