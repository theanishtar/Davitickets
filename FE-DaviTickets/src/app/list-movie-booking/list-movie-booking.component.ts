import { Component } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
// import { ListMovieService } from '../service/list-movie.service';

import '../../assets/toast/main.js';
declare var toast: any;
@Component({
  selector: 'app-list-movie-booking',
  templateUrl: './list-movie-booking.component.html',
  styleUrls: ['./list-movie-booking.component.css']
})
export class ListMovieBookingComponent {
  items: any[] = [];
  selectedDate: string = ''; // Ngày được chọn
  apiMovieUrl = 'http://localhost:8080/movie/loadMovie';
  dates: Date[] = [];
  currentMonth: number;
  currentYear: number;
  dateList: { date: Date; dayOfWeek: string }[] = [];


  constructor(private http: HttpClient) { }

  ngOnInit() {
    // Gọi phương thức để lấy dữ liệu với ngày hiện tại
    this.getSelectedNewDateItems(new Date());

    const today = new Date();
    this.currentMonth = today.getMonth();
    this.currentYear = today.getFullYear();
    this.generateDateList();

    // 
    const ftco_nav = document.getElementById('ftco-nav')!;
    //'activeCard' to keep track of the currently clicked card
    let activeCard: any = null;
    function handleClick(event: any) {
      const clickedCard = event.target.closest('.nav-item');
      const parentElement = document.getElementById('parentElement');
      const firstChild = parentElement.firstElementChild;
      firstChild.classList.add('active');
      if (clickedCard) {
        if (activeCard) {
          activeCard.removeEventListener('click', handleClick);
          activeCard.classList.remove('active');
          // firstChild.classList.remove('active');
        }

        clickedCard.addEventListener('click', handleClick);
        clickedCard.classList.add('active');
        // firstChild.classList.remove('active');

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
      console.log(items);
      if(items.length == 0){
        new toast({
          title: 'Thông báo!',
          message: 'Lịch chiếu chưa được cập nhật',
          type: 'info',
          duration: 1500,
        });
        this.items = null;
      }else{
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
}

