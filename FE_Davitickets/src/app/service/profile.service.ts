import { Injectable } from '@angular/core';
//Xử lí bất đồng bộ
import { Observable } from 'rxjs';
import { of } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { Router } from '@angular/router';

import { CookieService } from 'ngx-cookie-service';

@Injectable({
    providedIn: 'root'
})
export class ProfileService {
    private profileURL = 'http://localhost:8080/profile/data';
    private updateProfileURL = 'http://localhost:8080/profile/update';
    private updatePasswordURL = 'http://localhost:8080/profile/changePassword';

    getProfile() {
        return this.http.get<any>(this.profileURL).pipe(
            tap((receivedUser) =>
                console.log(`receivedUser = ${JSON.stringify(receivedUser)}`)
            ),
            catchError((error) => of([]))
        );
    }


    updateProfile(data: any): Observable<any> {
        return this.http.post(this.updateProfileURL, data).pipe(
            tap(updateMovie => console.log(`updateMovie = ${JSON.stringify(updateMovie)}`)),
            catchError(error => of([]))
        );
    }

    updatePassoword(data: any): Observable<any> {
        return this.http.post(this.updatePasswordURL, data).pipe(
            tap(updatePass => console.log(`updatePassword = ${JSON.stringify(updatePass)}`)),
            catchError(error => of([]))
        );
    }

    constructor(
        private http: HttpClient,
        private cookieService: CookieService,
        private router: Router,
        private route: ActivatedRoute,
        private httpClient: HttpClient
    ) { }
}
