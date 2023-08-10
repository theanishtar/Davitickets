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
import {
  Storage,
  ref,
  uploadBytesResumable,
  getDownloadURL,
} from '@angular/fire/storage';
@Injectable({
  providedIn: 'root'
})
export class MyappService {
  

  // profile: any;
  private profile: any[] = [];
  loadDataProfile(){
    this.profileService.getProfile().subscribe(res => {
      if(res !=null){
        this.profile = JSON.parse(JSON.stringify(res) );
        this.setProfile(this.profile);
        this.cookieService.set("Check", "check load page");
        this.cookieService.set("profile",this.profile.toString());

        // console.log("myapp: " + JSON.stringify(this.getProfile()));
        // console.log("cookie: " + this.cookieService.get("profile"));
        
        this.router.navigate(['profile']);
      }
      
    });
    
  }

  


  // Getter
  getProfile(): any[] {
    return this.profile;
}

//   Setter
setProfile(data: any[]): void {
    this.profile = data;
}
  public file: any = {};
  constructor(
    private http: HttpClient,
    private cookieService: CookieService,
    private router: Router,
    private route: ActivatedRoute,
    private httpClient: HttpClient,
    private profileService: ProfileService,
    public storage: Storage,
  ) { }
}
