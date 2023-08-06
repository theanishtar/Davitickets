import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {
  FormGroup,
  FormBuilder,
  Validators,
  FormControl,
} from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Location } from '@angular/common';
import { LoginService } from '../service/login.service';
import { ProfileService } from '../service/profile.service';
import { delay, catchError } from 'rxjs/operators';
import { CookieService } from 'ngx-cookie-service';
import { ValidatorService } from '../service/validator.service';
import { MyappService } from '../service/myapp.service';
//up ảnh firebase
import {
  Storage,
  ref,
  uploadBytesResumable,
  getDownloadURL,
} from '@angular/fire/storage';
//Xử lí bất đồng bộ
import { Observable } from 'rxjs';
import { of } from 'rxjs';

import Swal from 'sweetalert2';
import '../../assets/toast/main.js';
import { data, error } from 'jquery';
declare var toast: any;

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css'],
})
export class ProfileComponent {
  public profileForm!: FormGroup;
  userDisplayName = '';
  userEmail = '';
  profile: any;
  // isUserLoggedIn = '';
  ngOnInit() {
    this.createFormProfile();
    // this.loadDataProfile();
    // this.createFormProfile();
   }
  submitted: boolean = false;
  constructor(
    private formbuilder: FormBuilder,
    private http: HttpClient,
    private router: Router,
    private location: Location,
    public loginService: LoginService,
    private cookieService: CookieService,
    private validatorService: ValidatorService,
    public storage: Storage,
    private profileService: ProfileService,
    private myappService: MyappService
  ) {
    this.userDisplayName = this.cookieService.get('full_name');
    this.userEmail = this.cookieService.get('userEmail');
    // this.isUserLoggedIn = JSON.parse(this.cookieService.get('isUserLoggedIn'));
    // console.log(this.isUserLoggedIn);
    
    // this.createFormProfile();
  }

  loadDataProfile(){
    return this.myappService.loadDataProfile();
  }

  createFormProfile() {
    this.profileForm = this.formbuilder.group({
      profile_picture: [''],
      full_name: [''],
      email: ['', [Validators.required, Validators.email]],
      phone: [],
      password: [''],
      birthday: [''],
      gender: [],
    });
  }
  get profileFormControl() {
    return this.profileForm.controls;
  }

  updateProfile() {
    // this.submitted = true;
    // if (this.profileForm.valid) {
    //   this.profileService.updateProfile(this.profileForm.value).subscribe(res => {
    //     if (res != '') {
    //       console.log(JSON.parse(JSON.stringify(res)));
       
    //     }
    //   })
    // } else {
    //   return;
    // }
  }

  public file: any = {};

  chooseFile(event: any) {
    this.file = event.target.files[0];
    this.addData();
  }
  addData() {
    const storageRef = ref(this.storage, this.file.name);
    const uploadTast = uploadBytesResumable(storageRef, this.file);
    uploadTast.on(
      'state_changed',
      (snapshot) => { },
      (error) => {
        console.log(error.message);
      },
      () => {
        getDownloadURL(uploadTast.snapshot.ref).then((downloadURL) => {
          var x = downloadURL; 
                        console.log("URL", x);
                        document.getElementById('avatar').innerHTML = x;
         
        });
      }
    );
  }
  isLogin() {
    return this.loginService.isLogin();
  }

  logout() {
    return this.loginService.logout();
  }
}
