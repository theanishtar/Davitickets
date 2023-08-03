import { tap, catchError } from 'rxjs/operators';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { CookieService } from 'ngx-cookie-service';
import { Router } from '@angular/router';
@Injectable({
    providedIn: 'root',
})
export class ListMovieService {
    private urlMovie = 'http://localhost:8080/movie/loadData';
    private listSeat: any[] = [];
    private dataSeat: any[] = [];


    loadDataBooking(data: any) {
        // return this.http.post(this.userURL, data);
        return this.http.post<any[]>(this.urlMovie, data).pipe(
            // tap(() => console.log("Lấy dữ liệu thành công")),
            tap((receivedUser) =>
                this.listSeat = JSON.parse(JSON.stringify(receivedUser))
                // console.log(`receivedUser = ${JSON.stringify(receivedUser)}`
                // )
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
        let userid = this.cookieService.get('userid');

        var data = { "userid": userid, "idMovie": idMovie, "idShowTime": idShowTime };
        this.cookieService.set("dataBooking", data.toString());
        this.cookieService.set("idMovie", idMovie.toString());
        this.cookieService.set("idShowTime", idShowTime.toString());

        this.loadDataBooking(data).subscribe(listSeat => {
            if (listSeat != undefined) {
                this.listSeat = JSON.parse(JSON.stringify(this.listSeat));
                this.setListData(listSeat);
                console.warn(this.listSeat)
                this.router.navigate(['booking']);
            } else {
                console.log("Lỗi nè");
            }

        })
    }
    // Getter
    getListData(): any[] {
        return this.dataSeat;
    }

    //   Setter
    setListData(data: any[]): void {
        this.dataSeat = data;
    }


}