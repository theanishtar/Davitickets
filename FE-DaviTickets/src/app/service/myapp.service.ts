import { Injectable } from '@angular/core';
//Xử lí bất đồng bộ
import { Observable } from 'rxjs';
import { of } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { Router } from '@angular/router';
import { ProfileService } from './profile.service';
import { CookieService } from 'ngx-cookie-service';
@Injectable({
  providedIn: 'root'
})
export class MyappService {
  profile: any;

  loadDataProfile(){
    this.profileService.getProfile().subscribe(res => {
      if(res !=null){
        console.log(JSON.parse(JSON.stringify(res)));
        this.profile=JSON.parse(JSON.stringify(res));
      }
    });
    this.router.navigate(['home']);
  }
  constructor(
    private http: HttpClient,
    private cookieService: CookieService,
    private router: Router,
    private route: ActivatedRoute,
    private httpClient: HttpClient,
    private profileService: ProfileService
  ) { }
}
