import { tap, catchError } from 'rxjs/operators';
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { CookieService } from 'ngx-cookie-service';
import { Router } from '@angular/router';
@Injectable({
	providedIn: 'root',
})
export class ListMovieService {
	private urlMovie = 'http://localhost:8080/movie/loadData';
	private urlMovieBooking = 'http://localhost:8080/movie/booking';
	private urlMoviePayment = 'http://localhost:8080/movie/payment';
	private listSeat: any[] = [];
	private dataSeat: string[] = [];
	private dataBooking: string[] = [];
	private arrayDetailTicket: any[] = [];
	private total: number = 0;
	private showTime: any[] = [];


	loadDataBooking(data: any) {

		let headers = new HttpHeaders({
			'Authorization': localStorage.getItem("token"),
			'Content-Type': 'application/json'
		});
		// return this.http.post(this.userURL, data);
		return this.http.post<any[]>(this.urlMovie, data, { headers }).pipe(
			// tap(() => console.log("Lấy dữ liệu thành công")),
			tap((receivedUser) =>
				this.listSeat = JSON.parse(JSON.stringify(receivedUser))
				// console.log(`receivedUser = ${JSON.stringify(receivedUser)}`
				// )
			),
			catchError((error) => of([]))
		);
	}
	loadFormBooking(data: any) {

		let headers = new HttpHeaders({
			'Authorization': localStorage.getItem("token"),
			'Content-Type': 'application/json'
		});
		return this.http.post<any[]>(this.urlMovieBooking, data, { headers }).pipe(
			// tap(() => console.log("Lấy dữ liệu thành công")),
			tap((receivedUser) =>
				// this.listSeat = JSON.parse(JSON.stringify(receivedUser))
				// console.log(`receivedUser = ${JSON.stringify(receivedUser)}`)
				console.log("s")
			),
			catchError((error) => of([]))
		);
	}

	loadFormPayment(data: any) {
		let headers = new HttpHeaders({
			'Authorization': localStorage.getItem("token"),
			'Content-Type': 'application/json'
		});
		return this.http.post<any[]>(this.urlMoviePayment, data, { headers }).pipe(
			// tap(() => console.log("Lấy dữ liệu thành công")),
			tap((receivedUser) =>
				// this.listSeat = JSON.parse(JSON.stringify(receivedUser))
				// console.log(`receivedUser = ${JSON.stringify(receivedUser)}`)
				console.log(receivedUser)
			),
			catchError((error) => of([]))
		);
	}

	vnpayPayment() {
		return this.http.get<any>("http://localhost:8080/vnpay-payment").pipe(
			// tap(() => console.log("Lấy dữ liệu thành công")),
			tap((receivedUser) =>
				// this.listSeat = JSON.parse(JSON.stringify(receivedUser))
				// console.log(`receivedUser = ${JSON.stringify(receivedUser)}`)
				console.log(receivedUser)
			),
			catchError((error) => of([]))
		);
	}


	constructor(
		private http: HttpClient,
		private cookieService: CookieService,
		private router: Router,
	) { }

	// onclickChooseShowtime(idMovie: number, idShowTime: number) {
	//     var ck = this.cookieService.get('isUserLoggedIn');
	//     var userid=this.cookieService.get('userid');
	//     var data = {"userid":userid,"idMovie":idMovie, "idShowTime":idShowTime,"cookie":ck};
	//     // var d ={"idMovie":idMovie,"idShowTime":idShowTime}
	//     this.loadDataBooking(data).subscribe((listSeat) => {
	//       if (listSeat !=null) {
	//        return listSeat = listSeat;
	//       }
	//     })
	//     // alert(this.cookieService.get('isUserLoggedIn'))
	// }
	onclickChooseShowtime(idMovie: number, idShowTime: number) {
		var ck = this.cookieService.get('isUserLoggedIn');
		let userEmail = this.cookieService.get('userEmail');

		var data = { "userEmail": userEmail, "idMovie": idMovie, "idShowTime": idShowTime };
		this.cookieService.set("dataBooking", data.toString());
		this.cookieService.set("idMovie", idMovie.toString());
		this.cookieService.set("idShowTime", idShowTime.toString());

		this.loadDataBooking(data).subscribe(listSeat => {
			if (listSeat != undefined) {
				this.listSeat = JSON.parse(JSON.stringify(listSeat));
				this.setListData(listSeat);
				console.warn(this.listSeat)
				this.router.navigate(['booking']);
			} else {
				console.log("Lỗi nè");
			}

		})
	}
	onClickLoadBooking(idMovie: number, idShowTime: number) {

		let userEmail = this.cookieService.get('userEmail');
		// let idMovie = this.cookieService.get("idMovie");
		// let idShowTime = this.cookieService.get("idShowTime");
		var data = { "userEmail": userEmail, "idMovie": idMovie, "idShowTime": idShowTime };
		this.cookieService.set("dataBooking", data.toString());
		this.loadFormBooking(data).subscribe((res) => {
			if (res != undefined) {
				this.showTime = JSON.parse(JSON.stringify(res));
				// this.showTime = JSON.parse(JSON.stringify(res).substring(1,JSON.stringify(res).length-1));
				// console.log("showtime: " + )
				this.setListShowTime(this.showTime);
				this.router.navigate(['pay']);
			}
			else {
				console.log("Lỗi nè");
			}
		});
		this.arrayDetailTicket = this.getArrayDetailTicket();
		this.total = this.getTotal();


	}
	// Getter
	getListData(): any[] {
		return this.dataSeat;
	}
	getListShowTime(): any[] {
		return this.dataBooking;
	}
	getArrayDetailTicket(): string[] {
		return this.arrayDetailTicket;
	}
	getTotal(): number {
		return this.total;
	}
	//   Setter
	setListData(data: any[]): void {
		this.dataSeat = data;

	}
	setListShowTime(data: any[]): void {
		this.dataBooking = data;

	}
	setArrayDetailTicket(data: string[]): void {
		this.arrayDetailTicket = data;
	}
	setTotal(data: number): void {
		this.total = data;
	}

}