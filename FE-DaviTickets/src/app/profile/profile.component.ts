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
import { delay, catchError } from 'rxjs/operators';
import { CookieService } from 'ngx-cookie-service';
import { ValidatorService } from '../service/validator.service';
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
  // isUserLoggedIn = '';
  ngOnInit() {}

  constructor(
    private formbuilder: FormBuilder,
    private http: HttpClient,
    private router: Router,
    private location: Location,
    public loginService: LoginService,
    private cookieService: CookieService,
    private validatorService: ValidatorService,
    public storage: Storage
  ) {
    this.userDisplayName = this.cookieService.get('full_name');
    // this.isUserLoggedIn = JSON.parse(this.cookieService.get('isUserLoggedIn'));
    // console.log(this.isUserLoggedIn);
  }

  createFormProfile() {
    this.profileForm = this.formbuilder.group({
      profile_picture: [''],
      full_name: [''],
      email: [
        '',
        [
          Validators.required,
          Validators.email,
          this.validatorService.emailValidator(),
        ],
      ],
      phone: [],
      password: ['', Validators.required],
      birthday: [''],
      gender: [],
    });
  }
  get profileFormControl() {
    return this.profileForm.controls;
  }

  updateProfile() {}

  public file: any = {};

  chooseFile(event: any) {
    this.file = event.target.files[0];
  }
  addData() {
    const storageRef = ref(this.storage, this.file.name);
    const uploadTast = uploadBytesResumable(storageRef, this.file);
    uploadTast.on(
      'state_changed',
      (snapshot) => {},
      (error) => {
        console.log(error.message);
      },
      () => {
        getDownloadURL(uploadTast.snapshot.ref).then((downloadURL) => {
          console.log('Upload file : ', downloadURL);
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
