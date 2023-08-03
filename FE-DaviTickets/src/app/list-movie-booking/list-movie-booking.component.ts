import { Component, ElementRef, Input, ViewChild } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
// import { ListMovieService } from '../service/list-movie.service';
import { CookieService } from 'ngx-cookie-service';
import { Router } from '@angular/router';
import { LoginService } from '../service/login.service';
import { ListMovieService } from '../service/list-movie.service';
import '../../assets/toast/main.js';
declare var toast: any;
@Component({
  selector: 'app-list-movie-booking',
  templateUrl: './list-movie-booking.component.html',
  styleUrls: ['./list-movie-booking.component.css']
})
export class ListMovieBookingComponent {
  userDisplayName = '';
  items: any[] = [];
  selectedDate: string = ''; // Ngày được chọn
  apiMovieUrl = 'http://localhost:8080/movie/loadMovie';
  dates: Date[] = [];
  currentMonth: number;
  currentYear: number;
  dateList: { date: Date; dayOfWeek: string }[] = [];
  // listSeat:any[] = [];
  constructor(
    private http: HttpClient,
    private cookieService: CookieService,
    private loginService: LoginService,
    private listMovieService: ListMovieService,
    private router: Router,
  ) {
    this.userDisplayName = this.cookieService.get('full_name');
  }

  ngOnInit() {
    // Gọi phương thức để lấy dữ liệu với ngày hiện tại
    this.getSelectedNewDateItems(new Date());

    const today = new Date();
    this.currentMonth = today.getMonth();
    this.currentYear = today.getFullYear();
    this.generateDateList();

    // Add acitve cho menu ngày khi click vào
    const ftco_nav = document.getElementById('ftco-nav')!;
    //'activeCard' to keep track of the currently clicked card
    let activeCard: any = null;
    function handleClick(event: any) {
      const clickedCard = event.target.closest('.nav-item');

      if (clickedCard) {
        if (activeCard) {
          activeCard.removeEventListener('click', handleClick);
          activeCard.classList.remove('active');
        }

        clickedCard.addEventListener('click', handleClick);
        clickedCard.classList.add('active');

        activeCard = clickedCard;
      }
    }

    ftco_nav.addEventListener('click', handleClick);
  }

  // Phương thức để lấy dữ liệu từ API dựa trên ngày
  getSelectedNewDateItems(selectedDate: Date) {
    this.selectedDate = selectedDate.toISOString().split('T')[0]; // Chuyển đổi ngày thành định dạng ISO yyyy-MM-dd
    const apiUrl = `http://localhost:8080/movie/loadMovie/${this.selectedDate}`;

    this.http.get<any[]>(apiUrl).subscribe(items => {
      this.items = items;
    });
  }

  generateDateList() {
    this.dateList = [];

    const endDate = new Date(this.currentYear, this.currentMonth + 2, 0);
    const currentDate = new Date();

    while (currentDate <= endDate) {
      this.dateList.push({
        date: new Date(currentDate),
        dayOfWeek: currentDate.toLocaleDateString('vi-VN', { weekday: 'long' })
      });
      currentDate.setDate(currentDate.getDate() + 1);
    }
  }

  // Gọi API và truyền ngày vào URL
  getDataByDate(date: Date) {
    const formattedDate = this.formatDate(date);
    const url = `${this.apiMovieUrl}/${formattedDate}`;

    this.http.get<any[]>(url).subscribe(items => {
      if (items.length == 0) {
        new toast({
          title: 'Thông báo!',
          message: 'Lịch chiếu chưa được cập nhật',
          type: 'info',
          duration: 1500,
        });
        this.items = null;
      } else {
        this.items = items;
      }
    });
  }

  // Định dạng ngày thành chuỗi có định dạng 'yyyy-MM-dd' để truyền vào URL
  formatDate(date: Date): string {
    const year = date.getFullYear();
    const month = date.getMonth() + 1;
    const day = date.getDate();

    return `${year}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}`;
  }
onclickChooseMovie(idMovie: number, idShowTime: number){
  return this.listMovieService.onclickChooseShowtime(idMovie, idShowTime);
}
  // onclickChooseShowtime(idMovie: number, idShowTime: number) {
  //   var ck = this.cookieService.get('isUserLoggedIn');
  //   let userid=this.cookieService.get('userid');

  //   var data = {"userid":userid, "idMovie":idMovie, "idShowTime":idShowTime};
    
  //   console.log("Bắt đầu");
  //   // alert(JSON.stringify(data))
    
  //   this.listMovieService.loadDataBooking(data).subscribe(listSeat => {
  //     console.log("aaaaaaaaa: " + listSeat)
  //     if (listSeat != undefined) {
  //     //  this.listSeat = listSeat;
     
  //     this.listMovieService.setListData(listSeat);

  //     alert("1 "+this.listMovieService.getListData())
  //     this.router.navigate(['booking']);
  //     }else{
  //       console.log("Lỗi nè");
  //     }

  //   })
  // }

  isLogin() {
    return this.loginService.isLogin();
  }

  logout() {
    return this.loginService.logout();
  }
}

