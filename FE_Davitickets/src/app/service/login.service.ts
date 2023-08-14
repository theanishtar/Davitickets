import { Injectable, NgZone } from '@angular/core';

//Xử lí bất đồng bộ
import { Observable, throwError } from 'rxjs';
import { of } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { Router } from '@angular/router';

import { CookieService } from 'ngx-cookie-service';
import Swal from 'sweetalert2';
import '../../assets/toast/main.js';
declare var toast: any;
@Injectable({
	providedIn: 'root',
})
export class LoginService {
	private userURL = 'http://localhost:8080/oauth/login';
	// private userCheckCodeMail = 'http://localhost:8080/rest/checkCodeMail';
	private userLoginAuth = 'http://localhost:8080/oauth/login/authenticated';
	private userLogined: any[] = [];


	loginUser(data: any): Observable<any> {

		return this.http.post<any>(this.userURL, data)
			.pipe(
				tap((response) => {
					this.userLogined = JSON.parse(JSON.stringify(response));
					this.setUserLog(this.userLogined);
					console.warn(this.userLogined);
					localStorage.setItem(
						'token',
						JSON.parse(JSON.stringify(this.getUserLog())).token
					);
				}),
				catchError((error: HttpErrorResponse) => {
					console.log("error.status 2: " + error.status)
					if (error.status === 200) {
						// Handle successful response
						return [];
					} else if (error.status === 301) {
						// Handle redirect
						// You might want to navigate to the new location
						return throwError(
							new toast({
								title: 'Thất bại!',
								message: 'Vui lòng kiểm tra lại thông tin',
								type: 'error',
								duration: 1500,
							})
						);
					} else if (error.status === 303) {
						// Handle see other
						// You might want to follow the redirect
						return throwError(
							new toast({
								title: 'Thông báo!',
								message: error.error.name,
								type: 'warning',
								duration: 1500,
							})
						);
					} else {
						// Handle other errors
						return throwError(
							new toast({
								title: 'Server hiện không hoạt động!',
								message: 'Vui lòng quay lại sau, DaviTickets chân thành xin lỗi vì bất tiện này!',
								type: 'warning',
								duration: 1500,
							})
						);
					}
				})
			);
	}
	status: string;
	loginAuth(data: any) {
		//alert("dataparsms:"+data);
		var strData = { data: data };
		// return this.http.post(this.userURL, data);
		return this.http.post<any>(this.userLoginAuth, strData).pipe(
			tap((res) => {
				this.userLogined = JSON.parse(JSON.stringify(res));
				this.setUserLog(this.userLogined);
				console.warn(this.userLogined);
				localStorage.setItem(
					'token',
					JSON.parse(JSON.stringify(this.getUserLog())).token
				);
			}),
			catchError((error) => of([]))
		);
	}


	// checkCodeMail(data: any) {
	// 	return this.http.post<any>(this.userCheckCodeMail, data).pipe(
	// 		tap((receivedUser) =>
	// 			console.log(`receivedUser = ${JSON.stringify(receivedUser)}`)
	// 		),
	// 		catchError((error) => of([]))
	// 	);
	// }


	constructor(
		private http: HttpClient,
		private cookieService: CookieService,
		private router: Router,
		private route: ActivatedRoute,
		private httpClient: HttpClient
	) { }

	isLogin(): boolean {
		if (this.cookieService.get('isUserLoggedIn') == '') {
			return false;
		}
		return true;
	}

	logout(): void {
		function delay(ms: number) {
			return new Promise(function (resolve) {
				setTimeout(resolve, ms);
			});
		}
		Swal.fire({
			title: 'Bạn muốn đăng xuất?',
			icon: 'warning',
			showCancelButton: true,
			confirmButtonColor: '#d33',
			cancelButtonColor: '#3085d6',
			confirmButtonText: 'Yes',
			cancelButtonText: 'No',
		}).then((result) => {
			if (result.value) {
				delay(1).then((res) => {
					this.cookieService.deleteAll();
					localStorage.removeItem('user');
					localStorage.removeItem('token');
					localStorage.removeItem('stoken');
					this.router.navigate(['home']);
					new toast({
						title: 'Đã đăng xuất!',
						message: 'Hẹn gặp lại',
						type: 'warning',
						duration: 2000,
					});
					// delay(1).then((_) => {
					//   location.reload();
					// });
				});
			}
		});
	}

	// Getter
	getUserLog(): any[] {
		return this.userLogined;
	}

	//   Setter
	setUserLog(data: any[]): void {
		this.userLogined = data;
	}
}
